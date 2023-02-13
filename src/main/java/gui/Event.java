package gui;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.score.definition.ScoreDefinitionType;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import planner.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Event extends JFrame {
    private JPanel jPanel;
    private JButton btnPlan;
    private JTextArea textAreaRounds;
    private JTextArea textAreaStations;
    private JTextArea textAreaTesters;


    private Testplan createRessourcesToOptimize() {

        Testplan theTestPlan = new Testplan();
        theTestPlan.setId(0L);

        List<Station> stationList = new ArrayList<Station>(2);

        for (int i = 0; i < App.stations.size(); i++) {
            Station station1 = new Station();
            station1.setIndex(i);
            stationList.add(station1);

        }

        theTestPlan.setStationList(stationList);

        List<Test> testList = new ArrayList<Test>(App.testers.size());

        for (int i = 0; i < App.testers.size(); i++) {

            for (int j = 0; j < App.rounds.size(); j++) {

                Test aTest = new Test();
                aTest.setLabel("Test " + j + " with tester " + i);
                aTest.setTester(i);
                aTest.setRound(j);
                testList.add(aTest);

            }

        }
        theTestPlan.setTestList(testList);

        return theTestPlan;
    }

    public Event() {

        setTitle("Testessen");
        setSize(450,450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setContentPane(jPanel);
        final JFrame theFrame=this;
        btnPlan.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                theFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                App.stations = new ArrayList<>(Arrays.asList(textAreaStations.getText().split("\n")));

                App.testers = new ArrayList<>(Arrays.asList(textAreaTesters.getText().split("\n")));

                App.rounds = new ArrayList<>(Arrays.asList(textAreaRounds.getText().split("\n")));



                // build the solver
                SolverFactory<Testplan> solverFactory = SolverFactory.createEmpty();

                SolverConfig sc = solverFactory.getSolverConfig();
                sc.setSolutionClass(Testplan.class);

                ArrayList<Class> entities = new ArrayList<Class>();
                entities.add(Test.class);
                sc.setEntityClassList((List) entities);
                ScoreDirectorFactoryConfig sdc = new ScoreDirectorFactoryConfig();

                sdc.setScoreDefinitionType(ScoreDefinitionType.HARD_SOFT);
                sdc.setEasyScoreCalculatorClass(Judge.class);
                sc.setScoreDirectorFactoryConfig(sdc);
                TerminationConfig tc = new TerminationConfig();
                tc.setSecondsSpentLimit(10L);

                sc.setTerminationConfig(tc);

                Solver solver = solverFactory.buildSolver();

                Testplan unsolvedLuggage = createRessourcesToOptimize();

                // Solve the problem

                solver.solve(unsolvedLuggage);

                Testplan solvedLuggage = (Testplan) solver.getBestSolution();
                Laufzettel l = new Laufzettel();
                l.setTesters(App.testers);
                l.setStations(App.stations);
                l.fromTestPlan(solvedLuggage);

                // Display the result
                if (solvedLuggage.getScore().getHardScore() < 0) {
                    JOptionPane.showMessageDialog(theFrame, "Does not yet look feasible: hard score " + solvedLuggage.getScore().getHardScore());
                } else {
                    l.writeCSVs();
                    l.writeHTML();
                    JOptionPane.showMessageDialog(theFrame, "Feasibility " + solvedLuggage.getScore().getHardScore() + " Gain "
                            + solvedLuggage.getScore().getSoftScore() + " \nCSV und HTML-Dateien geschrieben.");
                    try {
                        Desktop.getDesktop().open(new File("laufzettel.html"));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                theFrame.setCursor(Cursor.getDefaultCursor());
            }


        });
    }
}