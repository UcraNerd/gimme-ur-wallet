package gimmeurwallet.view;

import gimmeurwallet.controller.RTController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * Pannello del tappeto verde dove l'utente può interagire per piazzare le scommesse.
 */
public class BettingTablePanel extends JPanel {
    private RTController controller;
    private final int[] numbers = {
            3,6,9,12,15,18,21,24,27,30,33,36,
            2,5,8,11,14,17,20,23,26,29,32,35,
            1,4,7,10,13,16,19,22,25,28,31,34
    };

    /**
     * Costruttore del pannello. Registra i listener del mouse per identificare le zone cliccate.
     */
    public BettingTablePanel() {
        setPreferredSize(new Dimension(900, 300));
        setBackground(new Color(5, 60, 5));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (controller == null || controller.getModel().isLocked()) return;

                int x = e.getX(), y = e.getY();
                boolean isRight = SwingUtilities.isRightMouseButton(e);
                int w = getWidth() / 13, h = 200 / 3;

                if (y < 200) {
                    int col = x / w, row = y / h;
                    int number;
                    if (col == 0) number = 0;
                    else if (col <= 12) number = numbers[row * 12 + (col - 1)];
                    else return;
                    controller.onNumberBet(number, isRight);
                } else {
                    int section = x / (getWidth() / 6);
                    String bet = switch (section) {
                        case 0 -> "1-18";
                        case 1 -> "EVEN";
                        case 2 -> "RED";
                        case 3 -> "BLACK";
                        case 4 -> "ODD";
                        case 5 -> "19-36";
                        default -> null;
                    };
                    if (bet != null) controller.onExternalBet(bet, isRight);
                }
            }
        });
    }

    /**
     * Registra il controller per questo pannello.
     * @param c Il controller.
     */
    public void registerController(RTController c) {
        this.controller = c;
    }

    /**
     * Disegna il tappeto con i numeri e le zone per le puntate esterne.
     * Mostra anche le fiches piazzate.
     * @param g Oggetto Graphics.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (controller == null) return;

        Map<Integer, Integer> numBets = controller.getModel().getNumberBets();
        Map<String, Integer> extBets = controller.getModel().getExternalBets();

        int w = getWidth() / 13, h = 200 / 3;

        g.setColor(new Color(0,100,0)); g.fillRect(0,0,w,200);
        g.setColor(Color.WHITE); g.drawRect(0,0,w,200); g.drawString("0", w/2,100);
        drawChip(g, numBets.get(0), w/2,100);

        for(int i=0;i<36;i++){
            int r = i/12, c = (i%12)+1, n = numbers[i];
            int x = c*w, y = r*h;
            g.setColor(controller.getModel().isRed(n) ? Color.RED : Color.BLACK);
            g.fillRect(x,y,w,h);
            g.setColor(Color.WHITE); g.drawRect(x,y,w,h); g.drawString(""+n,x+w/2,y+h/2);
            drawChip(g, numBets.get(n), x+w/2,y+h/2);
        }

        String[] bets = {"1-18","PARI","ROSSO","NERO","DISPARI","19-36"};
        String[] betKeys = {"1-18","EVEN","RED","BLACK","ODD","19-36"};
        for(int i=0;i<6;i++){
            int x = i*(getWidth()/6), y = 210;
            g.setColor(Color.GRAY); g.fillRect(x,y,getWidth()/6,80);
            g.setColor(Color.WHITE); g.drawRect(x,y,getWidth()/6,80); g.drawString(bets[i], x+30, y+40);
            drawChip(g, extBets.get(betKeys[i]), x+50,y+40);
        }
    }

    /**
     * Disegna una fiche dorata se il valore della puntata è maggiore di zero.
     * @param g Oggetto Graphics.
     * @param val Valore totale delle puntate in quella zona.
     * @param x Coordinata X centrale della zona.
     * @param y Coordinata Y centrale della zona.
     */
    private void drawChip(Graphics g, Integer val, int x, int y){
        if(val==null) return;
        g.setColor(new Color(255,215,0,200)); g.fillOval(x-15,y-15,30,30);
        g.setColor(Color.BLACK); g.drawOval(x-15,y-15,30,30);
        g.drawString(""+val,x-10,y+5);
    }
}