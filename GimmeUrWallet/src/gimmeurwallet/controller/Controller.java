package gimmeurwallet.controller;

import gimmeurwallet.model.BlackJack;
import gimmeurwallet.model.Escape;
import gimmeurwallet.model.Roulette;
import gimmeurwallet.view.BJView;
import gimmeurwallet.view.RTView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;

/**
 * Main controller between the view and the model
 */
public class Controller implements MouseListener {
	private final int maxBJs = 2;
	private int bjCount;
	private boolean rtDrawn;
	private Escape theGame;
	
	public Controller(Escape theGame) {
		this.bjCount = 0;
		this.rtDrawn = false;
		this.theGame = theGame;
	}
	
	/**
	 * Method to get mouse events and understand the selected game
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		String game = ((JLabel) e.getSource()).getText();
		if (game.equals("bj1") || game.equals("bj2")) {
			if (bjCount == maxBJs) {
				System.out.println("You reached the max number of blackjack tables!");
			} else {
				drawBlackJack();
				bjCount++;
			}
		}
		if (game.equals("rt") && !rtDrawn) {
			drawRoulette();
			rtDrawn = true;
		} else {
			System.out.println("The roulette has already been started!");
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	private void drawBlackJack() {
		BlackJack bjModel = new BlackJack();
		BJView bjView = new BJView();
		BJController bjController = new BJController(bjModel, theGame, bjView);
		bjView.registerController(bjController);
	    bjView.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosed(WindowEvent e) {
	            System.out.println("This black jack table has been closed...");
	            bjCount--;
	        }
	    });
	    bjView.setVisible(true);
	}
	
	private void drawRoulette() {
		Roulette rtModel = new Roulette();
		RTView rtView = new RTView();
		RTController rtController = new RTController(rtModel, theGame, rtView);
		rtView.registerController(rtController);
		rtView.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosed(WindowEvent e) {
	            System.out.println("The roulette has finished...");
	            rtDrawn = false;
	        }
	    });
		rtView.setVisible(true);
	}
}
