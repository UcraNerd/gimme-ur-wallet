package gimmeurwallet.model;

import java.util.concurrent.Semaphore;

/**
 * La classe Escape gestisce una risorsa wallet condivisa utilizzando un pattern 
 * di sincronizzazione Lettori-Scrittori con i Semafori.
 * Consente a più thread di leggere il saldo del wallet simultaneamente, 
 * ma garantisce l'accesso esclusivo ai thread che modificano il saldo.
 */
public class Escape {
    private final double moneyNeeded = 300000;
    private double wallet;
    
    private int readersCount = 0;
    
    /**
     * Semaforo per proteggere la variabile condivisa readersCount (Mutex).
     */
    private final Semaphore mutex = new Semaphore(1);
    
    /**
     * Semaforo per gestire l'accesso esclusivo per gli scrittori (Wrt).
     */
    private final Semaphore wrt = new Semaphore(1);

    /**
     * Costruisce una nuova istanza di Escape con un saldo iniziale del wallet.
     */
    public Escape() {
        this.wallet = 3000;
    }

    /**
     * Recupera il saldo attuale del wallet.
     * Implementa la logica del lettore: più lettori possono accedere contemporaneamente.
     * * @return L'importo attuale di denaro nel wallet.
     * @throws InterruptedException se il thread viene interrotto durante l'attesa.
     */
    public double getWallet() throws InterruptedException {
        mutex.acquire();
        readersCount++;
        if (readersCount == 1) {
            wrt.acquire(); 
        }
        mutex.release();

        double value = wallet; 

        mutex.acquire();
        readersCount--;
        if (readersCount == 0) {
            wrt.release(); 
        }
        mutex.release();
        
        return value;
    }

    /**
     * Aumenta il saldo del wallet di un importo specificato.
     * Implementa la logica dello scrittore: richiede l'accesso esclusivo.
     * * @param amount L'importo di denaro da aggiungere.
     * @throws InterruptedException se il thread viene interrotto durante l'attesa.
     */
    public void addMoney(double amount) throws InterruptedException {
        wrt.acquire(); 
        this.wallet += amount;
        wrt.release(); 
    }

    /**
     * Diminuisce il saldo del wallet di un importo specificato.
     * Implementa la logica dello scrittore: richiede l'accesso esclusivo.
     * * @param amount L'importo di denaro da rimuovere.
     * @throws InterruptedException se il thread viene interrotto durante l'attesa.
     */
    public void removeMoney(double amount) throws InterruptedException {
        wrt.acquire();
        this.wallet -= amount;
        wrt.release();
    }

    /**
     * Verifica se il saldo del wallet ha raggiunto l'obiettivo richiesto.
     * Implementa la logica del lettore per consentire controlli simultanei.
     * * @return true se il saldo è maggiore o uguale a moneyNeeded, false altrimenti.
     * @throws InterruptedException se il thread viene interrotto durante l'attesa.
     */
    public boolean isGoalReached() throws InterruptedException {
        mutex.acquire();
        readersCount++;
        if (readersCount == 1) {
            wrt.acquire();
        }
        mutex.release();

        boolean reached = this.wallet >= moneyNeeded;

        mutex.acquire();
        readersCount--;
        if (readersCount == 0) {
            wrt.release();
        }
        mutex.release();
        
        return reached;
    }
}
