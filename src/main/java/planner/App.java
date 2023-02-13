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

/**
 * 
 * testessenPlanner 1.2 by Jochen Stärk <jstaerk@usegroup.de>, 2016-12-24.
 * 
 * This software uses OptaPlanner to create a "Laufzettel" (routing slip) for
 * the testers of a usability-Testessen event (http://usability-testessen.de/)
 * 
 * build it with /opt/local/bin/mvn clean compile assembly:single
 * 
 */
public class App {

	public static ArrayList<String> testers;
	public static ArrayList<String> stations;
	public static ArrayList<String> rounds;
	public static int maxNumTesterForStation = 5;


	public static void main(String[] args) {
		Main m=new Main();

	}

}
