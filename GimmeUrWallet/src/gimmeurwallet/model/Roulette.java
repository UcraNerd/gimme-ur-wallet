package gimmeurwallet.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Rappresenta la logica di gioco della Roulette, gestendo le puntate e il calcolo delle vincite.
 */
public class Roulette {
    private final Map<Integer, Integer> numberBets = new HashMap<>();
    private final Map<String, Integer> externalBets = new HashMap<>();
    private boolean locked = false;

    /**
     * Costruttore della classe Roulette. Inizializza le strutture per le puntate.
     */
    public Roulette() {
    }

    /**
     * Aggiunge o rimuove una puntata su un numero specifico.
     * @param number Il numero su cui puntare.
     * @param chip Il valore della fiche da aggiungere o togliere.
     * @param isRightClick Vero se si desidera rimuovere la puntata, falso per aggiungere.
     */
    public void addNumberBet(int number, int chip, boolean isRightClick) {
        if (locked) return;
        if (!isRightClick) {
            numberBets.put(number, numberBets.getOrDefault(number, 0) + chip);
        } else if (numberBets.containsKey(number)) {
            int val = numberBets.get(number) - chip;
            if (val <= 0) numberBets.remove(number);
            else numberBets.put(number, val);
        }
    }

    /**
     * Aggiunge o rimuove una puntata su una combinazione esterna (es. Rosso, Pari).
     * @param type Il tipo di puntata esterna.
     * @param chip Il valore della fiche.
     * @param isRightClick Vero per rimuovere, falso per aggiungere.
     */
    public void addExternalBet(String type, int chip, boolean isRightClick) {
        if (locked) return;
        if (!isRightClick) {
            externalBets.put(type, externalBets.getOrDefault(type, 0) + chip);
        } else if (externalBets.containsKey(type)) {
            int val = externalBets.get(type) - chip;
            if (val <= 0) externalBets.remove(type);
            else externalBets.put(type, val);
        }
    }

    /**
     * Calcola la vincita totale basata sul numero estratto.
     * @param drawn Il numero uscito sulla ruota.
     * @return Il totale vinto in base alle puntate effettuate.
     */
    public int calculateWin(int drawn) {
        int totalWon = 0;

        if (numberBets.containsKey(drawn)) totalWon += numberBets.get(drawn) * 36;
        if (externalBets.containsKey("RED") && isRed(drawn)) totalWon += externalBets.get("RED") * 2;
        if (externalBets.containsKey("BLACK") && !isRed(drawn) && drawn != 0) totalWon += externalBets.get("BLACK") * 2;
        if (externalBets.containsKey("EVEN") && drawn % 2 == 0 && drawn != 0) totalWon += externalBets.get("EVEN") * 2;
        if (externalBets.containsKey("ODD") && drawn % 2 != 0) totalWon += externalBets.get("ODD") * 2;
        if (externalBets.containsKey("1-18") && drawn >= 1 && drawn <= 18) totalWon += externalBets.get("1-18") * 2;
        if (externalBets.containsKey("19-36") && drawn >= 19 && drawn <= 36) totalWon += externalBets.get("19-36") * 2;

        return totalWon;
    }

    /**
     * Calcola l'importo totale giocato nel turno corrente sommando tutte le puntate.
     * @return Somma totale di tutte le fiches sul tavolo.
     */
    public int getTotalBet() {
        int totalBet = 0;
        for (int value : numberBets.values()) totalBet += value;
        for (int value : externalBets.values()) totalBet += value;
        return totalBet;
    }

    /**
     * Rimuove tutte le puntate attuali dal tavolo.
     */
    public void clearBets() {
        numberBets.clear();
        externalBets.clear();
    }

    /**
     * Verifica se un numero specifico appartiene ai numeri rossi della roulette.
     * @param n Il numero da controllare.
     * @return true se il numero è rosso, false altrimenti.
     */
    public boolean isRed(int n) {
        int[] red = {1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36};
        for (int r : red) if (n == r) return true;
        return false;
    }

    /**
     * Controlla se l'utente ha effettuato almeno una puntata sul tavolo.
     * @return true se ci sono fiches puntate, false altrimenti.
     */
    public boolean hasBets() {
        return !numberBets.isEmpty() || !externalBets.isEmpty();
    }

    /** @return La mappa delle puntate sui numeri singoli. */
    public Map<Integer, Integer> getNumberBets() { return numberBets; }
    /** @return La mappa delle puntate sulle combinazioni esterne. */
    public Map<String, Integer> getExternalBets() { return externalBets; }
    /** @return true se le puntate sono bloccate (ruota in movimento). */
    public boolean isLocked() { return locked; }
    /** @param locked Imposta lo stato di blocco delle puntate. */
    public void setLocked(boolean locked) { this.locked = locked; }
}
