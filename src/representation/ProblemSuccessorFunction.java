package representation;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import model.Petition;
import model.RestrictionViolationException;
import model.Truck;

import java.util.ArrayList;
import java.util.List;

public class ProblemSuccessorFunction implements SuccessorFunction {

    //public static int count, calls = 0;
    //private static final ProblemHeuristicFunction heuristicFunction = new ProblemHeuristicFunction();

    @Override
    public List getSuccessors(Object state) {
        //count = 0;
        //calls++;
        //double max = Integer.MIN_VALUE;
        //System.out.println("getSuccessors() #" + calls);
        State current = (State) state;
        List<Successor> successors = new ArrayList<>();
        List<Truck> trucks = current.getTrucks();
        for (Petition petition : Global.getInstance().getAllPetitions()) {
            for (Truck truck : trucks) {
                try {
                    State next = new State(current);
                    next.assignTruck(petition, truck.getId());
                    //double heuristic = Math.abs(heuristicFunction.getHeuristicValue(next));
                    //if (heuristic > max) max = heuristic;
                    successors.add(new Successor("Assign " + next.getTrucks().get(truck.getId()) + " to Petition " + petition, next));
                    //count++;
                } catch (RestrictionViolationException ignore) { }
            }
        }
        //System.out.println("SUCCESSORS #" + calls + ": " + count);
        //System.out.println("MAX HEURISTIC #" + calls + ": " + max);
        return successors;
    }

}
