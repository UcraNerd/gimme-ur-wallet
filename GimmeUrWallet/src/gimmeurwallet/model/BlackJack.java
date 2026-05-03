package gimmeurwallet.model;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;

/**
 * Rappresenta la logica di business e lo stato del gioco Blackjack.
 * Gestisce il mazzo, le mani del giocatore e del banco, e il calcolo dei punteggi.
 */
public class BlackJack {
    private final int SCALE = 4;
    private final int CARD_WIDTH = 23;
    private final int CARD_HEIGHT = 35;
    private final int COLS = 13;
    private final int ROWS = 4;
    private final int TMIN = 17;
    private final int BLACKJACK = 21;

    private Card[][] cards;
    private ArrayList<Card> deck;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;
    private BufferedImage spriteSheet;
    private BufferedImage backSrc;
    private BufferedImage cardBack;

    /**
     * Inizializza il modello caricando le risorse grafiche e preparando il primo mazzo.
     */
    public BlackJack() {
        loadCards();
        resetGame();
    }

    /**
     * Carica lo sprite sheet delle carte e ritaglia ogni singola carta.
     * Calcola il valore blackjack per ogni carta e prepara l'immagine del dorso.
     */
    private void loadCards() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("/imgs/ClassicCards.png"));
            backSrc = ImageIO.read(getClass().getResourceAsStream("/imgs/LightClassic.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cardBack = new BufferedImage(CARD_WIDTH * SCALE, CARD_HEIGHT * SCALE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g1 = cardBack.createGraphics();
        g1.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g1.drawImage(backSrc, 0, 0, CARD_WIDTH * SCALE, CARD_HEIGHT * SCALE, null);
        g1.dispose();

        cards = new Card[ROWS][COLS];
        deck = new ArrayList<>();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = c * (CARD_WIDTH + 1);
                int y = r * (CARD_HEIGHT + 1);

                BufferedImage subImage = spriteSheet.getSubimage(x, y, CARD_WIDTH, CARD_HEIGHT);
                BufferedImage scaledImg = new BufferedImage(CARD_WIDTH * SCALE, CARD_HEIGHT * SCALE, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = scaledImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g2.drawImage(subImage, 0, 0, CARD_WIDTH * SCALE, CARD_HEIGHT * SCALE, null);
                g2.dispose();

                int value = (c == 0) ? 11 : (c >= 9) ? 10 : c + 1;
                Card card = new Card(scaledImg, value);
                cards[r][c] = card;
                deck.add(card);
            }
        }
    }

    /**
     * Ripristina il mazzo completo, lo mescola e distribuisce le 4 carte iniziali.
     */
    public void resetGame() {
        deck.clear();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                deck.add(cards[r][c]);
            }
        }
        Collections.shuffle(deck);

        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();

        playerHand.add(deck.removeFirst());
        dealerHand.add(deck.removeFirst());
        playerHand.add(deck.removeFirst());
        dealerHand.add(deck.removeFirst());
    }

    /**
     * Aggiunge una carta alla mano del giocatore pescata dal mazzo.
     */
    public void playerHit() {
        if (getHandScore(playerHand) < BLACKJACK) {
            playerHand.add(deck.removeFirst());
        }
    }

    /**
     * Gestisce l'intelligenza artificiale del banco pescando carte finché il punteggio è < 17.
     */
    public void dealerTurn() {
        while (getHandScore(dealerHand) < TMIN) {
            dealerHand.add(deck.removeFirst());
        }
    }

    /**
     * Calcola il punteggio totale di una mano gestendo il valore variabile dell'Asso (1 o 11).
     * @param hand La lista di carte da analizzare.
     * @return Il punteggio ottimizzato.
     */
    public int getHandScore(ArrayList<Card> hand) {
        int score = 0;
        int aces = 0;
        for (Card c : hand) {
            score += c.getValue();
            if (c.getValue() == 11) aces++;
        }
        while (score > BLACKJACK && aces > 0) {
            score -= 10;
            aces--;
        }
        return score;
    }

    /**
     * Verifica se la mano è un Blackjack naturale (21 con sole due carte).
     * @param hand La mano da controllare.
     * @return true se è Blackjack.
     */
    public boolean isNaturalBlackjack(ArrayList<Card> hand) {
        return hand.size() == 2 && getHandScore(hand) == BLACKJACK;
    }

    /**
     * Confronta i punteggi e restituisce l'esito della partita in formato testuale.
     * @return Stringa descrittiva del risultato.
     */
    public String getResult() {
        int pScore = getHandScore(playerHand);
        int dScore = getHandScore(dealerHand);

        boolean pBJ = isNaturalBlackjack(playerHand);
        boolean dBJ = isNaturalBlackjack(dealerHand);

        if (pBJ && dBJ) return "Pareggio! Entrambi avete Blackjack.";
        if (pBJ) return "BLACKJACK! Hai Vinto!";
        if (dBJ) return "Il Banco ha Blackjack! Hai Perso.";

        if (pScore > BLACKJACK) return "Sballato! Il Banco Vince.";
        if (dScore > BLACKJACK) return "Il Banco ha Sballato! Hai Vinto!";
        if (pScore > dScore) return "Hai Vinto!";
        if (pScore < dScore) return "Il Banco Vince!";
        return "Pareggio!";
    }

    /** @return La mano corrente del giocatore. */
    public ArrayList<Card> getPlayerHand() { return playerHand; }
    /** @return La mano corrente del banco. */
    public ArrayList<Card> getDealerHand() { return dealerHand; }
    /** @return L'immagine del dorso della carta. */
    public BufferedImage getCardBack() { return cardBack; }
    /** @return Il mazzo di carte rimanenti. */
    public ArrayList<Card> getDeck() { return deck; }
}
