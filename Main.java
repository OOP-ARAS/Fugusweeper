import java.awt.*;
import java.awt.event.*;

public class Main extends Frame {

    public static void main(String[] args) {
        new Main();
    }
    int rows = 8; //X   ////Galimai useless, galima daryti kad visada kvadratas bet taip labiau cool
    int cols = 8; //Y
    int tileSize = 50; // Size of each tile itself
    boolean[][] revealed; // Tracks if tile is revealed

    public Main() {
        setTitle("Fugusweeper");
        setSize(cols * tileSize + 20, rows * tileSize + 40); //Adds some kad netouchintu borderio
        setVisible(true);

        revealed = new boolean[rows][cols];

        addWindowListener(new WindowAdapter() { //copied
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent press) {
                int row = press.getY() / tileSize;
                int col = press.getX() / tileSize;
                if (row >= 0 && row < rows && col >= 0 && col < cols) {
                    revealed[row][col] = true;
                    repaint(); //Kviecia paint su braindead awt logika kur g yra simboliskai revealed masyvas
                }
            }
        });
    }

    public void paint(Graphics g) {
    /*
        g.setColor(Color.BLACK);
        g.drawRect(10, 30, cols * tileSize, rows * tileSize);

        Font font = new Font("Comic Sans", Font.BOLD, 20); //no FUCKING CLUE KODEL NEVEIKIA, nebent reikia JFrame arba JPanel susikurt??
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int stringWidth = metrics.stringWidth("Fugusweeper");
        g.drawString("Fugusweeper", getWidth() / 2 - stringWidth / 2, 20); //Turetu per viduri borderio rasyti pavadinima
      */
        //MAIN UZPILDYMAS
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * tileSize + 10;
                int y = row * tileSize + 30;

                if (revealed[row][col]) {
                    g.setColor(Color.PINK);                 //TILE
                    g.fillRect(x, y, tileSize, tileSize);
                    g.setColor(Color.BLACK);                //BORDERIS
                    g.drawRect(x, y, tileSize, tileSize);
                } else {                                    //JEI DAR NIEKO NERA, UZPILDO GRAY (pradzioje)
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(x, y, tileSize, tileSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, tileSize, tileSize);
                }
            }
        }
    }
}
