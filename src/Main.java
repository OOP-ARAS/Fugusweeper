import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Main extends Frame {

    public static void main(String[] args) {
        new Main();
    }
    int rows = 8; //X   ////Galimai useless, galima daryti kad visada kvadratas bet taip labiau cool
    int cols = 8; //Y
    int tileSize = 50; // Size of each tile itself
    boolean[][] revealed; // Tracks if tile is revealed
    int bombCount = 10;
    boolean[][] bombs; //Locations of bombs
    boolean gameOver;
    Image bombImage; //.png of da boom boom
    Image markImage; //Image for when right-clicked

    public Main() {
        bombImage = Toolkit.getDefaultToolkit().getImage("C:\\Users\\ignas\\Desktop\\Fugnus1.png"); // Load the bomb image
        //bombImage = Toolkit.getDefaultToolkit().getImage("Fugnus1.png"); // Load the bomb image
        markImage = Toolkit.getDefaultToolkit().getImage("C:\\Users\\ignas\\Desktop\\313.jpg");

        setTitle("Fugusweeper");
        setSize(cols * tileSize + 20, rows * tileSize + 100); //Adds some kad netouchintu borderio + restartui
        setVisible(true);

        revealed = new boolean[rows][cols];
        bombs = new boolean[rows][cols];
        gameOver = false;

        placeBombs();

        Button restartButton = new Button("Restart"); //NEW RESTART BUTTON
        restartButton.setBounds(10, rows * tileSize + 50, 80, 30); //padedamas apacioj
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                revealed = new boolean[rows][cols];
                bombs = new boolean[rows][cols];
                gameOver = false;

                placeBombs();
                repaint();
            }
        });
        //add(restartButton); //SWINGAS YRA HORSHESHIT SU POSITIONINGU SETBOUNDS NUSTATYTA BET BUTTON VISTIEK PER VISA EKRANA JEIGU add

        addWindowListener(new WindowAdapter() { //copied
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent press) {
                if (gameOver) {
                    return; //Does not let player click anymore if game is over
                }
                int row = press.getY() / tileSize;
                int col = press.getX() / tileSize;
                if (row >= 0 && row < rows && col >= 0 && col < cols) {
                    if (press.getButton() == MouseEvent.BUTTON1) {
                    if (bombs[row][col]) {
                        gameOver = true;
                        for (int temprow = 0; temprow < rows; temprow++) {
                            for (int tempcol = 0; tempcol < cols; tempcol++) {
                                if (bombs[temprow][tempcol]) { //Reveals all bombs. Kind of sviestas sviestuotas situation but whatever
                                    revealed[temprow][tempcol] = true;
                                }
                            }
                        }
                    } else {
                        revealed[row][col] = true;
                    }
                    repaint(); //Kviecia paint su braindead awt logika kur g yra simboliskai revealed masyvas
                }
            } else if(press.getButton() == MouseEvent.BUTTON3) { //Desinys mouse
                    //TODO
                    repaint();
                }
        }
        });
    }

    private void placeBombs() {
        Random rand = new Random();
        int placedBombs = 0;

        while (placedBombs < bombCount) {
            int x = rand.nextInt(rows);
            int y = rand.nextInt(cols);

            if (!bombs[x][y]) { // Ensure no duplicates
                bombs[x][y] = true;
                placedBombs++;
            }
        }
    }

    @Override
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
                    int neighborBombs = countNearBombs(row, col);
                    g.setColor(Color.BLUE);                 //TILE
                    g.fillRect(x, y, tileSize, tileSize);
                    g.setColor(Color.BLACK);                //BORDERIS
                    g.drawRect(x, y, tileSize, tileSize);
                    if (bombs[row][col]) { //If tile has bomb show it
                        g.drawImage(bombImage, x, y, tileSize, tileSize, this);
                    } else if (neighborBombs > 0) {
                        g.drawString(Integer.toString(neighborBombs), x + tileSize / 2, y + tileSize / 2);
                    }
                } else {                                    //JEI DAR NIEKO NERA, UZPILDO GRAY (pradzioje)
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(x, y, tileSize, tileSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, tileSize, tileSize);
                    //g.drawImage(bombImage, x, y, tileSize, tileSize, this); //TESTAVIMUI AR IS VIS KAZKA DRAWINA IS VIS
                }
            }
        }
    }

private int countNearBombs(int row, int col) {
    int count = 0;
    for (int dr = -1; dr <= 1; dr++) {
        for (int dc = -1; dc <= 1; dc++) {
            int r = row + dr;
            int c = col + dc;
            if (r >= 0 && r < rows && c >= 0 && c < cols && bombs[r][c]) {
                count++;
            }
        }
    }
    return count;
}
}