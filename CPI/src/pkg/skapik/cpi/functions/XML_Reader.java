package pkg.skapik.cpi.functions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pkg.skapik.cpi.assets.Container;
import pkg.skapik.cpi.assets.Draw_3D_Chain;
import pkg.skapik.cpi.assets.Draw_3D_Polygon;
import pkg.skapik.cpi.assets.Draw_3D_Solid;
import pkg.skapik.cpi.assets.Face;
import pkg.skapik.cpi.assets.Material;
import pkg.skapik.cpi.assets.Materials;
import pkg.skapik.cpi.assets.Object_3D;
import pkg.skapik.cpi.assets.Object_data;
import pkg.skapik.cpi.assets.Triangle;
import pkg.skapik.cpi.assets.Vertex;


public class XML_Reader {

	private Materials materials;
	private Map<Integer,Object_data> object_list;
	//private Map<Integer,String> data_3D_map;
	private Container object_root;
	private Document doc;
	


	public XML_Reader(){
		this.materials = null;
		this.object_root = null;
		this.object_list = new HashMap<Integer,Object_data>();
	}

	public void Load_XML(String file_path){
		try {
	
			File file = new File(file_path);
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = dBuilder.parse(file);
	
			if (doc.hasChildNodes()) {
	
				scan_ids();
				load_materials();
				load_object_tree();
				load_3d_data();
			}

	    } catch (Exception e) {
	    	System.out.println(e.getMessage());
	    }
	}

	private void scan_ids() {
		NamedNodeMap nodeMap = null;
		NodeList nodeList = doc.getElementsByTagName("ID");
		int len =  nodeList.getLength();
		for (int i = 0; i < len; i++) {
			nodeMap = nodeList.item(i).getAttributes();
			object_list.put(Integer.parseInt(nodeMap.getNamedItem("k").getNodeValue()), new Object_data(nodeMap.getNamedItem("v").getNodeValue()) );
		}
	}

	private void load_materials() {
		
		float[] diff = new float[4],amb = new float[4],spec = new float[4];
		float trans = 0;
		String name = "";
		int mat_id = 0;
		
		this.materials = new Materials();
		
		NamedNodeMap nodeMap = null;
		NodeList nodeList = doc.getElementsByTagName("material");
		NodeList lightNodes = null;
		
		int len = nodeList.getLength();
		int len2 = 0;
		for (int i = 0; i < len; i++) {
			
			nodeMap = nodeList.item(i).getAttributes();
			mat_id = Integer.parseInt(nodeMap.getNamedItem("ID").getNodeValue());
			name = nodeMap.getNamedItem("name").getNodeValue();
			
			lightNodes = nodeList.item(i).getChildNodes();
			
			len2 = lightNodes.getLength();
			
			for (int j = 0; j < len2; j++) {
				nodeMap = lightNodes.item(j).getAttributes();
				
				switch(lightNodes.item(j).getNodeName()){
					case "diff":{
						diff[0] = Integer.parseInt(nodeMap.getNamedItem("r").getNodeValue())/255.0f;
						diff[1] = Integer.parseInt(nodeMap.getNamedItem("g").getNodeValue())/255.0f;
						diff[2] = Integer.parseInt(nodeMap.getNamedItem("b").getNodeValue())/255.0f;
						diff[3] = Float.parseFloat(nodeMap.getNamedItem("a").getNodeValue());
					}break;
					case "amb":{
						amb[0] = Integer.parseInt(nodeMap.getNamedItem("r").getNodeValue())/255.0f;
						amb[1] = Integer.parseInt(nodeMap.getNamedItem("g").getNodeValue())/255.0f;
						amb[2] = Integer.parseInt(nodeMap.getNamedItem("b").getNodeValue())/255.0f;
						amb[3] = Float.parseFloat(nodeMap.getNamedItem("a").getNodeValue());
					}break;
					case "spec":{
						spec[0] = Integer.parseInt(nodeMap.getNamedItem("r").getNodeValue())/255.0f;
						spec[1] = Integer.parseInt(nodeMap.getNamedItem("g").getNodeValue())/255.0f;
						spec[2] = Integer.parseInt(nodeMap.getNamedItem("b").getNodeValue())/255.0f;
						spec[3] = Float.parseFloat(nodeMap.getNamedItem("a").getNodeValue());
					}break;
					case "trans":{
						trans = Float.parseFloat(nodeMap.getNamedItem("v").getNodeValue());
					}break;
				}
			}
			
			materials.add_material(new Material(mat_id,name,diff,amb,spec,trans));
		}
		
	}

	private void load_object_tree(){
		Node rootNode = doc.getElementsByTagName("rootContainer").item(0);
		NamedNodeMap nodeMap = rootNode.getAttributes();
		int node_id = Integer.parseInt(nodeMap.getNamedItem("ID").getNodeValue());
		object_root = new Container(node_id, nodeMap.getNamedItem("name").getNodeValue(), object_list.get(node_id));
		String[] vTrans_params = nodeMap.getNamedItem("vTrans").getNodeValue().split(" ");
		object_root.set_vTrans(new int[]{Integer.parseInt(vTrans_params[0]),Integer.parseInt(vTrans_params[1]),Integer.parseInt(vTrans_params[2])});
		load_object_tree_branch(object_root, rootNode);
	}
	
	private void load_object_tree_branch(Container container, Node node){
		NodeList nodeList = node.getChildNodes();
		NamedNodeMap nodeMap = null;
		int node_id = 0;
		int len = nodeList.getLength();
		for(int i = 0; i < len; i++){
			if(nodeList.item(i).getNodeName() == "container"){
				nodeMap = nodeList.item(i).getAttributes();
				node_id = Integer.parseInt(nodeMap.getNamedItem("ID").getNodeValue());
				Container con = new Container(node_id, nodeMap.getNamedItem("name").getNodeValue(), object_list.get(node_id));
				container.add_container(con);
				load_object_tree_branch(con, nodeList.item(i));
			}else if(nodeList.item(i).getNodeName() == "object3D"){
				nodeMap = nodeList.item(i).getAttributes();
				node_id = Integer.parseInt(nodeMap.getNamedItem("ID").getNodeValue());
				container.add_object(new Object_3D(node_id, nodeMap.getNamedItem("name").getNodeValue(), Integer.parseInt(nodeMap.getNamedItem("matID").getNodeValue()), object_list.get(node_id)));
			}
		}
	}
	
	private void load_3d_data(){
		NodeList nodeList = doc.getElementsByTagName("data3D");
		NodeList dataNodeList = null;
		Node curr_node = null;
		NamedNodeMap nodeMap = null;
		
		Draw_3D_Chain chain_3D_data = null;
		Draw_3D_Solid solid_3D_data = null;
		Draw_3D_Polygon poly_3D_data = null;
		
		int node_id = -1;
		int node_type = -1;
		String node_name = "";
		String[] chain_indices;
		ArrayList<Vertex> vertex_list;
		
		
		int len = nodeList.getLength();
		int len2;
		
		for(int i = 0; i < len; i++){
			dataNodeList = nodeList.item(i).getChildNodes();
			nodeMap = nodeList.item(i).getAttributes();
			node_id = Integer.parseInt(nodeMap.getNamedItem("refID").getNodeValue());
			vertex_list = new ArrayList<>();
			len2 = dataNodeList.getLength();
			for(int j = 0; j < len2; j++){
				curr_node = dataNodeList.item(j);
				node_name = curr_node.getNodeName();
				nodeMap = dataNodeList.item(j).getAttributes();
				
				if(node_name.compareTo("#text") == 0){
					node_type = 0;
				}else if(node_name.compareTo("p") == 0){ // Vertex
					vertex_list.add(new Vertex(Integer.parseInt(nodeMap.getNamedItem("nr").getNodeValue()),
												Double.parseDouble(nodeMap.getNamedItem("x").getNodeValue())/100.0,
												Double.parseDouble(nodeMap.getNamedItem("y").getNodeValue())/100.0,
												Double.parseDouble(nodeMap.getNamedItem("z").getNodeValue())/100.0));
				}else if(node_name.compareTo("pl") == 0){ // Chain object
					
					chain_3D_data = new Draw_3D_Chain();
					if(nodeMap.getNamedItem("type").getNodeValue().compareTo("Chain") == 0){
						node_type = 1;
						chain_indices = nodeMap.getNamedItem("points").getNodeValue().split(" ");
						chain_3D_data.set_vertices(vertex_list);
						chain_3D_data.add_chain(Arrays.stream(chain_indices).mapToInt(Integer::parseInt).toArray());
					}else{
						System.err.println("Unsupported type in chain object " + node_id);
					}
					
				}else if(node_name.compareTo("solid") == 0){ // Solid object
					
					solid_3D_data = new Draw_3D_Solid();
					node_type = 2;
					
					solid_3D_data.set_vertices(vertex_list);
					parse_faces(curr_node, solid_3D_data);
					
					
				}else if(node_name.compareTo("face") == 0){ // Polygon object
					
					poly_3D_data = new Draw_3D_Polygon();
					node_type = 3;
					
					poly_3D_data.set_vertices(vertex_list);
					parse_polygon_face(curr_node, poly_3D_data, node_id);
					
				}else if(node_name.compareTo("optOBBxy") == 0){
					// to do
				}else if(node_name.compareTo("optOBB") == 0){
					// to do
				}
				
				switch(node_type){
					case 0:{
						
					}break;
					case 1:{
						object_list.get(node_id).add_draw_data(chain_3D_data);
					}break;
					case 2:{
						object_list.get(node_id).add_draw_data(solid_3D_data);
					}break;
				 	case 3:{
				 		object_list.get(node_id).add_draw_data(poly_3D_data);
					}break;
					default:{
						System.err.println("Unsupported object class for ID " + node_id);
					}
				}
				
			}
		}
	}

	private void parse_faces(Node node, Draw_3D_Solid solid_3d_data) {
		NodeList faceNodeList = node.getChildNodes();
		NodeList triangleList = null;
		NamedNodeMap nodeMap = null;
		Face face = null;
		
		int len = faceNodeList.getLength();
		int len2 = 0;
		
		for(int i = 0; i < len; i++){
			
			if(faceNodeList.item(i).getNodeName().equals("face")){
				nodeMap = faceNodeList.item(i).getAttributes();
				face = new Face(nodeMap.getNamedItem("shape").getNodeValue());
				triangleList = faceNodeList.item(i).getChildNodes();

				len2 = triangleList.getLength();
				for(int j = 0; j < len2; j++){
					if(triangleList.item(j).getNodeName().equals("t")){
						nodeMap = triangleList.item(j).getAttributes();
						face.add_triangle(new Triangle(Integer.parseInt(nodeMap.getNamedItem("p1").getNodeValue()),
														Integer.parseInt(nodeMap.getNamedItem("p2").getNodeValue()),
														Integer.parseInt(nodeMap.getNamedItem("p3").getNodeValue())));
						
					}
				}
				
				solid_3d_data.add_face(face);
			}
			
		}
		
		solid_3d_data.compile_indices();
		
	}
	
	private void parse_polygon_face(Node node, Draw_3D_Polygon poly_3d_data, int id) {
		NodeList triangleList = node.getChildNodes();
		NamedNodeMap nodeMap = node.getAttributes();
		Face face = new Face(nodeMap.getNamedItem("shape").getNodeValue());
		
		int len = 0;
		if(triangleList.item(1).getNodeName().equals("pl")){
			triangleList = triangleList.item(1).getChildNodes().item(1).getChildNodes();
		}

		len = triangleList.getLength();
		for(int j = 0; j < len; j++){
			if(triangleList.item(j).getNodeName().equals("t")){
				nodeMap = triangleList.item(j).getAttributes();
				face.add_triangle(new Triangle(Integer.parseInt(nodeMap.getNamedItem("p1").getNodeValue()),
												Integer.parseInt(nodeMap.getNamedItem("p2").getNodeValue()),
												Integer.parseInt(nodeMap.getNamedItem("p3").getNodeValue())));
				
			}
		}
		
		poly_3d_data.add_face(face);
		poly_3d_data.compile_indices();
	}

	public Materials get_materials() {
		if(this.materials != null)
			return this.materials;
		return new Materials();
	}

	public Container get_object_tree() {
		return object_root;
	}
	
}
