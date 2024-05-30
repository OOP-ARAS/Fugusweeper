import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Fugusweeper_JFrame extends JFrame {
    private Fugusweeper fugusweeper;

    // Constructor
    public Fugusweeper_JFrame(Fugusweeper fugusweeper) {
	super("Fugusweeper"); // Calls superclass's constructor to set title, should be called before everything
	this.fugusweeper = fugusweeper;

	setDefaultCloseOperation(this.EXIT_ON_CLOSE);
	setSize(800, 800);
	setLayout(new GridBagLayout());

	Fugusweeper_JLabel label = new Fugusweeper_JLabel(fugusweeper);
	Fugusweeper_JPanel panel = new Fugusweeper_JPanel(fugusweeper, label);
	Fugusweeper_JButton button = new Fugusweeper_JButton(fugusweeper, panel, label);

        GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(0, 0, 0, 0);
	gbc.anchor = GridBagConstraints.CENTER;
	gbc.fill = GridBagConstraints.BOTH;

	gbc.weightx = 1.0;
	gbc.weighty = 0.9;
	gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
	add(panel, gbc);

	gbc.weightx = 1.0;
	gbc.weighty = 0.1;
	gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
	JScrollPane scrollPaneButton = new JScrollPane(button);
	add(scrollPaneButton, gbc);

	gbc.weightx = 1.0;
	gbc.weighty = 0.1;
	gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
	JScrollPane scrollPaneLabel = new JScrollPane(label);
	add(scrollPaneLabel, gbc);

	pack();
	setLocationRelativeTo(null);
	setVisible(true);
    }
}
