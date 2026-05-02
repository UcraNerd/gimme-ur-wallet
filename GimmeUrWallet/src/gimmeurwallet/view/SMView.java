package gimmeurwallet.view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import gimmeurwallet.controller.SMController;

/**
 * La classe Window rappresenta la finestra principale dell'applicazione della
 * slot machine. Contiene tutti i componenti grafici necessari per interagire
 * con l'utente, come i rulli, il pulsante spin e la visualizzazione del saldo e
 * dei risultati.
 */
public class SMView extends JFrame {
	private JPanel contentPane;
	private JLabel reel1, reel2, reel3;
	private JLabel resultLabel;
	private JLabel balanceLabel;
	private JLabel spinButton;
	private ImageIcon[] symbols;

	private boolean isSpinning = false;
	private JLabel resultInfo1;
	private JLabel resultInfo2;
	private JLabel resultInfo3;

	/**
	 * Crea il frame della finestra.
	 */
	public SMView() {
		initializeSettings();
		loadResources();
		setupUI();
	}

	/**
	 * Inizializza le impostazioni base della finestra.
	 */
	private void initializeSettings() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1210, 814);
		setTitle("Slot Machine");
	}

	/**
	 * Carica le icone dei simboli e le risorse grafiche dal percorso delle risorse.
	 */
	private void loadResources() {
		String[] fileNames = {
				"anguria.png", "campana.png", "ciliege.png",
				"limone.png", "sette.png", "uva.png"
		};

		symbols = new ImageIcon[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			symbols[i] = new ImageIcon(getClass().getResource("/imgs/" + fileNames[i]));
		}
	}

	/**
	 * Configura l'interfaccia utente e i componenti grafici.
	 */
	private void setupUI() {
		ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/imgs/back.png"));
		JLabel background = new JLabel(backgroundIcon);
		background.setVerticalAlignment(SwingConstants.TOP);
		background.setHorizontalAlignment(SwingConstants.LEFT);
		background.setBounds(0, 0, 1200, 784);
		background.setLayout(new BorderLayout());
		setContentPane(background);

		contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setLayout(null);
		background.add(contentPane);

		spinButton = new JLabel();
		spinButton.setBounds(812, 335, 40, 80);
		spinButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(spinButton);

		balanceLabel = new JLabel("100 €");
		balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		balanceLabel.setBounds(521, 270, 188, 38);
		balanceLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		balanceLabel.setForeground(Color.GREEN);
		contentPane.add(balanceLabel);

		JLabel priceLabel = new JLabel("10 € PER SPIN");
		priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		priceLabel.setFont(new Font("Dialog", Font.BOLD, 25));
		priceLabel.setBounds(521, 131, 188, 51);
		contentPane.add(priceLabel);

		JPanel reelsPanel = new JPanel();
		reelsPanel.setBounds(474, 310, 282, 190);
		reelsPanel.setOpaque(false);
		reelsPanel.setLayout(null);

		reel1 = createReelLabel(0);
		reel2 = createReelLabel(94);
		reel3 = createReelLabel(188);

		reelsPanel.add(reel1);
		reelsPanel.add(reel2);
		reelsPanel.add(reel3);

		resultLabel = new JLabel("SPINNA!", SwingConstants.CENTER);
		resultLabel.setBounds(0, 0, 282, 40);
		resultLabel.setFont(new Font("Impact", Font.BOLD, 30));
		resultLabel.setForeground(new Color(255, 215, 0));
		resultLabel.setOpaque(true);
		resultLabel.setBackground(Color.BLACK);
		resultLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));

		reelsPanel.add(resultLabel);
		contentPane.add(reelsPanel);

		resultInfo1 = createLegendLabel("+0 Ritenta", 633, new Color(246, 97, 81));
		resultInfo2 = createLegendLabel("+50 Piccola Vincita", 673, new Color(255, 190, 111));
		resultInfo3 = createLegendLabel("+10000 JackPot", 713, new Color(249, 240, 107));

		contentPane.add(resultInfo1);
		contentPane.add(resultInfo2);
		contentPane.add(resultInfo3);
	}

	/**
	 * Metodo di supporto per creare le etichette dei singoli rulli.
	 *
	 * @param xPosition la coordinata X orizzontale dell'etichetta
	 * @return un'istanza di JLabel configurata per il rullo
	 */
	private JLabel createReelLabel(int xPosition) {
		JLabel label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(xPosition, 0, 94, 190);
		return label;
	}

	/**
	 * Metodo di supporto per creare le etichette della legenda informativa.
	 *
	 * @param text il testo da visualizzare
	 * @param yPosition la coordinata Y verticale dell'etichetta
	 * @param color il colore del testo
	 * @return un'istanza di JLabel configurata per la legenda
	 */
	private JLabel createLegendLabel(String text, int yPosition, Color color) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Dialog", Font.BOLD, 16));
		label.setForeground(color);
		label.setBounds(832, yPosition, 168, 38);
		return label;
	}

	/**
	 * Restituisce l'array dei simboli disponibili caricate come ImageIcon.
	 *
	 * @return array di ImageIcon
	 */
	public ImageIcon[] getSymbols() {
		return symbols;
	}

	/**
	 * Registra il controller per gestire gli eventi di interazione,
	 * in particolare il clic sull'area dello spin.
	 *
	 * @param controller il controller incaricato di gestire la logica
	 */
	public void registerController(final SMController controller) {
		spinButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (isSpinning) return;

				isSpinning = true;
				hideResult();
				ActionEvent event = new ActionEvent(spinButton, ActionEvent.ACTION_PERFORMED, "spin");
				controller.actionPerformed(event);
			}
		});
	}

	/**
	 * Nasconde l'etichetta del risultato durante lo spin.
	 */
	public void hideResult() {
		resultLabel.setVisible(false);
	}

	/**
	 * Rende visibile l'etichetta del risultato dello spin.
	 */
	public void showResult() {
		resultLabel.setVisible(true);
	}

	/**
	 * Imposta il testo del risultato e lo mostra a video, terminando lo stato di spin.
	 *
	 * @param text il messaggio di risultato da visualizzare
	 */
	public void setResult(String text) {
		resultLabel.setText(text);
		resultLabel.setVisible(true);
		isSpinning = false;
	}

	/**
	 * Restituisce il componente grafico del primo rullo.
	 *
	 * @return la JLabel del primo rullo
	 */
	public JLabel getReel1() {
		return reel1;
	}

	/**
	 * Restituisce il componente grafico del secondo rullo.
	 *
	 * @return la JLabel del secondo rullo
	 */
	public JLabel getReel2() {
		return reel2;
	}

	/**
	 * Restituisce il componente grafico del terzo rullo.
	 *
	 * @return la JLabel del terzo rullo
	 */
	public JLabel getReel3() {
		return reel3;
	}

	/**
	 * Aggiorna graficamente il saldo attuale dell'utente.
	 *
	 * @param balance la cifra intera del nuovo saldo
	 */
	public void updateBalance(int balance) {
		balanceLabel.setText(balance + " €");
	}
}