package planner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import gui.Main;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.score.definition.ScoreDefinitionType;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;

import javax.swing.*;

/**
 * 
 * testessenPlanner 1.2 by Jochen Stärk <jstaerk@usegroup.de>, 2016-12-24.
 * 
 * This software uses OptaPlanner to create a "Laufzettel" (routing slip) for
 * the testers of a usability-Testessen event (http://usability-testessen.de/)
 *
 */
public class App {


	public static void main(String[] args) {
		JFrame frame=new JFrame("Testessen");
		frame.setTitle("Testessen");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.setContentPane(new Main().panel1);
		frame.setSize(650,450);
		//frame.pack();
		frame.setVisible(true);

	}

}
