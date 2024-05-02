import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Main extends Frame {

    public static void main(String[] args) {
        new Main();
    }
    int rows = 8; // X 
    int cols = 8; // Y
    int tileSize = 50; // Size of each tile itself
    boolean[][] revealed; // Tracks if tile is revealed
    int bombCount = 10;
    boolean[][] bombs; // Locations of bombs
    boolean gameOver;
    Image bombImage; //.png of da boom boom

    public Main() {
        bombImage = Toolkit.getDefaultToolkit().getImage("C:\\PATH\\TO\\REPO\\Fugnus1.png"); // Load the bomb image, for now this has to be an ABSOLUTE path, will be fixed next stage
        // bombImage = Toolkit.getDefaultToolkit().getImage("Fugnus1.png"); // Load the bomb image

        setTitle("Fugusweeper");
        setSize(cols * tileSize + 20, rows * tileSize + 40); // Also makes it slightly further away from windows border
        setVisible(true);

        revealed = new boolean[rows][cols];
        bombs = new boolean[rows][cols];
        gameOver = false;

        Random rand = new Random();
        int placedBombs = 0;

        while (placedBombs < bombCount) {
            int x = rand.nextInt(rows);
            int y = rand.nextInt(cols);

            if (!bombs[x][y]) { // So that there are no duplicates
                bombs[x][y] = true;
                placedBombs++;
            }
        }

        addWindowListener(new WindowAdapter() { // Copied
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent press) {
                if (gameOver) {
                    return; // Does not let player click anymore if game is over
                }
                int row = press.getY() / tileSize;
                int col = press.getX() / tileSize;
                if (row >= 0 && row < rows && col >= 0 && col < cols) {
                    if (bombs[row][col]) {
                        gameOver = true;
                        for (int temprow = 0; temprow < rows; temprow++) {
                            for (int tempcol = 0; tempcol < cols; tempcol++) {
                                if (bombs[temprow][tempcol]) { // Reveals all bombs
                                    revealed[temprow][tempcol] = true;
                                }
                            }
                        }
                    }
                    else {
                        revealed[row][col] = true;
                    }
                    repaint(); // Calls paint
                }
            }
        });
    }

    @Override
    public void paint(Graphics g) {
/*
        g.setColor(Color.BLACK);
        g.drawRect(10, 30, cols * tileSize, rows * tileSize);

        Font font = new Font("Comic Sans", Font.BOLD, 20); // Unfortunately, numbers don't work yet :(
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int stringWidth = metrics.stringWidth("Fugusweeper");
        g.drawString("Fugusweeper", getWidth() / 2 - stringWidth / 2, 20); // This should write the title in the middle
*/
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * tileSize + 10;
                int y = row * tileSize + 30;

                if (revealed[row][col]) {
                    g.setColor(Color.BLUE);                 // Tile
                    g.fillRect(x, y, tileSize, tileSize);
                    g.setColor(Color.BLACK);                // Border
                    g.drawRect(x, y, tileSize, tileSize);
                    if (bombs[row][col]) { // If tile has bomb show it
                        g.drawImage(bombImage, x, y, tileSize, tileSize, this);
                    }
                } else {                                    // Fill with gray if not revealed yet
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(x, y, tileSize, tileSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, tileSize, tileSize);
                    // g.drawImage(bombImage, x, y, tileSize, tileSize, this); // Test statement
                }
            }
        }
    }
}
