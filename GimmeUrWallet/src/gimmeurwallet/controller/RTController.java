package gimmeurwallet.controller;

import gimmeurwallet.model.Roulette;
import gimmeurwallet.model.Escape;
import gimmeurwallet.view.RTView;
import javax.swing.JOptionPane;

/**
 * Gestisce l'interazione tra l'utente, il modello della roulette e la visualizzazione grafica.
 */
public class RTController {
	private final Roulette model;
	private final RTView view;
	private final Escape theGame;
	private final int CHIP = 10;

	/**
	 * Costruttore del controller. Registra se stesso nella vista e inizializza il saldo.
	 * @param model Il modello logico della roulette.
	 * @param view Il frame principale della vista.
	 * @param theGame Il riferimento al sistema di portafoglio.
	 */
	public RTController(Roulette model, RTView view, Escape theGame) {
		this.model = model;
		this.view = view;
		this.theGame = theGame;
		this.view.registerController(this);

		try {
			double initialMoney = this.theGame.getWallet();
			this.view.getRoulettePanel().updateWalletLabel(initialMoney);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gestisce l'evento di puntata su un numero della tabella.
	 * @param number Il numero scelto.
	 * @param isRightClick true se l'azione è una rimozione (tasto destro).
	 */
	public void onNumberBet(int number, boolean isRightClick) {
		model.addNumberBet(number, CHIP, isRightClick);
		view.repaintTable();
	}

	/**
	 * Gestisce l'evento di puntata su una zona esterna (es. Rosso/Nero).
	 * @param type La stringa identificativa della zona.
	 * @param isRightClick true se l'azione è una rimozione (tasto destro).
	 */
	public void onExternalBet(String type, boolean isRightClick) {
		model.addExternalBet(type, CHIP, isRightClick);
		view.repaintTable();
	}

	/**
	 * Gestisce la richiesta di inizio rotazione della ruota.
	 */
	public void onSpinRequested() {
		if (!model.hasBets()) {
			JOptionPane.showMessageDialog(view, "Devi prima puntare!");
			return;
		}
		model.setLocked(true);
		view.getRoulettePanel().startVisualSpin();
	}

	/**
	 * Elabora il risultato finale dopo che la ruota si è fermata.
	 * Aggiorna il portafoglio e mostra il messaggio di esito.
	 * @param drawn Il numero estratto dalla ruota.
	 */
	public void onSpinFinished(int drawn) {
		int won = model.calculateWin(drawn);
		int bet = model.getTotalBet();
		int balance = won - bet;

		try {
			if (balance > 0) {
				theGame.addMoney(balance);
			} else if (balance < 0) {
				theGame.removeMoney(Math.abs(balance));
			}

			double currentMoney = theGame.getWallet();
			view.getRoulettePanel().updateWalletLabel(currentMoney);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String message = (won > 0) ?
				"VITTORIA!\nNumero uscito: " + drawn + "\nVinto: " + balance + "€" :
				"HAI PERSO\nNumero uscito: " + drawn + "\nPerso: -" + bet + "€";

		JOptionPane.showMessageDialog(view, message);
		model.clearBets();
		model.setLocked(false);
		view.repaintTable();
		view.getRoulettePanel().resetSpinState();
	}

	/** @return Il modello logico associato. */
	public Roulette getModel() { return model; }
}
