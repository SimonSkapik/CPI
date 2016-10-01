package pkg.skapik.cpi.functions;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import pkg.skapik.cpi.main.CPI;

public class Key_Listener implements KeyListener {
	
	private CPI module;
	
	public Key_Listener(CPI module){
		this.module = module;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.module.key_pressed(e.getKeyCode(), e.getID());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.module.key_released(e.getKeyCode(), e.getID());
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
