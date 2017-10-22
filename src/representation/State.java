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

    public State() {
        assignments = new HashMap<>();
        // Generate all trucks without assignments
        CentrosDistribucion distributionCenters = Global.getInstance().getDistributionCenters();
        trucks = new ArrayList<>(distributionCenters.size() * Global.TRUCKS_PER_DISTRIBUTION_CENTER);
        for (Distribucion origin : distributionCenters) {
            trucks.add(new Truck(origin));
        }
        initialState();
        trucks = Collections.unmodifiableList(trucks);
    }

    public State(State copyState) {
        assignments = new HashMap<>(copyState.assignments.size());
        trucks = new ArrayList<>(copyState.trucks.size());
        Map<Truck, Truck> copies = new HashMap<>(copyState.trucks.size());
        for (Map.Entry<Petition, Truck> assignment : copyState.assignments.entrySet()) {
            Truck original = assignment.getValue();
            if (!copies.containsKey(original)) {
                Truck copy = new Truck(original);
                copies.put(original, copy);
                trucks.add(copy);
            }
            assignments.put(assignment.getKey(), copies.get(original));
        }
        List<Truck> trucksNotAssigned = copyState.trucks.stream()
                .filter(truck -> !truck.hasAssignments())
                .map(Truck::new)
                .collect(Collectors.toList());
        trucks.addAll(trucksNotAssigned);
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

    public List<Truck> getTrucks() {
        return trucks;
    }

    public Map<Petition, Truck> getAssignments() {
        return assignments;
    }

    public void assignTruck(Petition petition, Truck truck) throws RestrictionViolationException {
        Truck oldAssignedTruck = assignments.get(petition);
        if (truck.equals(oldAssignedTruck)) {
            throw new RestrictionViolationException("Petition already assigned to that truck");
        }
        truck.addPetition(petition);
        if (oldAssignedTruck != null) {
            oldAssignedTruck.deletePetition(petition);
        }
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
