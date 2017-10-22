package representation;

import aima.search.framework.SuccessorFunction;
import model.Petition;
import model.RestrictionViolationException;
import model.Truck;

import java.util.ArrayList;
import java.util.List;

public class ProblemSuccessorFunction implements SuccessorFunction {

    @Override
    public List getSuccessors(Object state) {
        State current = (State) state;
        List<State> successors = new ArrayList<>();
        List<Truck> trucks = current.getTrucks();
        for (Petition petition : Global.getInstance().getAllPetitions()) {
            for (Truck truck : trucks) {
                try {
                    State next = new State(current);
                    next.assignTruck(petition, truck.getId());
                    successors.add(next);
                } catch (RestrictionViolationException ignore) { }
            }
        }
        return successors;
    }

}
