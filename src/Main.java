import aima.search.framework.GoalTest;
import aima.search.framework.Problem;
import aima.search.informed.HillClimbingSearch;
import model.Petition;
import model.Trip;
import model.Truck;
import representation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        State initialState = new State();
        System.out.println(initialState);
        test(initialState);
        List successors = new ProblemSuccessorFunction().getSuccessors(initialState);
        System.out.println("Successors: " + successors.size());
        for (Object successorObj : successors) {
            test((State) successorObj);
        }

//        State initialState = new State();
//        ProblemSuccessorFunction successorFunction = new ProblemSuccessorFunction();
//        ProblemHeuristicFunction heuristicFunction = new ProblemHeuristicFunction();
//        Problem problem = new Problem(initialState, successorFunction, isGoalState -> false, heuristicFunction);
//        HillClimbingSearch hillClimbingSearch = new HillClimbingSearch();
//        try {
//            hillClimbingSearch.search(problem);
//        } catch (Exception e) {
//            System.out.println("Oops, something went wrong.");
//        }


    }

    private static boolean test(State state) {
        int i = 0;
        ProblemHeuristicFunction heuristic = new ProblemHeuristicFunction();
        System.out.println("Heuristic value for this node: " + heuristic.getHeuristicValue(state));
        for (Truck truck : state.getTrucks()) {
            if (truck.getId() != i++) {
                System.out.println("TRUCK ID " + truck.getId() + " DOES NOT MATCH WITH LIST POSITION: " + i);
                System.out.println(truck);
                return false;
            }
            if (truck.getTravelledDistance() > Global.MAX_KM_PER_DAY) {
                System.out.println("MAX_KM_PER_DAY");
                System.out.println(truck);
                return false;
            }
            if (truck.getTrips().size() > Global.MAX_TRIPS_PER_TRUCK) {
                System.out.println("MAX_TRIPS_PER_TRUCK");
                System.out.println(truck);
                return false;
            }
            Set<Petition> servedPetitions = new HashSet<>();
            for (Trip trip : truck.getTrips()) {
                if (trip.size() > Global.ASSIGNATIONS_PER_TRIP) {
                    System.out.println("ASSIGNATIONS_PER_TRIP");
                    System.out.println(truck);
                    return false;
                }
                for (Petition petition : trip) {
                    if (!servedPetitions.add(petition)) {
                        System.out.println("TRUCK SERVES IDENTICAL PETITION TWO TIMES");
                        System.out.println(truck);
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
