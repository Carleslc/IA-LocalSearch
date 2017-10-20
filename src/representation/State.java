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
        Collections.shuffle(trucks);
        trucks = Collections.unmodifiableList(trucks);

        // TODO generación del estado inicial
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

}
