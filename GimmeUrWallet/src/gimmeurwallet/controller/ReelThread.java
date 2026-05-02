package gimmeurwallet.controller;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * La classe ReelThread rappresenta un singolo rullo nella slot machine. 
 * Ogni rullo ruota per un numero predefinito di iterazioni, cambiando l'immagine 
 * visualizzata sulla JLabel in modo casuale. Al termine del ciclo, segnala il 
 * completamento tramite un semaforo per permettere la sincronizzazione globale.
 */
public class ReelThread implements Runnable {

    private static final int SPIN_ITERATIONS = 20;
    private static final int SPIN_DELAY_MS = 100;

    private final JLabel label;
    private final ImageIcon[] symbols;
    private final Semaphore semaphore;
    private final Random random;
    private int value;

    /**
     * Costruttore della classe ReelThread.
     * Configura il rullo con i componenti grafici necessari, il set di simboli
     * disponibili e il meccanismo di sincronizzazione.
     *
     * @param label     la JLabel utilizzata per visualizzare graficamente il simbolo
     * @param symbols   l'array di ImageIcon contenente i possibili simboli del rullo
     * @param semaphore il semaforo per segnalare la fine della rotazione
     */
    public ReelThread(JLabel label, ImageIcon[] symbols, Semaphore semaphore) {
        this.label = label;
        this.symbols = symbols;
        this.semaphore = semaphore;
        this.random = new Random();
    }

    /**
     * Esegue l'animazione di rotazione del rullo.
     * Il ciclo seleziona casualmente un simbolo per ogni iterazione e aggiorna 
     * la visualizzazione. Al termine, memorizza l'indice finale e rilascia 
     * un permesso sul semaforo.
     */
    @Override
    public void run() {
        try {
            int lastIndex = 0;

            for (int i = 0; i < SPIN_ITERATIONS; i++) {
                lastIndex = random.nextInt(symbols.length);
                
                final int currentIndex = lastIndex;
                SwingUtilities.invokeLater(() -> {
                    label.setIcon(symbols[currentIndex]);
                    label.repaint();
                });

                Thread.sleep(SPIN_DELAY_MS);
            }

            this.value = lastIndex;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    /**
     * Restituisce l'indice del simbolo visualizzato al termine della rotazione.
     *
     * @return l'indice del simbolo finale
     */
    public int getValue() {
        return value;
    }
}