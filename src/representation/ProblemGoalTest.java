package representation;

import aima.search.framework.GoalTest;
import aima.search.framework.HeuristicFunction;
import model.Truck;

public class ProblemGoalTest implements GoalTest {

    @Override
    public boolean isGoalState(Object state) {
        return false;
    }
}
