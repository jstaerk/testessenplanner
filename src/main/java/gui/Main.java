package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private JButton Laufzettel;
    private JPanel panel1;
    private JButton namensschildButton;

    public Main() {

        setTitle("Testessen");
        setSize(450,450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setContentPane(panel1);

        namensschildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Badge b = new Badge();

            }
        });
        Laufzettel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Event ev=new Event();
            }
        });
    }
}
