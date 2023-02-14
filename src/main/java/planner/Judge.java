package planner;


import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.calculator.EasyScoreCalculator;

public class Judge  implements EasyScoreCalculator<Testplan, HardSoftScore> {

	/**
	 * 
	 * A very simple implementation. The double loop can easily be removed by
	 * using Maps as shown in
	 * 
	 * {@link CloudBalancingMapBasedEasyScoreCalculator#calculateScore(Testplan)}
	 * .
	 */

	public HardSoftScore calculateScore(Testplan theTestplan) {

		int hardScore = 0;

		int softScore = 0;

	

		Laufzettel l = new Laufzettel();
		l.setStations(App.stations);
		l.setTesters(App.testers);

		// es dürfen keine Stationen unbesetzt sein(?)
		for (Station theStation : theTestplan.getStationList()) {

			for (int roundIdx = 0; roundIdx < App.rounds.size(); roundIdx++) {
				for (Test theTester : theTestplan.getTestList()) {

					if ((theStation != null) && (theTester.getStation() != null)
							&& (theStation.getIndex() == theTester.getStation().getIndex())
							&& (theTester.getRound() == roundIdx)) {

						/* if this station is visited by another tester in the same round */
						int numTestersOnStation = 0;
						for (int currentTester = 0; currentTester < App.testers.size(); currentTester++) {
							if (currentTester == theTester.getTester()) {
								numTestersOnStation++; // first tester
							}

							if (l.get(currentTester, roundIdx) == theTester.getStation().getIndex()) {
								numTestersOnStation++;
							}

						}
						if (numTestersOnStation > App.maxNumTesterForStation) {
							hardScore -= 1;
						}
						/* 
						 * if this station was visited by this very tester in a previous round: this may never happen, i.e. hardscore
						   */
						if (l.hasTesterVisitedStation(theTester.getTester(), theTester.getStation().getIndex())) {
							hardScore -= 2;
						} else {

							// otherwise add station to history
							l.set(theTester.getTester(), roundIdx, theTester.getStation().getIndex());
							int utility = 100; // a single tester on a otherwise
												// untested station
							if (numTestersOnStation > 1) {
								utility = utility
										/ numTestersOnStation; 
								/* this way a station gets 100pt for the first tester, 150 for the
									second and so  on, i.e. a minimum but increasingly less points 
									per additional tester */
							}
							softScore += utility;// we have a test which should be added to the total benefit :-)
						}
						// prefer tester with low indices
						softScore -= theTester.getTester();

					}

				}

			}

		}
		return HardSoftScore.valueOf(hardScore, softScore);

	}
}