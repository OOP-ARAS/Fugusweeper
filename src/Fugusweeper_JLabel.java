import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Fugusweeper_JLabel extends JLabel {
    private Fugusweeper fugusweeper;

    public Fugusweeper_JLabel(Fugusweeper fugusweeper){
	this.fugusweeper = fugusweeper;

        setText("");
	setHorizontalAlignment(SwingConstants.CENTER);
	setVerticalAlignment(SwingConstants.CENTER);
    }
}
