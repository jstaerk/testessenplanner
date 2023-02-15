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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private JLabel lblCountRounds;
    private JLabel lblCountStations;
    private JLabel lblCountTesters;


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
        setSize(450, 450);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setVisible(true);
        setContentPane(jPanel);
        final JFrame theFrame = this;
        btnPlan.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                theFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                App.stations = new ArrayList<>(Arrays.asList(textAreaStations.getText().split("\n")));

                App.testers = new ArrayList<>(Arrays.asList(textAreaTesters.getText().split("\n")));

                App.rounds = new ArrayList<>(Arrays.asList(textAreaRounds.getText().split("\n")));


                ArrayList<Class> entities = new ArrayList<Class>();
                entities.add(Test.class);

                ScoreDirectorFactoryConfig sdc = new ScoreDirectorFactoryConfig();

                sdc.setEasyScoreCalculatorClass(Judge.class);

                TerminationConfig tc = new TerminationConfig();
                tc.setUnimprovedMillisecondsSpentLimit(10000l);

                SolverFactory<Testplan> solverFactory = SolverFactory.create(new SolverConfig()
                        .withSolutionClass(Testplan.class)
                        .withEntityClasses(Test.class)
                        .withScoreDirectorFactory(sdc)
                        .withTerminationConfig(tc));
                Solver<Testplan> solver = solverFactory.buildSolver();


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
        textAreaRounds.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                lblCountRounds.setText(Integer.toString(textAreaRounds.getText().split("\n").length));
            }
        });
        textAreaStations.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                lblCountStations.setText(Integer.toString(textAreaStations.getText().split("\n").length));
            }
        });
        textAreaTesters.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                lblCountTesters.setText(Integer.toString(textAreaTesters.getText().split("\n").length));
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
        jPanel = new JPanel();
        jPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        btnPlan = new JButton();
        btnPlan.setText("Planen");
        jPanel.add(btnPlan, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Runden");
        jPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textAreaRounds = new JTextArea();
        jPanel.add(textAreaRounds, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Stationen");
        jPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textAreaStations = new JTextArea();
        jPanel.add(textAreaStations, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Tester");
        jPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textAreaTesters = new JTextArea();
        jPanel.add(textAreaTesters, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        lblCountRounds = new JLabel();
        lblCountRounds.setText("");
        jPanel.add(lblCountRounds, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblCountStations = new JLabel();
        lblCountStations.setText("");
        jPanel.add(lblCountStations, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblCountTesters = new JLabel();
        lblCountTesters.setText("");
        jPanel.add(lblCountTesters, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return jPanel;
    }
}
