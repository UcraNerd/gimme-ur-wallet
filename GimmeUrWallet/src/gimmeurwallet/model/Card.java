package gimmeurwallet.model;

import java.awt.image.BufferedImage;

/**
 * Rappresenta una carta da gioco fisica in una partita di Blackjack.
 * Ogni carta possiede una rappresentazione grafica e un valore numerico
 * utilizzato per calcolare il punteggio totale della mano.
 */
public class Card {
    private final BufferedImage image;
    private final int value;
    
    /**
     * Costruisce una nuova Carta con l'immagine e il valore blackjack specificati.
     * * @param image Il BufferedImage che mostra il seme e il valore della carta.
     * @param value Il valore intero che la carta apporta alla mano di blackjack.
     */
    public Card(BufferedImage image, int value) {
        this.image = image;
        this.value = value;
    }

    /**
     * Restituisce il valore blackjack della carta.
     * * @return Il valore intero della carta.
     */
    public int getValue() {
        return value;
    }

    /**
     * Restituisce l'immagine associata a questa carta.
     * * @return Il BufferedImage della faccia della carta.
     */
    public BufferedImage getImage() {
        return image;
    }
}