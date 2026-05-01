package gimmeurwallet.view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import gimmeurwallet.controller.RTController;

/**
 * Frame principale della Roulette che organizza i componenti della ruota e del tavolo.
 */
public class RTView extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private RoulettePanel roulettePanel;
    private BettingTablePanel bettingTablePanel;

    /**
     * Costruttore della vista. Configura il layout e i sotto-pannelli.
     */
    public RTView() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 950, 900);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));

        roulettePanel = new RoulettePanel();
        bettingTablePanel = new BettingTablePanel();

        contentPane.add(roulettePanel, BorderLayout.CENTER);
        contentPane.add(bettingTablePanel, BorderLayout.SOUTH);
    }

    /**
     * Collega il controller ai pannelli interni per la gestione degli eventi.
     * @param c Il controller da registrare.
     */
    public void registerController(RTController c) {
        roulettePanel.registerController(c);
        bettingTablePanel.registerController(c);
    }

    /** @return Il pannello contenente la ruota animata. */
    public RoulettePanel getRoulettePanel() { return roulettePanel; }

    /** Forza il ridisegno grafico del tavolo delle puntate. */
    public void repaintTable() { bettingTablePanel.repaint(); }
}package gimmeurwallet.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gimmeurwallet.controller.RTController;

public class RTView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RTView frame = new RTView();
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
	public RTView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

	}
	
	public void registerController(RTController c) {
			
	}
}
