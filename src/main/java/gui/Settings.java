package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

public class Settings extends JFrame {
    private JButton applyButton;
    private JComboBox cmbPapersize;
    private JPanel panel1;

    public Settings() {
        setTitle("Testessen");
        setSize(450,450);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setVisible(true);
        setContentPane(panel1);

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Preferences prefs = Preferences.userRoot().node(Settings.class.getName());
                String ID1 = "pagesize";
                String value="101mm 54mm landscape";

                switch (cmbPapersize.getSelectedIndex()) {
                    case 0: value="210mm 297mm landscape";//a4
                        break;
                        case 1: value="84.1mm 118.9mm landscape";//a5
                        break;
                        case 2: //label, 101x54
                        break;
                }
                prefs.put(ID1, value);

            }
        });
    }
}
