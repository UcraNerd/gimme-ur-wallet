package gimmeurwallet.controller;

import gimmeurwallet.model.BlackJack;
import gimmeurwallet.model.Card;
import gimmeurwallet.model.Escape;
import gimmeurwallet.view.BJView;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 * Gestisce l'interazione tra l'utente (Vista) e la logica di gioco (Modello).
 * Coordina le puntate, le animazioni di distribuzione e la gestione del wallet.
 */
public class BJController {
    private BlackJack bjModel;
    private BJView bjView;
    private Escape theGame;
    private double currentBet = 0;
    private boolean gameStarted = false;

    /**
     * Crea il controller e aggiorna la visualizzazione del saldo iniziale.
     * @param bjModel Riferimento alla logica del Blackjack.
     * @param theGame Riferimento al sistema economico globale.
     * @param bjView Riferimento all'interfaccia grafica.
     */
    public BJController(BlackJack bjModel, BJView bjView, Escape theGame) {
        this.bjModel = bjModel;
        this.bjView = bjView;
        this.theGame = theGame;
        refreshBalance();
    }

    /** Sincronizza il saldo visualizzato con quello reale nel wallet. */
    private void refreshBalance() {
        try { bjView.updateWallet(theGame.getWallet()); } catch (Exception _) {}
    }

    /**
     * Incrementa la puntata corrente se il gioco non è ancora iniziato.
     * @param amount Valore da aggiungere alla scommessa.
     */
    public void handleBet(int amount) {
        if (!gameStarted) {
            currentBet += amount;
            bjView.updateBet(currentBet);
        }
    }

    /** Azzera la puntata selezionata. */
    public void handleResetBet() {
        if (!gameStarted) {
            currentBet = 0;
            bjView.updateBet(0);
        }
    }

    /**
     * Verifica la disponibilità di fondi e avvia la distribuzione iniziale delle carte.
     */
    public void handleStart() {
        if (currentBet <= 0) return;
        try {
            if (theGame.getWallet() < currentBet) return;
            theGame.removeMoney(currentBet);
            refreshBalance();
        } catch (Exception e) { return; }

        gameStarted = true;
        bjView.setGamePhase(true);
        bjModel.resetGame();
        animateInitialDeal();
    }

    /**
     * Simula la distribuzione sequenziale tipica del casinò tramite Timer.
     */
    private void animateInitialDeal() {
        ArrayList<Card> pHand = bjModel.getPlayerHand();
        ArrayList<Card> dHand = bjModel.getDealerHand();
        ArrayList<Card> pTemp = new ArrayList<>();
        ArrayList<Card> dTemp = new ArrayList<>();

        new Timer(300, e -> {
            pTemp.add(pHand.getFirst());
            bjView.displayCards(pTemp, dTemp, false, bjModel.getCardBack());
            ((Timer)e.getSource()).stop();
        }).start();

        new Timer(600, e -> {
            dTemp.add(dHand.getFirst());
            bjView.displayCards(pTemp, dTemp, false, bjModel.getCardBack());
            bjView.updateScores(bjModel.getHandScore(pTemp), dHand.getFirst().getValue() + " + ?");
            ((Timer)e.getSource()).stop();
        }).start();

        new Timer(900, e -> {
            pTemp.add(pHand.get(1));
            bjView.displayCards(pTemp, dTemp, false, bjModel.getCardBack());
            bjView.updateScores(bjModel.getHandScore(pTemp), dHand.getFirst().getValue() + " + ?");
            ((Timer)e.getSource()).stop();
        }).start();

        new Timer(1200, e -> {
            dTemp.add(dHand.get(1));
            updateUI(false);
            if (bjModel.getHandScore(pHand) == 21) handleStay();
            ((Timer)e.getSource()).stop();
        }).start();
    }

    /** Richiede una carta aggiuntiva per il giocatore. */
    public void handleHit() {
        bjModel.playerHit();
        new Timer(400, e -> {
            updateUI(false);
            if (bjModel.getHandScore(bjModel.getPlayerHand()) >= 21) handleStay();
            ((Timer)e.getSource()).stop();
        }).start();
    }

    /** Conclude il turno del giocatore e avvia quello del banco. */
    public void handleStay() {
        bjModel.dealerTurn();
        new Timer(700, e -> {
            updateUI(true);
            endGame();
            ((Timer)e.getSource()).stop();
        }).start();
    }

    /**
     * Aggiorna la vista con lo stato corrente delle mani e dei punteggi.
     * @param showDealer Se true, mostra la carta coperta del banco.
     */
    private void updateUI(boolean showDealer) {
        bjView.displayCards(bjModel.getPlayerHand(), bjModel.getDealerHand(), showDealer, bjModel.getCardBack());
        int pScore = bjModel.getHandScore(bjModel.getPlayerHand());
        String dScore = showDealer ? String.valueOf(bjModel.getHandScore(bjModel.getDealerHand())) : bjModel.getDealerHand().getFirst().getValue() + " + ?";
        bjView.updateScores(pScore, dScore);
    }

    /**
     * Calcola il pagamento, mostra il risultato e pulisce il tavolo.
     */
    private void endGame() {
        String result = bjModel.getResult();
        try {
            if (result.contains("BLACKJACK!")) theGame.addMoney(currentBet * 2.5);
            else if (result.contains("Hai Vinto!")) theGame.addMoney(currentBet * 2);
            else if (result.contains("Pareggio")) theGame.addMoney(currentBet);
        } catch (Exception _) {}

        bjView.showResultDialog(result, bjModel.getHandScore(bjModel.getPlayerHand()), bjModel.getHandScore(bjModel.getDealerHand()));

        bjView.resetGameUI();
        gameStarted = false;
        bjView.setGamePhase(false);
        refreshBalance();
    }
}
