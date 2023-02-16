package planner;

import gui.Badge;
import gui.Settings;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class Testessen {

    public static ArrayList<String> testers;
    public static ArrayList<String> stations;
    public static ArrayList<String> rounds;
    public static int maxNumTesterForStation = 5;

    public void printBadgesFromNames(final ArrayList<String> lines) {
        ArrayList<QualifiedName> qnames = new ArrayList<QualifiedName>();
        for (String line : lines) {
            qnames.add(new QualifiedName(line));
        }
        printBadgesFromQNames(qnames);

    }

    public void printBadgesFromQNames(final ArrayList<QualifiedName> qnames) {
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
                            "\t<div class='bigger center'> <span class='printed'>{{tester.function}}</span> <br class='clear'/><span class='written'>{{tester.name}}</span></div>\n" +

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
        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getTemplate(absoluteFilename);
        FileWriter fos;
        try {
            fos = new FileWriter("badge.html");
            Preferences prefs = Preferences.userRoot().node(Settings.class.getName());
            String ID1 = "pagesize";
            String pagesize = prefs.get(ID1, "101mm 54mm landscape");


            Map<String, Object> context = new HashMap<>();
            context.put("pageSize", pagesize);
            context.put("testers", qnames);

            QualifiedName qn=new QualifiedName("ABC def|Ghois");
            context.put("qn", qn);
            context.put("abc", "Testi");

            compiledTemplate.evaluate(fos, context);


        } catch (FileNotFoundException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Desktop.getDesktop().open(new File("badge.html"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
