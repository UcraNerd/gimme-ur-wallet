package gimmeurwallet.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import gimmeurwallet.model.Escape;
import gimmeurwallet.model.Roulette;
import gimmeurwallet.view.RTView;

public class RTController implements MouseListener {
	private Roulette rtModel;
	private Escape theGame;
	private RTView rtView;
	
	public RTController(Roulette rtModel, Escape theGame, RTView rtView) {
		this.rtModel = rtModel;
		this.theGame = theGame;
		this.rtView = rtView;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
