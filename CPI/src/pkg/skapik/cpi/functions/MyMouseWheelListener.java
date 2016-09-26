package pkg.skapik.cpi.functions;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import pkg.skapik.cpi.main.CPI;

public class MyMouseWheelListener implements MouseWheelListener{

	private CPI module;
	
	public MyMouseWheelListener(CPI module){
		this.module = module;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe) {
		this.module.mouse_wheel_moved(mwe);
	}

}
