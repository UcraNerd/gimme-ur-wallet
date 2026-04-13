package gimmeurwallet.controller;

import gimmeurwallet.model.Escape;
import gimmeurwallet.view.View;

public class Main {

	public static void main(String[] args) {
		View mainView = new View();
		Escape theGame = new Escape();
		Controller controller = new Controller(theGame);
		mainView.registerController(controller);
		mainView.setVisible(true);
	}
}
