package gimmeurwallet.controller;

import javax.swing.SwingUtilities;
import java.util.concurrent.Semaphore;

/**
 * La classe ReelWatcher è responsabile di gestire la sincronizzazione tra i thread 
 * dei rulli. Monitora lo stato di avanzamento tramite un semaforo e invoca il 
 * metodo di chiusura dello spin nel Controller solo quando tutti i movimenti 
 * sono terminati.
 */
public class ReelWatcher implements Runnable {

    private final Semaphore semaphore;
    private final ReelThread thread1;
    private final ReelThread thread2;
    private final ReelThread thread3;
    private final SMController controller;

    /**
     * Costruttore della classe ReelWatcher.
     * Inizializza i riferimenti necessari per attendere la terminazione dei 
     * tre thread dei rulli e comunicare i risultati al controller.
     * 
     * @param semaphore il semaforo utilizzato per attendere il completamento dei thread
     * @param thread1 il thread associato al primo rullo
     * @param thread2 il thread associato al secondo rullo
     * @param thread3 il thread associato al terzo rullo
     * @param controller il controller che gestisce la logica di gioco finale
     */
    public ReelWatcher(Semaphore semaphore, ReelThread thread1, ReelThread thread2, ReelThread thread3, SMController controller) {
        this.semaphore = semaphore;
        this.thread1 = thread1;
        this.thread2 = thread2;
        this.thread3 = thread3;
        this.controller = controller;
    }

    /**
     * Esegue il monitoraggio dei rulli. Resta in attesa finché il semaforo non 
     * riceve i permessi da tutti i thread (3 rulli). Al termine, recupera i 
     * valori finali e aggiorna l'interfaccia utente tramite il thread Event Dispatch di Swing.
     */
    @Override
    public void run() {
        try {
            semaphore.acquire(3); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        int value1 = thread1.getValue();
        int value2 = thread2.getValue();
        int value3 = thread3.getValue();

        SwingUtilities.invokeLater(() -> controller.finishSpin(value1, value2, value3));
    }
}