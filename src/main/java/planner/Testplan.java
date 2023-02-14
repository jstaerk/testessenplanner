package planner;

import java.util.Collection;
import java.util.List;

import org.optaplanner.core.api.domain.solution.*;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class Testplan {

	private long Id;
	private List<Station> stationList;

	private List<Test> testList;

	private HardSoftScore score;

	@ValueRangeProvider(id = "stationRange")
	public List<Station> getStationList() {
		return stationList;
	}

	@PlanningEntityCollectionProperty
	public List<Test> getTestList() {
		return testList;
	}

	@PlanningScore
	public HardSoftScore getScore() {
		return score;
	}

	public void setScore(HardSoftScore score) {
		this.score = score;
	}

	public void setStationList(List<Station> newTrunkList) {
		this.stationList = newTrunkList;
	}

	public void setTestList(List<Test> newBagList) {
		this.testList = newBagList;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

}
