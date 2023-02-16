package planner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gui.Settings;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class Laufzettel {
    /**
     * Models for jtwig templates
     * <p>
     * we have an arraylist of testermodels, which have tester name and number and an arraylist of assignments,
     * which contain rounds (both name and index) and stations (both name and index)
     */

    private class NamedIndexModel {
        int index;
        String name;

        public NamedIndexModel(int index, String name) {
            super();
            this.index = index;
            this.name = name;
        }

    }

    private class TesterModel extends NamedIndexModel {
        ArrayList assignments;

        public TesterModel(int index, String name, ArrayList assignments) {
            super(index, name);
            this.assignments = assignments;
        }

    }

    private class AssignmentModel {
        NamedIndexModel round;
        NamedIndexModel station;

        public AssignmentModel(NamedIndexModel round, NamedIndexModel station) {
            super();
            this.round = round;
            this.station = station;
        }
    }


    // Maps tester (Integer) to station
    private HashMap<Integer, ArrayList<Integer>> testerStationHistory = new HashMap<Integer, ArrayList<Integer>>();

    // the names of the testers (names may be empty)
    private ArrayList<String> testers;

    // the names of the stations (names may be empty)
    private ArrayList<String> stations;

    public Laufzettel() {

        testers = new ArrayList<String>();
        stations = new ArrayList<String>();

    }

    public void setTesters(ArrayList<String> allTesters) {
        testers = allTesters;
        ArrayList<Integer> emptyArrayList = new ArrayList<Integer>();
        for (int roundIdx = 0; roundIdx < Testessen.rounds.size(); roundIdx++) {
            emptyArrayList.add(-1);
        }

        for (int testerIdx = 0; testerIdx < testers.size(); testerIdx++) {
            // init the journey
            // it is vital to use clone here, otherwise we get a arrayList of references to the same element
            testerStationHistory.put(new Integer(testerIdx), (ArrayList<Integer>) emptyArrayList.clone());
        }

    }

    public void setStations(ArrayList<String> allStations) {
        stations = allStations;
    }

    /***
     * Quickly visualize (i.e. make a string of) a single tester station history
     * @param r
     * @return
     */
    private String join(ArrayList<Integer> r) {

        String res = "";
        for (Integer currentInt : r) {
            res += currentInt + ",";
        }
        if (res.length() > 0) {
            res = res.substring(0, res.length() - 1);
        }
        return "{" + res + "}";
    }


    /***
     * Quickly visualize (i.e. make a string of) all tester's station history
     * @param r
     * @return
     */
    public String ajoin() {
        String res = "[";
        for (Integer currentKey : testerStationHistory.keySet()) {
            res += currentKey + ":" + join(testerStationHistory.get(currentKey)) + ";\n";
        }
        return res + "]";
    }

    /**
     * Read the template laufzettel-vorlage.twig
     */
    public void writeHTML() {

        String templateFilename = "laufzettel-vorlage.twig";
        Path currentRelativePath = Paths.get("");
        String workingDir = currentRelativePath.toAbsolutePath().toString();
        String absoluteFilename = workingDir + File.separator + templateFilename;
        if (!new File(absoluteFilename).exists()) {
            String templateSampleContent =
                    "<!DOCTYPE HTML>"
                            + "<html lang='de'>"
                            + "<head>"
                            + "<title>Usability Testessen Laufzettel</title>"
                            + "<meta charset='utf-8' /> " +
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
                            ".runden {\n" +
                            "\tlist-style-type: none;\n" +
                            "\tpadding:0px;\n" +
                            "}\n" +
                            "\n" +
                            ".bigger {\n" +
                            "\tfont-size:14pt;\n" +
                            "}\n" +
                            ".written {\n" +
                            "\tfont-size:13pt;\n" +
                            "\tfont-family:Shadows;\n" +
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
                            "{% for tester in tests %}\n" +
                            "\n" +
                            "<img src='logo-UT-Frankfurt.svg' class=\"maxsize breakPageBefore\" alt=\"Usability Testessen Logo\"/>\n" +
                            "   \n" +
                            "    <br class='clear'/>\n" +
                            "    <div class='content'>\n" +
                            "    <div class='bigger center'> <span class='printed'>Laufzettel</span> <br class='clear'/><span class='written'>{{tester.name}}</span></div>\n" +
                            "    <ul class='runden'>\n" +
                            "\n" +
                            "        <li class='clear'>\n" +
                            "            <div class='head runde'>Runde</div>\n" +
                            "                    <!--div class='head station written'> Station</div-->\n" +
                            "        </li>\n" +
                            "\n" +
                            "        {% for assignment in tester.assignments %}\n" +
                            "\n" +
                            "        <li class='clear'>\n" +
                            "            <div class='runde'>{{assignment.round.index}}.</div>\n" +
                            "                <div class='station written'> {{assignment.station.name}}</div>\n" +
                            "        </li>\n" +
                            "    \n" +
                            "        {% endfor %}\n" +
                            "    </ul>\n" +
                            "</div>\n" +
                            "{% endfor %}" +
                            "</body>"
                            + "</html>";
            Path file = Paths.get(templateFilename);
            try {
                Files.write(file, templateSampleContent.getBytes());

                System.err.println("Beispiel-Laufzettel-Vorlagedatei " + templateFilename + " geschrieben.");
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        JtwigTemplate template = JtwigTemplate.fileTemplate(absoluteFilename);
        JtwigModel model = JtwigModel.newModel();

        ArrayList testsNamedIndex = new ArrayList<>();

        for (int testerIdx = 0; testerIdx < testers.size(); testerIdx++) {

            String currentTestername = testers.get(testerIdx);
            if (currentTestername.length() == 0) {
                currentTestername = "Tester " + (testerIdx + 1);
            }

            ArrayList runden = new ArrayList();
            for (int roundIdx = 0; roundIdx < Testessen.rounds.size(); roundIdx++) {

                int station = get(testerIdx, roundIdx);
                String currentStationsname = "Pause";
                if (station >= 0) {
                    currentStationsname = stations.get(station);
                    if (currentStationsname.length() == 0) {
                        currentStationsname = "Station " + (station + 1);
                    }
                }

                AssignmentModel ai = new AssignmentModel(new NamedIndexModel(roundIdx + 1, Testessen.rounds.get(roundIdx)), new NamedIndexModel(station + 1, currentStationsname));
                runden.add(ai);
            }
            NamedIndexModel currentTesterNamedIndex = new NamedIndexModel(testerIdx + 1, currentTestername);
            TesterModel ti = new TesterModel(testerIdx + 1, currentTestername, runden);
            testsNamedIndex.add(ti);

        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream("laufzettel.html");
            Preferences prefs = Preferences.userRoot().node(Settings.class.getName());
            String ID1 = "pagesize";
            String pagesize = prefs.get(ID1, "101mm 54mm landscape");
            model.with("pageSize", pagesize);
            model.with("tests", testsNamedIndex);
            template.render(model, fos);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /***
     * Writes the spreadsheets by_tester.csv and by_station.csv
     */
    public void writeCSVs() {
        StringBuilder csv = new StringBuilder();
        csv.append("Runde #;Runde Info;Tester #;Tester Name;Station #;Station Name;\n");
        int roundIdx = 0;
        for (roundIdx = 0; roundIdx < Testessen.rounds.size(); roundIdx++) {
            for (int testerIdx = 0; testerIdx < testers.size(); testerIdx++) {

                int station = get(testerIdx, roundIdx);
                int roundNr = roundIdx + 1;
                int stationNr = station + 1;
                int testerNr = testerIdx + 1;
                csv.append(roundNr).append(";").append(Testessen.rounds.get(roundIdx)).append(";").append(testerNr).append(";").append(Testessen.testers.get(testerIdx)).append(";").append(stationNr).append(";").append(Testessen.stations.get(station)).append(";\n");

            }
        }

        Path file = Paths.get("by_tester.csv");
        try {
            Files.write(file, csv.toString().getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        csv = new StringBuilder();
        csv.append("Runde #;Runde Info;Station #;Station Name;Tester #;Tester Name;\n");

        for (roundIdx = 0; roundIdx < Testessen.rounds.size(); roundIdx++) {
            for (int stationIdx = 0; stationIdx < stations.size(); stationIdx++) {
                ArrayList<Integer> stationTesters = getTesterByStationRound(stationIdx, roundIdx);
                int roundNr = roundIdx + 1;
                int stationNr = stationIdx + 1;
                for (Integer tester : stationTesters) {
                    int testerNr = tester + 1;
                    csv.append(roundNr).append(";").append(Testessen.rounds.get(roundIdx)).append(";").append(stationNr).append(";").append(Testessen.stations.get(stationIdx)).append(";").append(testerNr).append(";").append(Testessen.testers.get(tester)).append(";\n");

                }

            }
        }
        file = Paths.get("by_station.csv");
        try {
            Files.write(file, csv.toString().getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Returns which tester(s) are assigned to a station in a particular round
     *
     * @param station
     * @param runde
     * @return
     */
    public ArrayList<Integer> getTesterByStationRound(int station, int runde) {

        ArrayList<Integer> res = new ArrayList<>();

        for (int testerIdx = 0; testerIdx < testers.size(); testerIdx++) {
            if (testerStationHistory.get(testerIdx).get(runde) == station) {
                res.add(testerIdx);
            }
        }
        return res;

    }

    /**
     * returns which station a tester is at at a particular round
     *
     * @param tester
     * @param runde
     * @return
     */
    public int get(int tester, int runde) {
        return testerStationHistory.get(tester).get(runde);
    }

    /**
     * places a tester at a particular station in a particular round
     *
     * @param tester
     * @param runde
     * @param station
     */
    public void set(int tester, int runde, int station) {
        ((ArrayList<Integer>) testerStationHistory.get(tester)).set(runde, station);
    }

    public boolean hasTesterVisitedStation(int tester, int station) {
        return (((ArrayList<Integer>) testerStationHistory.get(tester)).contains(station));
    }

    /***
     * Fills a laufzettel from a testplan
     * @param solvedTestplan
     */
    public void fromTestPlan(Testplan solvedTestplan) {
        for (int roundIdx = 0; roundIdx < Testessen.rounds.size(); roundIdx++) {
            for (Test process : solvedTestplan.getTestList()) {
                if (process.getRound() == roundIdx) {
                    Station theStation = process.getStation();
                    if (theStation != null) {
                        set(process.getTester(), roundIdx, theStation.getIndex());
                    }
                }
            }
        }
    }

}
