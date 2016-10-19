package pkg.skapik.cpi.functions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

import pkg.skapik.cpi.main.CPI;

public class Mouse_Listener implements MouseListener, MouseMotionListener, MouseWheelListener {

	private CPI module;

	public Mouse_Listener(CPI module){
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

	}

	@Override
	public void mouseReleased(MouseEvent m) {
		this.module.mouse_released(m);
	}

	@Override
	public void mouseDragged(MouseEvent m) {
		if(SwingUtilities.isLeftMouseButton(m)){
			this.module.mouse_dragged(1, m.getX(), m.getY());
		}else if(SwingUtilities.isMiddleMouseButton(m)){
			this.module.mouse_dragged(2, m.getX(), m.getY());
		}else if(SwingUtilities.isRightMouseButton(m)){
			this.module.mouse_dragged(3, m.getX(), m.getY());
		}
	}

	@Override
	public void mouseMoved(MouseEvent m) {
		this.module.mouse_moved(m.getX(), m.getY());
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe) {
		this.module.mouse_wheel_moved(mwe);
	}

}
