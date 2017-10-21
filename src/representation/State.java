package representation;

import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Distribucion;
import model.Petition;
import model.RestrictionViolationException;
import model.Truck;

import java.util.*;
import java.util.stream.Collectors;

public class State {

    private final Map<Petition, Truck> assignments;
    private List<Truck> trucks;

    public State(boolean initial) {
        assignments = new HashMap<>();

        // Generate all trucks without assignments
        CentrosDistribucion distributionCenters = Global.getInstance().getDistributionCenters();
        trucks = new ArrayList<>(distributionCenters.size() * Global.TRUCKS_PER_DISTRIBUTION_CENTER);
        for (Distribucion origin : distributionCenters) {
            trucks.add(new Truck(origin));
        }

        if (initial) initialState();

        trucks = Collections.unmodifiableList(trucks);
    }

    private void initialState() {
        int seed = Global.SEED;
        for (Petition petition : Global.getInstance().getAllPetitions()) {
            Collections.shuffle(trucks, new Random(seed++));
            Optional<Truck> firstTruckAvailable = trucks.parallelStream().filter(truck -> !truck.isFull()).findFirst();
            if (firstTruckAvailable.isPresent()) {
                try {
                    Truck truck = firstTruckAvailable.get();
                    truck.addPetition(petition);
                    assignments.put(petition, truck);
                } catch (RestrictionViolationException ignore) { }
            }
        }
    }

    public List<Truck> getTrucksWithoutAssignments() {
        return getTrucks(false);
    }

    public List<Truck> getTrucksWithAssignments() {
        return getTrucks(true);
    }

    private List<Truck> getTrucks(boolean withAssignments) {
        return trucks.stream()
                .filter(truck -> truck.hasAssignments() == withAssignments)
                .collect(Collectors.toList());
    }

    public Map<Petition, Truck> getAssignments() {
        return assignments;
    }

    // OPERATORS

    public void changeTruck(Petition petition, Truck from, Truck to) throws RestrictionViolationException {
        if (!assignments.containsKey(petition) || !assignments.get(petition).equals(from)) {
            throw new IllegalArgumentException("Petition is not assigned to the required truck");
        }
        to.addPetition(petition);
        from.deletePetition(petition);
        assignments.put(petition, to);
    }

    public void assignTruck(Petition petition, Truck truck) throws RestrictionViolationException {
        if (assignments.containsKey(petition)) {
            throw new IllegalArgumentException("Petition is already assigned to a truck");
        }
        truck.addPetition(petition);
        assignments.put(petition, truck);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Petition, Truck> entry : assignments.entrySet()) {
            builder.append(entry.getKey()).append(" <- ").append(entry.getValue()).append('\n');
        }
        return builder.toString();
    }

}
