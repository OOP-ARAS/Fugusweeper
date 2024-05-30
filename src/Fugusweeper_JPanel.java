import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Fugusweeper_JPanel extends JPanel implements MouseListener {
    private Fugusweeper fugusweeper;
    private Fugusweeper_JLabel fugusweeper_jlabel;

    private int startX;
    private int startY;
    private int numCells;
    private int cellSize;
    private boolean freezePanel;

    private int[][] cellState;
    private int[][] cellNearby;

    private Image fuguImage;
    private Image flagImage;
    private Font font;

    public Fugusweeper_JPanel(Fugusweeper fugusweeper, Fugusweeper_JLabel fugusweeper_jlabel) {
	this.fugusweeper = fugusweeper;
	this.fugusweeper_jlabel = fugusweeper_jlabel;

        this.numCells = fugusweeper.getNumCells();
	this.cellState = new int[numCells][numCells];
	this.cellNearby = new int[numCells][numCells];

	calculateGrid();

        fuguImage = Toolkit.getDefaultToolkit().getImage("Fugnus1.png");
        flagImage = Toolkit.getDefaultToolkit().getImage("CoralReef.png");

        addMouseListener(this);
    }

    // Also resets cellNearby
    public void resetCellState() {
	this.cellState = new int[numCells][numCells];
	this.cellNearby = new int[numCells][numCells];
	freezePanel = false;
	
	repaint();
    }

    private void calculateGrid() {
        cellSize = Math.min(getWidth(), getHeight()) / numCells;

        startX = (getWidth() - cellSize * numCells) / 2;
        startY = (getHeight() - cellSize * numCells) / 2;
    }

    private void revealAllFugu() {
        for (int i = 0; i < numCells; i++) {
            for (int j = 0; j < numCells; j++) {
                if (fugusweeper.isFugu(i, j)) {
                    cellState[i][j] = 3;
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        calculateGrid();
	
	// Cell fugu nearby indicator
	font = new Font("Comic Sans MS", Font.BOLD, cellSize);
        g.setFont(font);

        for (int i = 0; i < numCells; i++) {
            for (int j = 0; j < numCells; j++) {
                int x = startX + i * cellSize;
                int y = startY + j * cellSize;
		// Switch case could perhaps be of use here
		if (cellState[i][j] == 0) {
		    g.setColor(Color.DARK_GRAY);
		    g.fillRect(x, y, cellSize, cellSize);
		}
		else if (cellState[i][j] == 1) {
		    g.setColor(Color.BLUE);
		    g.fillRect(x, y, cellSize, cellSize);
		    if (cellNearby[i][j] > 0) {
			g.setColor(Color.BLACK);
                        g.drawString(Integer.toString(cellNearby[i][j]), x + cellSize/4, y + 7*cellSize/8);
		    }
		}
		else if (cellState[i][j] == 2) {
		    g.setColor(Color.GRAY);
		    g.fillRect(x, y, cellSize, cellSize);
                    g.drawImage(flagImage, x, y, cellSize, cellSize, this);
		}
		else if (cellState[i][j] == 3) {
		    g.setColor(Color.YELLOW);
		    g.fillRect(x, y, cellSize, cellSize);
                    g.drawImage(fuguImage, x, y, cellSize, cellSize, this);
		}
	        g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int relativeX = (e.getX() - startX) / cellSize;
        int relativeY = (e.getY() - startY) / cellSize;

	int revealedCellState;

	if ((relativeX >= 0 && relativeX < numCells) &&
	(relativeY >= 0 && relativeY < numCells) &&
	(freezePanel == false)) {
//	    System.out.println("(" + relativeX + ", " + relativeY + ")");
            if (e.getButton() == MouseEvent.BUTTON1) {
		if (cellState[relativeX][relativeY] != 2) { // So we don't accidentally reveal flagged cells
		    // Right now the main way panel communicates with Fugusweeper is through revealCell
		    revealedCellState = fugusweeper.revealCell(relativeX, relativeY);
		    if (revealedCellState == -1) {
			fugusweeper_jlabel.setText("You won! Try again");
			freezePanel = true;
			revealedCellState = 1;
		    }
		    else if (revealedCellState == 3) {
			fugusweeper_jlabel.setText("You lost! Try again");
            		revealAllFugu();
			freezePanel = true;
		    }
		    cellState[relativeX][relativeY] = revealedCellState;
		    cellNearby[relativeX][relativeY] = fugusweeper.getNearby(relativeX, relativeY);
		}
	    }
	    else if (e.getButton() == MouseEvent.BUTTON3) {
		// We don't need Fugusweeper to know whether cells are flagged, logically speaking
		// But for load/save, MAYBE
		if (cellState[relativeX][relativeY] == 0) {
		    cellState[relativeX][relativeY] = 2;
		}
		else if (cellState[relativeX][relativeY] == 2) {
		    cellState[relativeX][relativeY] = 0;
		}
	    }
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
