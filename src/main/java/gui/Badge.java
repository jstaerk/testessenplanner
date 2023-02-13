package gui;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import planner.App;
import planner.Laufzettel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Badge extends JFrame {
    private JButton printButton;
    private JTextField textField1;
    private JPanel badgePanel;

    public Badge() {
        setTitle("Testessen");
        setSize(450,450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setContentPane(badgePanel);
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Test");
                String templateFilename="badge-vorlage.twig";
                Path currentRelativePath = Paths.get("");
                String workingDir = currentRelativePath.toAbsolutePath().toString();
                String absoluteFilename=workingDir+ File.separator+templateFilename;
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
                                    "   size: 101mm 54mm landscape;\n" +
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
                                    "<body>\n" +
                                    "\n" +
                                    "<img src='logo-UT-Frankfurt.svg' class=\"maxsize breakPageBefore\" alt=\"Usability Testessen Logo\"/>\n" +
                                    "   \n" +
                                    "    <br class='clear'/>\n" +
                                    "    <div class='content'>\n" +
                                    "\t<div class='bigger center'> <br class='clear'/><span class='written'>{{name}}</span></div>\n" +
                                    "\n" +
                                    "</div>\n" +
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
                    model.with("name",textField1.getText());
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
    }
}
