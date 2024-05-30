import java.util.*;

/* To implement:
 *	Algorithm classes for fugu assignment, this is for difficulty
 * 	Main menu, where you can set difficulty, load game, start game
 *	Save button, this should be next to restart button, it will point you to other menu
 * Enums:
 * 	0 - Unrevealed
 * 	1 - Revealed
 * 	2 - Flagged
 * 	3 - Fugu
 */

public class Fugusweeper {
    // Private fields
    private int numCells;
    private int numFugus;

    private int numCellsLeft;

    private boolean[][] fugus;
    private boolean[][] revealed;

    // Constructor
    public Fugusweeper(int numCells) {
	setNumCells(numCells);
	setNumFugus(numCells*numCells/12); // Temporary

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
	setNumCellsLeft(getNumCellsLeft());

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
    public boolean isFugu(int x, int y) {
        return this.fugus[x][y];
    }
}
