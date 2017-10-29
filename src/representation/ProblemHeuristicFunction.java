package representation;

import aima.search.framework.HeuristicFunction;
import model.Petition;

public class ProblemHeuristicFunction implements HeuristicFunction {

    @Override
    public double getHeuristicValue(Object state) {
        State currentState = (State) state;
        int kmCost = Global.KM_COST * currentState.getTravelledDistance();
        double profit = currentState.getAssignments().keySet().parallelStream().mapToDouble(this::getProfit).sum();
        double profitNextDay = currentState.getUnassignedPetitions().parallelStream().mapToDouble(this::getProfitNextDay).sum();
        return -(profit - kmCost + profitNextDay); // Negative because we need to maximize profit but Hill Climbing minimizes
    }

    private double getProfit(Petition petition) {
        int days = petition.getPendingDays();
        if (days == 0) return Global.PETITION_SERVED_PROFIT * 1.02;
        return Global.PETITION_SERVED_PROFIT * (100 - twoPow(days))/100d;
    }

    private double getProfitNextDay(Petition petition) {
        int days = petition.getPendingDays() + 1;
        Petition newPetition = new Petition(petition);
        newPetition.setPendingDays(days);
        return getProfit(newPetition);
    }

    private int twoPow(int d) {
        return 1 << d;
    }
}
