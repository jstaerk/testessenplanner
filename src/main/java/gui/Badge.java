package gui;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import planner.App;
import planner.Laufzettel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.prefs.Preferences;

public class Badge extends JFrame {
    private JButton printButton;
    private JPanel badgePanel;
    private JTextArea textAreaTester;
    private JLabel lblCount;

    public Badge() {
        setTitle("Testessen");
        setSize(450, 450);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setVisible(true);
        setContentPane(badgePanel);
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String templateFilename = "badge-vorlage.twig";
                Path currentRelativePath = Paths.get("");
                String workingDir = currentRelativePath.toAbsolutePath().toString();
                String absoluteFilename = workingDir + File.separator + templateFilename;
                if (!new File(absoluteFilename).exists()) {
                    String templateSampleContent =
                            "<!DOCTYPE HTML>\n" +
                                    "<html lang=\"de\">\n" +
                                    "<head>\n" +
                                    "<title>Usability Testessen Badge</title>\n" +
                                    "<meta charset=\"utf-8\" />\n" +
                                    "<style>\n" +
                                    "@font-face {\n" +
                                    "\tfont-family: Bitter;\n" +
                                    "\tsrc: url('Bitter regular.ttf')\n" +
                                    "}\n" +
                                    "@font-face {\n" +
                                    "\tfont-family: Shadows;\n" +
                                    "\tsrc: url('Shadows Into Light regular.ttf')\n" +
                                    "}\n" +
                                    "@page {\n" +
                                    "   size: {{pageSize}};\n" +
                                    "   margin: 5mm 5mm 5mm 1mm;\n" +
                                    "}\n" +
                                    ".content {\n" +
                                    "\tpadding-left:40px;\n" +
                                    "\tpage-break-after: always;\n" +
                                    "}\n" +
                                    ".bigger {\n" +
                                    "\tfont-size:14pt;\n" +
                                    "}\n" +
                                    ".written {\n" +
                                    "\tfont-size:32pt;\n" +
                                    "\tfont-family:shadows;\n" +
                                    "\tcolor:#f23e0e\n" +
                                    "}\n" +
                                    ".printed {\n" +
                                    "\tfont-size:13pt;\n" +
                                    "\tfont-weight:bold;\n" +
                                    "\tcolor:#6f6e6f;\n" +
                                    "\tfont-family:Bitter;\n" +
                                    "}\n" +
                                    ".station {\n" +
                                    "\tfloat:left;\n" +
                                    "}\n" +
                                    ".maxsize {\n" +
                                    "\tmax-width:100%;\n" +
                                    "\tmax-height:100%\n" +
                                    "}\n" +
                                    ".runde {\n" +
                                    "\tfont-size:13pt;\n" +
                                    "\tfont-weight:bold;\n" +
                                    "\tcolor:#6f6e6f;\n" +
                                    "\tfloat:left;\n" +
                                    "\twidth:1em;\n" +
                                    "\tpadding-top: 4px;\n" +
                                    "\tfont-family:Bitter;\n" +
                                    "}\n" +
                                    ".clear {\n" +
                                    "\tclear:both;\n" +
                                    "}\n" +
                                    ".breakPageBefore {\n" +
                                    "  page-break-before: always;\n" +
                                    "}\n" +
                                    ".center {\n" +
                                    "\ttext-align: center;\n" +
                                    "}\n" +
                                    ".head {\n" +
                                    "\n" +
                                    "}\n" +
                                    "\n" +
                                    "</style>\n" +
                                    "</head>\n" +
                                    "<body>{% for tester in testers %}\n" +
                                    "\n" +
                                    "<img src='logo-UT-Frankfurt.svg' class=\"maxsize breakPageBefore\" alt=\"Usability Testessen Logo\"/>\n" +
                                    "   \n" +
                                    "    <br class='clear'/>\n" +
                                    "    <div class='content'>\n" +
                                    "\t<div class='bigger center'> <span class='printed'></span> <br class='clear'/><span class='written'>{{tester}}</span></div>\n" +

                                    "</div>\n" +
                                    "{% endfor %}\n" +
                                    "</body>\n" +
                                    "</html>\n";
                    Path file = Paths.get(templateFilename);
                    try {
                        Files.write(file, templateSampleContent.getBytes());

                        System.err.println("Beispiel-Badge-Vorlagedatei " + templateFilename + " geschrieben.");
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                JtwigTemplate template = JtwigTemplate.fileTemplate(absoluteFilename);
                JtwigModel model = JtwigModel.newModel();


                FileOutputStream fos;
                try {
                    fos = new FileOutputStream("badge.html");
                    Preferences prefs = Preferences.userRoot().node(Settings.class.getName());
                    String ID1 = "pagesize";
                    String pagesize = prefs.get(ID1, "101mm 54mm landscape");
                    model.with("pageSize", pagesize);

                    model.with("testers", new ArrayList<>(Arrays.asList(textAreaTester.getText().split("\n"))));
                    template.render(model, fos);
                } catch (FileNotFoundException ex) {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }
                try {
                    Desktop.getDesktop().open(new File("badge.html"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        textAreaTester.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                lblCount.setText(Integer.toString(textAreaTester.getText().split("\n").length));
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        badgePanel = new JPanel();
        badgePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        printButton = new JButton();
        printButton.setText("Print");
        badgePanel.add(printButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        badgePanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textAreaTester = new JTextArea();
        badgePanel.add(textAreaTester, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        lblCount = new JLabel();
        lblCount.setText("");
        badgePanel.add(lblCount, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return badgePanel;
    }
}
