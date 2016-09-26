package pkg.skapik.cpi.functions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import pkg.skapik.cpi.main.CPI;

public class MyMouseListener implements MouseListener, MouseMotionListener {

	private CPI module;

	public MyMouseListener(CPI module){
		this.module = module;
	}
	
	@Override
	public void mouseClicked(MouseEvent m) {
		
	}

	@Override
	public void mouseEntered(MouseEvent m) {

	}

	@Override
	public void mouseExited(MouseEvent m) {

	}

	@Override
	public void mousePressed(MouseEvent m) {
		this.module.mouse_clicked(m);
	}

	@Override
	public void mouseReleased(MouseEvent m) {

	}

	@Override
	public void mouseDragged(MouseEvent m) {
		this.module.mouse_dragged(m.getButton(), m.getX(), m.getY());
	}

	@Override
	public void mouseMoved(MouseEvent m) {
		this.module.mouse_moved(m.getX(), m.getY());
	}

}
