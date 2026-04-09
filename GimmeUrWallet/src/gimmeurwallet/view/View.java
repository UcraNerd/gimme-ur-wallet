package gimmeurwallet.view;

import gimmeurwallet.controller.Controller;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

/**
 * Main window of the game
 */

public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblBlackJack1;
	private JLabel lblBlackJack2;
	private JLabel lblSlotMachine1;
	private JLabel lblSlotMachine2;
	private JLabel lblRoulette;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View frame = new View();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public View() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1323, 784);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblBackground = new JLabel("");
		lblBackground.setIcon(new ImageIcon(View.class.getResource("/imgs/background.png")));
		lblBackground.setBounds(0, 0, 1323, 784);
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
	 * Method that adds mouse listeners to JLabels
	 * requires the @param c
	 */
	public void registerController(Controller c) {
		lblBlackJack1.addMouseListener(c);
		lblBlackJack2.addMouseListener(c);
		lblSlotMachine1.addMouseListener(c);
		lblSlotMachine2.addMouseListener(c);
		lblRoulette.addMouseListener(c);
	}
}
