package representation;

import aima.search.framework.HeuristicFunction;
import model.Truck;

public class ProblemHeuristicFunction implements HeuristicFunction {

    @Override
    public double getHeuristicValue(Object state) {
        State currentState = (State) state;
        int kmCost = Global.KM_COST * currentState.getTrucks().stream()
                .mapToInt(Truck::getTravelledDistance).sum();
        int attendedPetitionsAmount = currentState.getAssignments().size();
        return (double) (Global.PETITION_SERVED_PROFIT * attendedPetitionsAmount - kmCost);
    }

}
