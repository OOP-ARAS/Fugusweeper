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
        bombImage = Toolkit.getDefaultToolkit().getImage("C:\\Users\\bleid\\Desktop\\Fugusweeper-main\\Fugnus1.png"); // Load the bomb image
        //bombImage = Toolkit.getDefaultToolkit().getImage("Fugnus1.png"); // Load the bomb image
        markImage = Toolkit.getDefaultToolkit().getImage("C:\\Users\\bleid\\Desktop\\Fugusweeper-main\\313.jpg");

        setTitle("Fugusweeper");
        setSize(cols * tileSize + 20, rows * tileSize + 100); //Adds some kad netouchintu borderio + restartui
        setVisible(true);

        revealed = new boolean[rows][cols];
        bombs = new boolean[rows][cols];
        gameOver = false;

        placeBombs();

        /*kaip veikia? nzn nesigilinu borderiu pasauly

        The issue with the restart button taking up the entire program window is due to the way components are added to
         a Frame in AWT. By default, the layout manager for a Frame is BorderLayout, which can lead to components being
         added in an unintended manner if not specified correctly.

        To solve this issue, you can use a Panel with a FlowLayout to hold the button, and then add that panel to the
        Frame. This way, the button won't take up the entire window. Below is the modified code:
        * */

        setLayout(new BorderLayout()); // Use BorderLayout for the Frame

        Button restartButton = new Button("Restart"); // NEW RESTART BUTTON
        restartButton.setPreferredSize(new Dimension(80, 30));
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                revealed = new boolean[rows][cols];
                bombs = new boolean[rows][cols];
                gameOver = false;

                placeBombs();
                repaint();
            }
        });

        Panel buttonPanel = new Panel(); // Create a Panel to hold the button
        buttonPanel.setLayout(new FlowLayout()); // Use FlowLayout for the Panel
        buttonPanel.add(restartButton);
        add(buttonPanel, BorderLayout.SOUTH); // Add the Panel to the Frame

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //leaving debug actions, if you plan to change program size a lot you should make this a math equation
        // i cba so its just hardcoded vale for now


        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent press) {
                if (gameOver) {
                    return; //Does not let player click anymore if game is over
                }
                int row = ((press.getY() -30) / tileSize) ; // remove 30 for windows ui at top

                int col = ((press.getX() -10) / tileSize) ; // remove 10 for side borders
                //System.out.println("Y " + press.getY());
                //System.out.println("X " + press.getX());
                //System.out.println("ROW " + row);
                //System.out.println("COL " + col);
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

        g.setColor(Color.BLACK);
        g.drawRect(10, 30, cols * tileSize, rows * tileSize);

        //fontas neveike nes comic sans >MS<
        Font font = new Font("Comic Sans MS", Font.BOLD, 20); //no FUCKING CLUE KODEL NEVEIKIA, nebent reikia JFrame arba JPanel susikurt??
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int stringWidth = metrics.stringWidth("Fugusweeper");
        g.drawString("Fugusweeper", getWidth() / 2 - stringWidth / 2, 20); //Turetu per viduri borderio rasyti pavadinima


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
                        g.drawString(Integer.toString(neighborBombs), x -5 + tileSize / 2, y +5 + tileSize / 2); // -5+5 kad vidury butu
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