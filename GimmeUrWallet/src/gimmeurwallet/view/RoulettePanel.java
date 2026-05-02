package gimmeurwallet.view;

import gimmeurwallet.controller.RTController;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

/**
 * Pannello dedicato al disegno grafico della ruota e all'animazione della pallina.
 */
public class RoulettePanel extends JPanel implements Runnable {
    private RTController controller;
    private final JLabel walletLabel;
    private final int[] numbers = {
            0,32,15,19,4,21,2,25,17,34,6,27,13,36,11,30,8,23,10,
            5,24,16,33,1,20,14,31,9,22,18,29,7,28,12,35,3,26
    };
    private double wheelAngle = 0, wheelSpeed = 0, ballAngle = 0, ballSpeed = 0;
    private boolean spinning = false;
    private int resultNumber = -1;
    private final Random rand = new Random();

    /**
     * Crea il pannello della ruota con l'interfaccia per il portafoglio e il pulsante di avvio.
     */
    public RoulettePanel() {
        setPreferredSize(new Dimension(900, 500));
        setBackground(new Color(10, 100, 10));
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);

        walletLabel = new JLabel("Caricamento portafoglio...");
        walletLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        walletLabel.setForeground(Color.WHITE);

        topPanel.add(walletLabel);
        add(topPanel, BorderLayout.NORTH);

        JButton spinBtn = new JButton("GIRA");
        spinBtn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        spinBtn.addActionListener(e -> {
            if (controller != null) controller.onSpinRequested();
        });
        add(spinBtn, BorderLayout.SOUTH);
    }

    /**
     * Aggiorna l'interfaccia con l'importo corrente del portafoglio.
     * @param currentMoney Saldo da mostrare.
     */
    public void updateWalletLabel(double currentMoney) {
        walletLabel.setText("Portafoglio: " + currentMoney + "€");
    }

    /**
     * Registra il controller per questo pannello.
     * @param c Il controller.
     */
    public void registerController(RTController c) {
        this.controller = c;
    }

    /**
     * Inizializza le velocità e avvia il thread dell'animazione fisica.
     */
    public void startVisualSpin() {
        if (spinning) return;
        spinning = true;
        resultNumber = -1;
        wheelSpeed = 8 + rand.nextDouble() * 4;
        ballSpeed = -20 - rand.nextDouble() * 6;
        new Thread(this).start();
    }

    /**
     * Resetta lo stato del pannello dopo la fine di una giocata.
     */
    public void resetSpinState() {
        spinning = false;
        resultNumber = -1;
        repaint();
    }

    /**
     * Loop dell'animazione: aggiorna gli angoli basandosi sulla decelerazione.
     */
    @Override
    public void run() {
        while (spinning) {
            wheelAngle += wheelSpeed;
            ballAngle += ballSpeed;
            wheelSpeed *= 0.992;
            ballSpeed *= 0.985;

            if (Math.abs(ballSpeed) < 0.15) {
                spinning = false;
                calculateResultFromBall();
                SwingUtilities.invokeLater(() -> {
                    if (controller != null) controller.onSpinFinished(resultNumber);
                });
            }
            repaint();
            try { Thread.sleep(16); } catch (Exception ignored) {}
        }
    }

    /**
     * Determina il numero estratto basandosi sulla posizione relativa della pallina sulla ruota.
     */
    private void calculateResultFromBall() {
        double slice = 360.0 / numbers.length;
        double relative = (ballAngle - wheelAngle) % 360;
        if (relative < 0) relative += 360;
        int index = (int)((relative + slice / 2) / slice) % numbers.length;
        resultNumber = numbers[index];
    }

    /**
     * Rendering grafico della ruota, dei numeri e della pallina.
     * @param g Oggetto Graphics.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2, cy = getHeight() / 2, r = 220;
        double slice = 360.0 / numbers.length;

        AffineTransform old = g2.getTransform();
        g2.rotate(Math.toRadians(wheelAngle), cx, cy);

        for (int i = 0; i < numbers.length; i++) {
            double angleDeg = i * slice;
            int startAngle = (int)(-(angleDeg + slice / 2));
            g2.setColor(getColor(numbers[i]));
            g2.fillArc(cx - r, cy - r, r * 2, r * 2, startAngle, (int)Math.ceil(slice));
            g2.setColor(Color.WHITE);
            g2.drawArc(cx - r, cy - r, r * 2, r * 2, startAngle, (int)Math.ceil(slice));
        }

        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        for (int i = 0; i < numbers.length; i++) {
            double angle = Math.toRadians(i * slice);
            int tx = (int)(cx + Math.cos(angle) * (r - 35));
            int ty = (int)(cy + Math.sin(angle) * (r - 35));
            String s = String.valueOf(numbers[i]);
            AffineTransform oldText = g2.getTransform();
            g2.translate(tx, ty);
            g2.rotate(angle + Math.PI / 2);
            g2.setColor(Color.WHITE);
            g2.drawString(s, -g2.getFontMetrics().stringWidth(s)/2, g2.getFontMetrics().getAscent()/2);
            g2.setTransform(oldText);
        }

        g2.setTransform(old);
        g2.setColor(Color.DARK_GRAY);
        g2.fillOval(cx - 30, cy - 30, 60, 60);
        g2.setColor(Color.YELLOW);
        g2.fillPolygon(new int[]{cx-10, cx+10, cx}, new int[]{cy-r-15, cy-r-15, cy-r}, 3);

        double rad = Math.toRadians(ballAngle);
        g2.setColor(Color.WHITE);
        g2.fillOval((int)(cx + Math.cos(rad) * (r - 15)) - 6, (int)(cy + Math.sin(rad) * (r - 15)) - 6, 12, 12);
    }

    /**
     * Restituisce il colore corretto (Verde, Rosso o Nero) per un dato numero.
     * @param n Il numero.
     * @return Il colore associato.
     */
    private Color getColor(int n) {
        if (n == 0) return new Color(0, 150, 0);
        return (controller != null && controller.getModel().isRed(n)) ? new Color(180, 0, 0) : new Color(20,20,20);
    }
}
