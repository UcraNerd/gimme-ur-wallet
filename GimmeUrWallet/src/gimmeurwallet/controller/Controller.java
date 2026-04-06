package gimmeurwallet.controller;

import gimmeurwallet.model.Model;
import gimmeurwallet.view.View;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

/**
 * Main controller between the view and the model
 */
public class Controller implements MouseListener {
	Model model;
	View view;
	
	public Controller (Model model, View view) {
		this.model = model;
		this.view = view;
	}

	/**
	 * Method to get mouse events and understand the selected game
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		String game = ((JLabel) e.getSource()).getText();

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

}
