package planner;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Test {
/***
 * Test refers to a visit of a tester at a station in a round
 */
	private int size;


	/**
	 * theStation equalling null would mean the tester has no assignment in this round
	 */
	@PlanningVariable(valueRangeProviderRefs = "stationRange", nullable = true) 
	private Station theStation;

	private int whichRound;

	private int tester;

	private String label;

	public Station getStation() {
		return theStation;
	}

	public void setStation(Station newStation) {
		theStation = newStation;
	}

	public int getRound() {
		return whichRound;
	}

	public void setRound(int newRound) {
		whichRound = newRound;
	}

	public int getSize() {
		return size;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getTester() {
		return tester;
	}

	public void setTester(int tester) {
		this.tester = tester;
	}

}
