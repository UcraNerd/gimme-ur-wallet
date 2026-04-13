package gimmeurwallet.controller;

import gimmeurwallet.model.BlackJack;
import gimmeurwallet.model.Escape;
import gimmeurwallet.view.BJView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BJController implements MouseListener {
	private BlackJack bjModel;
	private Escape theGame;
	private BJView bjView;
	
	public BJController(BlackJack bjModel, Escape theGame, BJView bjView) {
		this.bjModel = bjModel;
		this.theGame = theGame;
		this.bjView = bjView;
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
