package gimmeurwallet.view;

import gimmeurwallet.controller.Controller;
import java.awt.EventQueue;
import java.io.Serial;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

/**
 * Finestra principale del gioco.
 */
public class View extends JFrame {
	private JPanel contentPane;
	private JLabel lblBlackJack1;
	private JLabel lblBlackJack2;
	private JLabel lblSlotMachine1;
	private JLabel lblSlotMachine2;
	private JLabel lblRoulette;

	/**
	 * Inizializza il frame e configura i componenti grafici dell'interfaccia.
	 */
	public View() {
		setTitle("Gimme ur Wallet ( ͡° ͜ʖ ͡°)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1323, 820);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblBackground = new JLabel("");
		lblBackground.setIcon(new ImageIcon(View.class.getResource("/imgs/background.png")));
		lblBackground.setBounds(0, 0, 1308, 784);
		contentPane.add(lblBackground);

		lblBlackJack1 = new JLabel("bj1");
		lblBlackJack1.setBounds(229, 162, 255, 207);
		contentPane.add(lblBlackJack1);

		lblBlackJack2 = new JLabel("bj2");
		lblBlackJack2.setBounds(852, 162, 255, 207);
		contentPane.add(lblBlackJack2);

		lblSlotMachine1 = new JLabel("sm1");
		lblSlotMachine1.setBounds(26, 430, 155, 227);
		contentPane.add(lblSlotMachine1);

		lblSlotMachine2 = new JLabel("sm2");
		lblSlotMachine2.setBounds(182, 545, 290, 159);
		contentPane.add(lblSlotMachine2);

		lblRoulette = new JLabel("rt");
		lblRoulette.setBounds(510, 281, 317, 312);
		contentPane.add(lblRoulette);
	}

	/**
	 * Registra il controller aggiungendo i listener del mouse a tutti i componenti
	 * cliccabili (JLabel) dell'interfaccia.
	 *
	 * @param c Il controller che gestirà gli eventi del mouse.
	 */
	public void registerController(Controller c) {
		lblBlackJack1.addMouseListener(c);
		lblBlackJack2.addMouseListener(c);
		lblSlotMachine1.addMouseListener(c);
		lblSlotMachine2.addMouseListener(c);
		lblRoulette.addMouseListener(c);
	}
}
