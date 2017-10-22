import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import model.Petition;
import model.Trip;
import model.Truck;
import representation.Global;
import representation.ProblemHeuristicFunction;
import representation.ProblemSuccessorFunction;
import representation.State;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        State initialState = new State();
        ProblemSuccessorFunction successorFunction = new ProblemSuccessorFunction();
        ProblemHeuristicFunction heuristicFunction = new ProblemHeuristicFunction();
        Problem problem = new Problem(initialState, successorFunction, isGoalState -> false, heuristicFunction);
        System.out.println("Initial State:\n" + initialState);
        System.out.println("Initial Heuristic: " + -heuristicFunction.getHeuristicValue(initialState));
        System.out.println("Initial Total Kilometers: " + initialState.getTravelledDistance());
        System.out.println("Initial Assigned Petitions: " + initialState.getAssignments().size());
        System.out.println("\nCalculating Solution...\n");
        HillClimbingSearch hillClimbingSearch = new HillClimbingSearch();
        try {
            long start = System.currentTimeMillis();
            SearchAgent agent = new SearchAgent(problem, hillClimbingSearch);
            long end = System.currentTimeMillis();
            System.out.println("Hill Climbing Time: " + (end - start) + " ms");
            System.out.println("\nActions:");
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
            List pathStates = hillClimbingSearch.getPathStates();
            System.out.println();
            State last = (State) pathStates.get(pathStates.size() - 1);
            System.out.println("Final State:\n" + last);
            System.out.println("Heuristic: " + -heuristicFunction.getHeuristicValue(last));
            System.out.println("Total Kilometers: " + last.getTravelledDistance());
            System.out.println("Total Petitions: " + Global.getInstance().getAllPetitions().size());
            System.out.println("Assigned Petitions: " + last.getAssignments().size());
            List<Petition> unassigned = last.getUnassignedPetitions();
            System.out.println("Unassigned Petitions: " + unassigned.size());
        } catch (Exception e) {
            System.out.println("Oops, something went wrong.");
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties) {
        for (Object o : properties.keySet()) {
            String key = (String) o;
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }

    private static void printActions(List actions) {
        for (Object action1 : actions) {
            String action = (String) action1;
            System.out.println(action);
        }
    }

    private static boolean test(State state) {
        int i = 0;
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
