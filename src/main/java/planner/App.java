package planner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

	private static Testplan createRessourcesToOptimize() {

		Testplan theTestPlan = new Testplan();
		theTestPlan.setId(0L);

		List<Station> stationList = new ArrayList<Station>(2);

		for (int i = 0; i < stations.size(); i++) {
			Station station1 = new Station();
			station1.setIndex(i);
			stationList.add(station1);

		}

		theTestPlan.setStationList(stationList);

		List<Test> testList = new ArrayList<Test>(testers.size());

		for (int i = 0; i < testers.size(); i++) {

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

	public static void main(String[] args) {
		System.out.println("Laufzettel Schritt 1 v 3:\n");
		Scanner scan = new Scanner(System.in);
		
		stations = new ArrayList<String>();
		testers = new ArrayList<String>();
		rounds = new ArrayList<String>();

        

		

		String rundenName = "";
		while (!rundenName.equals(".")) {
			int index = rounds.size() + 1;
			System.out.println("Runde " + index + " info (\"RETURN\"=keine Angabe, \".\"=beende Liste):");
			rundenName = scan.nextLine();
			rounds.add(rundenName);
		}
		// letzte Runde ="." entfernen
		rounds.remove(rounds.size() - 1);

		System.out.println("Danke. Schritt 2 v 3:\n");



		String stationName = "";
		while (!stationName.equals(".")) {
			int index = stations.size() + 1;
			System.out.println("Station " + index + " name (\"RETURN\"=keine Angabe, \".\"=beende Liste):");
			stationName = scan.nextLine();
			stations.add(stationName);
		}
		// letzte Station ="." entfernen
		stations.remove(stations.size() - 1);

		System.out.println("Danke. Schritt 3 von 3:\n");

		String testerName = "";
		while (!testerName.equals(".")) {

			int index = testers.size() + 1;
			System.out.println(
					"Tester " + (testers.size() + 1) + " name (\"RETURN\"=keine Angabe, \".\"=beende Liste):");
			testerName = scan.nextLine();
			testers.add(testerName);
		}

		// letzten Tester ="." entfernen
		testers.remove(testers.size() - 1);
		
		// End of input
		scan.close();
		
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
		l.setTesters(testers);
		l.setStations(stations);
		l.fromTestPlan(solvedLuggage);

		// Display the result
		if (solvedLuggage.getScore().getHardScore() < 0) {
			System.err.println("Does not yet look feasible: hard score " + solvedLuggage.getScore().getHardScore());
		} else {
			System.out.println("Feasibility " + solvedLuggage.getScore().getHardScore() + " Gain "
					+ solvedLuggage.getScore().getSoftScore());

			l.writeCSVs();
			l.writeHTML();

			System.out.println("by_tester.csv geschrieben.\nby_station.csv geschrieben.\nlaufzettel.html geschrieben.");
		}
	}

}
