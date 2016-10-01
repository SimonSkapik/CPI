package pkg.skapik.cpi.functions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pkg.skapik.cpi.assets.Container;
import pkg.skapik.cpi.assets.Material;
import pkg.skapik.cpi.assets.Materials;
import pkg.skapik.cpi.assets.Object_3D;


public class XML_Reader {

	private Materials materials;
	private Map<Integer,String> id_map;
	private Map<Integer,String> data_3D_map;
	private Container object_root;
	private Document doc;

	public XML_Reader(){
		this.materials = null;
		this.object_root = null;
		this.id_map = new HashMap<Integer,String>();
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
			id_map.put(Integer.parseInt(nodeMap.getNamedItem("k").getNodeValue()), nodeMap.getNamedItem("v").getNodeValue());
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
		object_root = new Container(Integer.parseInt(nodeMap.getNamedItem("ID").getNodeValue()), nodeMap.getNamedItem("name").getNodeValue());
		String[] vTrans_params = nodeMap.getNamedItem("vTrans").getNodeValue().split(" ");
		object_root.set_vTrans(new int[]{Integer.parseInt(vTrans_params[0]),Integer.parseInt(vTrans_params[1]),Integer.parseInt(vTrans_params[2])});
		load_object_tree_branch(object_root, rootNode);
	}
	
	private void load_object_tree_branch(Container container, Node node){
		NodeList nodeList = node.getChildNodes();
		NamedNodeMap nodeMap = null;
		int len = nodeList.getLength();
		for(int i = 0; i < len; i++){
			if(nodeList.item(i).getNodeName() == "container"){
				nodeMap = nodeList.item(i).getAttributes();
				Container con = new Container(Integer.parseInt(nodeMap.getNamedItem("ID").getNodeValue()), nodeMap.getNamedItem("name").getNodeValue());
				container.add_container(con);
				load_object_tree_branch(con, nodeList.item(i));
			}else if(nodeList.item(i).getNodeName() == "object3D"){
				nodeMap = nodeList.item(i).getAttributes();
				container.add_object(new Object_3D(Integer.parseInt(nodeMap.getNamedItem("ID").getNodeValue()), nodeMap.getNamedItem("name").getNodeValue(), Integer.parseInt(nodeMap.getNamedItem("matID").getNodeValue())));
			}
		}
	}
	
	private void load_3d_data(){
		NodeList nodeList = doc.getElementsByTagName("data3D");
		NodeList dataNodeList = null;
		int len = nodeList.getLength();
		for(int i = 0; i < len; i++){
			dataNodeList = nodeList.item(i).getChildNodes();
		}
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
