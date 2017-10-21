package representation;

import aima.search.framework.SuccessorFunction;
import model.Petition;
import model.RestrictionViolationException;
import model.Truck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProblemSuccessorFunction implements SuccessorFunction {

    @Override
    public List getSuccessors(Object state) {
        State current = (State) state;
        List<State> successors = new ArrayList<>();
        Collection<Petition> unserved = getUnservedPetitions(current);
        Collection<Truck> freeTrucks = current.getTrucksWithoutAssignments();
        System.out.println("Unserved: " + unserved.size());
        System.out.println("Free Trucks: " + freeTrucks.size());
        for (Truck truck : freeTrucks) {
            for (Petition petition : unserved) {
                try {
                    State next = new State(false);
                    next.assignTruck(petition, truck);
                    successors.add(next);
                } catch (RestrictionViolationException ignore) { }
            }
        }
        return successors;
    }

    private Collection<Petition> getUnservedPetitions(State state) {
        Set<Petition> assigned = state.getAssignments().keySet();
        return Global.getInstance().getAllPetitions().parallelStream()
                .filter(petition -> !assigned.contains(petition))
                .collect(Collectors.toSet());
    }

}
