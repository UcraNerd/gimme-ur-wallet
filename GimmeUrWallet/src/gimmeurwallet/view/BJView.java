package gimmeurwallet.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import gimmeurwallet.controller.BJController;
import gimmeurwallet.model.Card;

/**
 * Rappresenta l'interfaccia utente grafica del Blackjack.
 * Gestisce il disegno delle carte, i pulsanti di controllo e i dialoghi di stato.
 */
public class BJView extends JFrame {
    private JPanel contentPane;
    private JLabel playerScoreLabel, dealerScoreLabel;
    private JButton btnHit, btnStay, btnStart;
    private JButton[] betButtons;
    private JButton btnResetBet;
    private JLabel walletLabel;
    private JLabel betLabel;

    /**
     * Costruisce il frame principale e inizializza tutti i componenti grafici.
     */
    public BJView() {
        setTitle("Blackjack Casinò");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1039, 625);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        setupInterface();
    }

    /** Configura i pannelli, i pulsanti e le label informative sul tavolo. */
    private void setupInterface() {
        JPanel gamePanel = new JPanel();
        gamePanel.setBounds(0, 500, 1024, 44);
        gamePanel.setBackground(new Color(30, 30, 30));
        contentPane.add(gamePanel);

        btnStart = new JButton("Distribuisci");
        btnStart.setFont(new Font("Liberation Mono", Font.BOLD, 12));
        btnStart.setBackground(new Color(182, 190, 233));
        btnStart.setFocusPainted(false);
        btnStart.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        
        btnHit = new JButton("Carta");
        btnHit.setFont(new Font("Liberation Mono", Font.BOLD, 12));
        btnHit.setBackground(new Color(250, 222, 165));
        btnHit.setFocusPainted(false);
        btnHit.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        
        btnStay = new JButton("Stai");
        btnStay.setFont(new Font("Liberation Mono", Font.BOLD, 12));
        btnStay.setBackground(new Color(190, 241, 173));
        btnStay.setFocusPainted(false);
        btnStay.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        gamePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        gamePanel.add(btnStart);
        gamePanel.add(btnHit);
        gamePanel.add(btnStay);
        
        betLabel = new JLabel("Puntata Corrente: 0$");
        betLabel.setFont(new Font("Verdana Pro", Font.PLAIN, 14));
        gamePanel.add(betLabel);
        betLabel.setForeground(Color.WHITE);

        playerScoreLabel = new JLabel("I Tuoi Punti: 0");
        playerScoreLabel.setForeground(Color.CYAN);
        playerScoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerScoreLabel.setBounds(30, 250, 250, 40);
        contentPane.add(playerScoreLabel);

        dealerScoreLabel = new JLabel("Punti Banco: ?");
        dealerScoreLabel.setForeground(Color.ORANGE);
        dealerScoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dealerScoreLabel.setBounds(30, 60, 250, 40);
        contentPane.add(dealerScoreLabel);
        
        walletLabel = new JLabel("Saldo: 0$");
        walletLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
        walletLabel.setBounds(30, 218, 227, 32);
        contentPane.add(walletLabel);
        walletLabel.setForeground(Color.GREEN);

        JLabel background = new JLabel(new ImageIcon(BJView.class.getResource("/imgs/table.png")));
        background.setBounds(0, 0, 1024, 500);
        contentPane.add(background);
        contentPane.setComponentZOrder(background, contentPane.getComponentCount() - 1);
        
        JPanel betPanel = new JPanel();
        betPanel.setBackground(new Color(30, 30, 30));
        betPanel.setBounds(0, 542, 1024, 44);
        contentPane.add(betPanel);
        betPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        btnResetBet = new JButton("Resetta Puntata");
        btnResetBet.setFont(new Font("Liberation Mono", Font.BOLD, 12));
        btnResetBet.setFocusPainted(false);
        btnResetBet.setEnabled(true);
        btnResetBet.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnResetBet.setBackground(new Color(255, 159, 159));
        betPanel.add(btnResetBet);
        
        int[] values = {1, 5, 10, 50, 100};
        betButtons = new JButton[values.length];
        for (int i = 0; i < values.length; i++) {
            betButtons[i] = new JButton(values[i] + "$");
            betButtons[i].setFont(new Font("Liberation Mono", Font.BOLD, 12));
            betButtons[i].setBackground(new Color(231, 184, 233));
            betButtons[i].setFocusPainted(false);
            betButtons[i].setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            betPanel.add(betButtons[i]);
        }

        setGamePhase(false);
    }

    /** Rimuove tutte le carte dal tavolo e resetta i testi dei punteggi. */
    public void resetGameUI() {
        for (Component c : contentPane.getComponents()) {
            if (c instanceof JLabel && c.getName() != null && c.getName().startsWith("card")) {
                contentPane.remove(c);
            }
        }
        playerScoreLabel.setText("I Tuoi Punti: 0");
        dealerScoreLabel.setText("Punti Banco: ?");
        contentPane.repaint();
    }

    /**
     * Mostra un messaggio pop-up con il risultato finale.
     * @param message Esito della partita.
     * @param pScore Punti finali giocatore.
     * @param dScore Punti finali banco.
     */
    public void showResultDialog(String message, int pScore, int dScore) {
        JOptionPane.showMessageDialog(this, message + "\nTu: " + pScore + " vs Banco: " + dScore);
    }

    /** Abilita o disabilita i pulsanti in base allo stato del gioco. */
    public void setGamePhase(boolean isPlaying) {
        btnStart.setEnabled(!isPlaying);
        for (JButton b : betButtons) b.setEnabled(!isPlaying);
        btnHit.setEnabled(isPlaying);
        btnStay.setEnabled(isPlaying);
    }

    /** Aggiorna le etichette dei punteggi sul tavolo. */
    public void updateScores(int pScore, String dScore) {
        playerScoreLabel.setText("I Tuoi Punti: " + pScore);
        dealerScoreLabel.setText("Punti Banco: " + dScore);
    }

    /** Aggiorna il testo del wallet. */
    public void updateWallet(double amount) { walletLabel.setText("Saldo: " + amount + "$"); }
    /** Aggiorna il testo della scommessa. */
    public void updateBet(double amount) { betLabel.setText("Puntata Corrente: " + amount + "$"); }

    /**
     * Renderizza le carte di giocatore e banco con effetto di sovrapposizione.
     * @param pHand Mano del giocatore.
     * @param dHand Mano del banco.
     * @param showDealer Se rivelare la carta nascosta.
     * @param back Immagine del dorso.
     */
    public void displayCards(ArrayList<Card> pHand, ArrayList<Card> dHand, boolean showDealer, Image back) {
        for (Component c : contentPane.getComponents()) {
            if (c instanceof JLabel && c.getName() != null && c.getName().startsWith("card")) {
                contentPane.remove(c);
            }
        }
        int cardWidth = 92;
        int overlap = cardWidth / 2;

        for (int i = 0; i < dHand.size(); i++) {
            Image img = (i == 1 && !showDealer) ? back : dHand.get(i).getImage();
            JLabel card = new JLabel(new ImageIcon(img));
            card.setName("cardD" + i);
            card.setBounds(300 + (i * overlap), 50, cardWidth, 140);
            contentPane.add(card);
            contentPane.setComponentZOrder(card, 0);
        }
        for (int i = 0; i < pHand.size(); i++) {
            JLabel card = new JLabel(new ImageIcon(pHand.get(i).getImage()));
            card.setName("cardP" + i);
            card.setBounds(300 + (i * overlap), 200, cardWidth, 140);
            contentPane.add(card);
            contentPane.setComponentZOrder(card, 0);
        }
        contentPane.repaint();
    }

    /**
     * Registra il controller per gestire i click sui componenti della vista.
     * @param c Il controller del Blackjack.
     */
    public void registerController(BJController c) {
        btnStart.addActionListener(e -> c.handleStart());
        btnHit.addActionListener(e -> c.handleHit());
        btnStay.addActionListener(e -> c.handleStay());
        btnResetBet.addActionListener(e -> c.handleResetBet());
        for (JButton b : betButtons) {
            b.addActionListener(e -> c.handleBet(Integer.parseInt(b.getText().replace("$", ""))));
        }
    }
}
