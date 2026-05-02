package gimmeurwallet.controller;

import gimmeurwallet.model.Escape;
import gimmeurwallet.view.View;

/**
 * Il punto di ingresso dell'applicazione GimmeUrWallet.
 * Questa classe segue il pattern architetturale MVC (Model-View-Controller)
 * inizializzando la Vista, il Modello (Escape) e il Controller, stabilendo
 * poi le connessioni necessarie tra di essi.
 */
public class Main {

    /**
     * Metodo principale che avvia l'applicazione.
     * Esegue la seguente sequenza di configurazione:
     * 1. Crea l'interfaccia grafica utente (View).
     * 2. Inizializza la logica di gioco e la risorsa condivisa (Model).
     * 3. Crea il Controller per fare da ponte tra Modello e Vista.
     * 4. Registra il Controller all'interno della Vista per gestire gli eventi utente.
     * 5. Rende visibile la finestra dell'applicazione.
     * * @param args Argomenti della riga di comando (non utilizzati).
     */
    static void main() {
        View mainView = new View();
        Escape theGame = new Escape();
        Controller controller = new Controller(theGame);
        mainView.registerController(controller);
        mainView.setVisible(true);
    }
}
