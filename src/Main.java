import model.Petition;
import model.Trip;
import model.Truck;
import representation.Global;
import representation.ProblemSuccessorFunction;
import representation.State;

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
    }

    private static boolean test(State state) {
        int i = 0;
        for (Truck truck : state.getTrucks()) {
            if (truck.getId() != i++) {
                System.out.println("TRUCK ID " + truck.getId() + " DOES NOT MATCH WITH LIST POSITION: " + i);
                System.out.println(truck);
            }
            if (truck.getDistanceTraveled() > Global.MAX_KM_PER_DAY) {
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
