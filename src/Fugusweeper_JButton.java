import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Fugusweeper_JButton extends JButton {
    private Fugusweeper fugusweeper;
    private Fugusweeper_JPanel fugusweeper_jpanel;
    private Fugusweeper_JLabel fugusweeper_jlabel;

    public Fugusweeper_JButton(Fugusweeper fugusweeper, Fugusweeper_JPanel fugusweeper_jpanel, Fugusweeper_JLabel fugusweeper_jlabel) {
	this.fugusweeper = fugusweeper;
	this.fugusweeper_jpanel = fugusweeper_jpanel;
	this.fugusweeper_jlabel = fugusweeper_jlabel;

        setText("Restart");

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fugusweeper.initFugus();
		fugusweeper_jpanel.resetCellState();
		fugusweeper_jlabel.setText("");
            }
        });
    }

}
