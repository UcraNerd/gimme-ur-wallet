package gimmeurwallet.controller;

import java.awt.event.*;
import java.util.concurrent.Semaphore;
import javax.swing.*;

import gimmeurwallet.model.Escape;
import gimmeurwallet.view.SMView;

/**
 * La classe Controller gestisce la logica di interazione tra la vista e il
 * modello per la Slot Machine. Coordina le azioni dell'utente, come l'avvio
 * dello spin, e supervisiona lo stato dei rulli e l'aggiornamento del saldo.
 * Integra la classe Escape per la gestione sicura del portafoglio.
 */
public class SMController implements ActionListener {

	private final Escape model;
	private final SMView view;
	private boolean isSpinning = false;

	/**
	 * Costruttore del Controller. Collega la vista al modello Escape e inizializza
	 * la visualizzazione del saldo corrente recuperandolo dal portafoglio.
	 *
	 * @param model l'istanza di Escape che gestisce il saldo condiviso
	 * @param view la finestra principale che rappresenta l'interfaccia utente
	 */
	public SMController(Escape model, SMView view) {
		this.model = model;
		this.view = view;
		try {
			view.updateBalance((int) model.getWallet());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Intercetta e gestisce gli eventi generati dall'interfaccia, in particolare
	 * il comando di avvio dei rulli. Verifica la disponibilità del credito
	 * tramite Escape e avvia i thread dedicati all'animazione.
	 *
	 * @param event l'evento d'azione generato dalla vista
	 */
	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getActionCommand().equals("spin")) {

			if (isSpinning) {
				return;
			}

			isSpinning = true;

			view.hideResult();

			try {
				model.removeMoney(10);
				int currentBalance = (int) model.getWallet();
				view.updateBalance(currentBalance);

				if (currentBalance <= 0) {
					JOptionPane.showMessageDialog(view, "Hai finito i soldi!");
					isSpinning = false;
					return;
				}

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				isSpinning = false;
				return;
			}

			Semaphore semaphore = new Semaphore(0);
			ImageIcon[] symbols = view.getSymbols();

			ReelThread thread1 = new ReelThread(view.getReel1(), symbols, semaphore);
			ReelThread thread2 = new ReelThread(view.getReel2(), symbols, semaphore);
			ReelThread thread3 = new ReelThread(view.getReel3(), symbols, semaphore);

			Thread t1 = new Thread(thread1);
			Thread t2 = new Thread(thread2);
			Thread t3 = new Thread(thread3);

			t1.start();
			t2.start();
			t3.start();

			ReelWatcher watcher = new ReelWatcher(semaphore, thread1, thread2, thread3, this);
			Thread tWatcher = new Thread(watcher);
			tWatcher.start();
		}
	}

	/**
	 * Conclude la fase di spin valutando la combinazione ottenuta dai rulli.
	 * Aggiorna il saldo in Escape in base alla vincita e mostra il risultato sulla vista.
	 *
	 * @param value1 indice del simbolo ottenuto sul primo rullo
	 * @param value2 indice del simbolo ottenuto sul secondo rullo
	 * @param value3 indice del simbolo ottenuto sul terzo rullo
	 */
	public void finishSpin(int value1, int value2, int value3) {

		try {
			if (value1 == value2 && value2 == value3) {
				view.setResult("JACKPOT!");
				model.addMoney(10000);
			} else if (value1 == value2 || value2 == value3 || value1 == value3) {
				view.setResult("Piccola vincita!");
				model.addMoney(50);
			} else {
				view.setResult("Ritenta!");
			}

			int currentBalance = (int) model.getWallet();
			view.updateBalance(currentBalance);

			if (currentBalance <= 0) {
				JOptionPane.showMessageDialog(view, "Hai finito i soldi!");
			}

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		isSpinning = false;
	}
}
