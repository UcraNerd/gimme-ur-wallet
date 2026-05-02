package gimmeurwallet.controller;

import gimmeurwallet.model.*;
import gimmeurwallet.view.BJView;
import gimmeurwallet.view.SMView;
import gimmeurwallet.view.RTView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;

/**
 * Controller principale che gestisce l'interazione tra la vista e il modello.
 * Si occupa di monitorare gli eventi del mouse per l'apertura delle diverse 
 * sessioni di gioco (Blackjack, Roulette, Slot Machine) e di limitarne il numero massimo.
 */
public class Controller implements MouseListener {
    private int bjCount;
    private int smCount;
    private boolean rtDrawn;
    private final Escape theGame;
    
    /**
     * Inizializza il controller con lo stato dei giochi azzerato e il modello principale.
     * * @param theGame L'istanza del modello Escape (risorsa wallet condivisa).
     */
    public Controller(Escape theGame) {
        this.bjCount = 0;
        this.smCount = 0;
        this.rtDrawn = false;
        this.theGame = theGame;
    }
    
    /**
     * Metodo per la gestione del click del mouse (non utilizzato).
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Metodo per la gestione dell'entrata del cursore (non utilizzato).
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Metodo per la gestione dell'uscita del cursore (non utilizzato).
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Rileva la pressione del mouse sui componenti della vista per avviare il gioco selezionato.
     * Controlla se è stato raggiunto il numero massimo di finestre per ogni tipologia.
     * * @param e L'evento del mouse generato dalla sorgente.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        String game = ((JLabel) e.getSource()).getText();
        
        if (game.equals("bj1") || game.equals("bj2")) {
            int maxBJs = 2;
            if (bjCount == maxBJs) {
                System.out.println("Hai raggiunto il numero massimo di tavoli da blackjack!");
            } else {
                drawBlackJack();
                bjCount++;
            }
        }
        
        if (game.equals("rt")) {
            if (rtDrawn) {
                System.out.println("La roulette è già stata avviata!");
            } else {
                drawRoulette();
                rtDrawn = true;
            }
        }    
        
        if (game.equals("sm1") || game.equals("sm2")) {
            int maxSMs = 5;
            if (smCount == maxSMs) {
                System.out.println("Hai raggiunto il numero massimo di slot machine!");
            } else {
                drawSlotMachine();
                smCount++;
            }
        }
    }

    /**
     * Metodo per la gestione del rilascio del mouse (non utilizzato).
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Crea e visualizza una nuova sessione di Blackjack.
     * Inizializza il relativo sistema MVC e riduce il contatore alla chiusura della finestra.
     */
    private void drawBlackJack() {
        BlackJack bjModel = new BlackJack();
        BJView bjView = new BJView();
        BJController bjController = new BJController(bjModel, bjView, theGame);
        bjView.registerController(bjController);
        bjView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Il tavolo di blackjack è stato chiuso...");
                bjCount--;
            }
        });
        bjView.setVisible(true);
    }
    
    /**
     * Crea e visualizza una nuova sessione di Roulette.
     * Inizializza il relativo sistema MVC e resetta lo stato alla chiusura della finestra.
     */
    private void drawRoulette() {
        Roulette rtModel = new Roulette();
        RTView rtView = new RTView();
        RTController rtController = new RTController(rtModel, rtView, theGame);
        rtView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("La roulette è terminata...");
                rtDrawn = false;
            }
        });
        rtView.setVisible(true);
    }
    
    /**
     * Crea e visualizza una nuova sessione di Slot Machine.
     * Inizializza il relativo sistema MVC e riduce il contatore alla chiusura della finestra.
     */
    private void drawSlotMachine() {
        SMView smView = new SMView();
        SMController rtController = new SMController(theGame, smView);
        smView.registerController(rtController);
        smView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("La slot machine è terminata...");
                smCount--;
            }
        });
        smView.setVisible(true);
    }
}
