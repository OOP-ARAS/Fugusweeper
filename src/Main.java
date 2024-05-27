import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/* Missing:
 * 	Restart
 * 	Images
 * 	Game state handling
 * Needs fix:
 * 	Fugu num indicator not set properly
 */

class CellsPanel extends JPanel implements MouseListener {
    private GameSession gameSession;

    private int marginX;
    private int marginY;
    private int numCells;
    private int squareSize;
    private int startX;
    private int startY;

    private int[][] cellStatus;
    private int[][] cellNearby;

    public CellsPanel(GameSession gameSession) {
	this.gameSession = gameSession;
        this.numCells = gameSession.getCells();
	this.cellStatus = new int[numCells][numCells];
	this.cellNearby = new int[numCells][numCells];

        addMouseListener(this);
    }

    public void setMargin(int marginX, int marginY) {
        this.marginX = marginX;
        this.marginY = marginY;
        calculateGrid();
    }

    private void calculateGrid() {
        int width = getWidth();
        int height = getHeight();
        int drawableWidth = width - 2 * marginX;
        int drawableHeight = height - 2 * marginY;

        squareSize = Math.min(drawableWidth, drawableHeight) / numCells;

        startX = marginX + (drawableWidth - squareSize * numCells) / 2;
        startY = marginY + (drawableHeight - squareSize * numCells) / 2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        calculateGrid();

        Font font = new Font("Comic Sans MS", Font.BOLD, 20);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);

        // Header, doesn't respect resizability
        int stringWidth = metrics.stringWidth("Fugusweeper");
        g.drawString("Fugusweeper", getWidth() / 2 - stringWidth / 2, 20);

        for (int i = 0; i < numCells; i++) {
            for (int j = 0; j < numCells; j++) {
                int x = startX + i * squareSize;
                int y = startY + j * squareSize;
		if (cellStatus[i][j] == 0) {
		    g.setColor(Color.DARK_GRAY);
		}
		else if (cellStatus[i][j] == 1) {
		    if (cellNearby[i][j] > 0) {
			g.setColor(Color.BLACK);
                        g.drawString(Integer.toString(cellNearby[i][j]), x, y);
		    }
		    g.setColor(Color.BLUE);
		}
		g.fillRect(x, y, squareSize, squareSize);
	        g.setColor(Color.BLACK);
                g.drawRect(x, y, squareSize, squareSize);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int relativeX = (e.getX() - startX) / squareSize;
        int relativeY = (e.getY() - startY) / squareSize;

	if ((relativeX >= 0 && relativeX < numCells) && (relativeY >= 0 && relativeY < numCells)) {
	    System.out.println("(" + relativeX + ", " + relativeY + ")");
	    cellNearby[relativeX][relativeY] = gameSession.getNearby(relativeX, relativeY);
	    cellStatus[relativeX][relativeY] = gameSession.revealCell(relativeX, relativeY);
	    repaint();
	}
    }

    // These must be present because of implementation
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}

class GameSession {
    private int numCells;
    private int numFugus;

    private boolean[][] fugus;

    public GameSession(int numCells, int numFugus) {
	this.numCells = numCells;
	this.numFugus = numFugus;
	this.fugus = new boolean[numCells][numCells];

	initFugus();
    }
    public int getCells() {
	return this.numCells;
    }
    public int revealCell(int x, int y) {
	if (fugus[x][y] == true) {
	    return 3;
	}
	else {
	    return 1;
	}
    }
    public int getNearby(int x, int y) {
        int iFugus = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int iX = x + i;
                int jY = y + j;
                if (iX >= 0 && iX < numCells && jY >= 0 && jY < numCells && fugus[iX][jY]) {
                    iFugus++;
                }
            }
        }
        return iFugus;
    }
    private void initFugus() {
        Random random = new Random();
        int iNumFugus = 0;

        while (iNumFugus < numFugus) {
            int randomX = random.nextInt(numCells);
            int randomY = random.nextInt(numCells);

            if (fugus[randomX][randomY] == false) {
                fugus[randomX][randomY] = true;
                iNumFugus++;
            }
        }
    }

}

public class Main {
    public static void main(String[] args) {
        int numCells = 20;
	int numFugus = 40;

	GameSession gameSession = new GameSession(numCells, numFugus);

        JFrame frame = new JFrame("Fugusweeper");
        CellsPanel panel = new CellsPanel(gameSession);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(panel);
        frame.setVisible(true);

        // Initialize margin
        int marginX = frame.getWidth() / 16;
        int marginY = frame.getHeight() / 16;
        panel.setMargin(marginX, marginY);

        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int newMarginX = frame.getWidth() / 16;
                int newMarginY = frame.getHeight() / 16;
                panel.setMargin(newMarginX, newMarginY);
            }
        });
    }
}
