import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/* To implement:
 *	Algorithm classes for fugu assignment, algorithms differs in difficulties
 * 	Main menu, where you can set difficulty, load game, etc
 *	Save button, this should be next to restart button, it will point you to other menu
 *	Header text box
 * Enums:
 * 	0 - Unrevealed
 * 	1 - Revealed
 * 	2 - Flagged
 * 	3 - Fugu
 */

class Fugusweeper_JFrame extends JFrame {
    private Fugusweeper fugusweeper;

    // Constructor
    public Fugusweeper_JFrame(Fugusweeper fugusweeper) {
	this.fugusweeper = fugusweeper;

	setDefaultCloseOperation(this.EXIT_ON_CLOSE);
	setSize(800, 800);
	setLayout(new GridBagLayout());

	Fugusweeper_JPanel panel = new Fugusweeper_JPanel(fugusweeper);
	Fugusweeper_JButton button = new Fugusweeper_JButton(fugusweeper, panel);

        GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(0, 0, 0, 0);
	gbc.anchor = GridBagConstraints.CENTER;
	gbc.fill = GridBagConstraints.BOTH;

	gbc.weightx = 1.0;
	gbc.weighty = 0.9;
        gbc.gridx = 0;
        gbc.gridy = 0;
	add(panel, gbc);

	gbc.weightx = 1.0;
	gbc.weighty = 0.1;
        gbc.gridx = 0;
        gbc.gridy = 1;
	add(button, gbc);

	setVisible(true);
    }
}
class Fugusweeper_JButton extends JButton {
    private Fugusweeper fugusweeper;
    private Fugusweeper_JPanel fugusweeper_jpanel;

    public Fugusweeper_JButton(Fugusweeper fugusweeper, Fugusweeper_JPanel fugusweeper_jpanel) {
	this.fugusweeper = fugusweeper;
	this.fugusweeper_jpanel = fugusweeper_jpanel;

        setText("Restart");

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fugusweeper.initFugus();
		fugusweeper_jpanel.resetCellState();
            }
        });
    }

}
class Fugusweeper_JPanel extends JPanel implements MouseListener {
    private Fugusweeper fugusweeper;

    private int startX;
    private int startY;
    private int numCells;
    private int cellSize;
    private String headerText; // DEPRECATED, GET OTHER CLASS
    private boolean freezePanel;

    private int[][] cellState;
    private int[][] cellNearby;

    private Image fuguImage;
    private Image flagImage;
    private Font font;

    public Fugusweeper_JPanel(Fugusweeper fugusweeper) {
	this.fugusweeper = fugusweeper;
        this.numCells = fugusweeper.getNumCells();
	this.cellState = new int[numCells][numCells];
	this.cellNearby = new int[numCells][numCells];
	this.headerText = "Fugusweeper!";

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        calculateGrid();

        // Header, NOW DEPRECATED, GET OTHER CLASS
	/*
	font = new Font("Comic Sans MS", Font.BOLD, marginY);
        g.setFont(font);
        fontMetrics = g.getFontMetrics(font);
        g.drawString(headerText, getWidth() / 2 - fontMetrics.stringWidth(headerText) / 2, 3*marginY/4);
	*/
	
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
			headerText = "You won!!!";
			freezePanel = true;
			revealedCellState = 1;
		    }
		    else if (revealedCellState == 3) {
			headerText = "You lost!!!";
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

class Fugusweeper {
    // Private fields
    private int numCells;
    private int numFugus;

    private int numCellsLeft;

    private boolean[][] fugus;
    private boolean[][] revealed;

    // Constructor
    public Fugusweeper(int numCells) {
	setNumCells(numCells);
	setNumFugus(numCells*numCells/14); // Temporary

	initFugus();

        Fugusweeper_JFrame frame = new Fugusweeper_JFrame(this);
    }

    // Getters
    public int getNumCells() {
	return this.numCells;
    }
    public int getNumCellsLeft() {
	return this.numCellsLeft;
    }
    // Setters
    public void setNumCells(int numCells) {
	this.numCells = numCells;
    }
    // Also sets numCellsLeft
    public void setNumFugus(int numFugus) {
	if (numFugus > getNumCells() * getNumCells()) {
	    // Throw exception
	}
	else {
	    this.numFugus = numFugus;
	    this.numCellsLeft = getNumCells()*getNumCells() - numFugus;
	}
    }
    // Used for updating
    public void setNumCellsLeft(int numCellsLeft) {
	this.numCellsLeft = numCellsLeft;
    }

    // Other methods
    public int revealCell(int x, int y) {
	if (fugus[x][y] == true) {
	    // Code for game lost, at the same time also for updating the cell with fugu
	    return 3;
	}
	else {
	    if (revealed[x][y] == false) {
		setNumCellsLeft(getNumCellsLeft() - 1);
		revealed[x][y] = true;
	    }
	    if (getNumCellsLeft() == 0) {
		// Code for game won
		return -1;
	    }
	    else {
		return 1;
	    }
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
    // This can use algorithm classes instead
    public void initFugus() {
	this.fugus = new boolean[getNumCells()][getNumCells()];
	this.revealed = new boolean[getNumCells()][getNumCells()];

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
	Fugusweeper fugusweeper = new Fugusweeper(20);
    }
}
