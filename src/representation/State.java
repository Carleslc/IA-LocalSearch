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
        trucks = new ArrayList<>();
        for (Distribucion origin : distributionCenters) {
            trucks.add(new Truck(origin));
        }
        trucks = Collections.unmodifiableList(trucks);
        //initialState();
    }

    public State(State copyState) {
        assignments = new HashMap<>(copyState.assignments.size());
        trucks = new ArrayList<>(Collections.nCopies(copyState.trucks.size(), null));
        for (Map.Entry<Petition, Truck> assignment : copyState.assignments.entrySet()) {
            Truck original = assignment.getValue();
            if (trucks.get(original.getId()) == null) {
                trucks.set(original.getId(), new Truck(original));
            }
            assignments.put(assignment.getKey(), trucks.get(original.getId()));
        }
        List<Truck> trucksNotAssigned = copyState.trucks.stream()
                .filter(truck -> !truck.hasAssignments())
                .map(Truck::new)
                .collect(Collectors.toList());
        trucksNotAssigned.forEach(truck -> trucks.set(truck.getId(), truck));
        trucks = Collections.unmodifiableList(trucks);
    }

    private void initialState() {
        int seed = Global.SEED;
        for (Petition petition : Global.getInstance().getAllPetitions()) {
            List<Truck> randomTrucks = new ArrayList<>(trucks);
            Collections.shuffle(randomTrucks, new Random(seed++));
            Optional<Truck> firstTruckAvailable = randomTrucks.parallelStream().filter(truck -> !truck.isFull()).findFirst();
            if (firstTruckAvailable.isPresent()) {
                try {
                    assignTruck(petition, firstTruckAvailable.get().getId());
                } catch (RestrictionViolationException ignore) {
                }
            }
        }
    }

    public List<Truck> getTrucks() {
        return trucks;
    }

    public Map<Petition, Truck> getAssignments() {
        return Collections.unmodifiableMap(assignments);
    }

    public List<Petition> getUnassignedPetitions() {
        return Global.getInstance().getAllPetitions().stream().filter(p -> !assignments.containsKey(p)).collect(Collectors.toList());
    }

    public int getTravelledDistance() {
        return trucks.stream().mapToInt(Truck::getTravelledDistance).sum();
    }

    private void validateTruck(int truckId) {
        if (truckId < 0 || truckId >= trucks.size()) {
            throw new IllegalArgumentException("Invalid truck");
        }
    }

    public void assignTruck(Petition petition, int truckId) throws RestrictionViolationException {
        validateTruck(truckId);
        Truck oldAssignedTruck = assignments.get(petition);
        boolean newPetition = oldAssignedTruck == null;
        if (!newPetition && truckId == oldAssignedTruck.getId()) {
            throw new RestrictionViolationException("Petition already assigned to that truck");
        }
        Truck truck = trucks.get(truckId);
        truck.addPetition(petition);
        if (!newPetition) {
            oldAssignedTruck.deletePetition(petition);
        }
        assignments.put(petition, truck);
    }

    public void assignPetition(int truckId, Petition oldPetition, Petition newPetition) throws RestrictionViolationException {
        validateTruck(truckId);
        Truck truck = trucks.get(truckId);
        if (!isAssignedToTruck(oldPetition, truck)) {
            throw new IllegalArgumentException("This petition is not assigned to that truck");
        }
        try {
            unassign(oldPetition);
            assignTruck(newPetition, truckId);
        } catch (RestrictionViolationException e) {
            assignTruck(oldPetition, truckId);
            throw e;
        }
    }

    public boolean isAssignedToTruck(Petition petition, Truck truck) {
        return truck.equals(assignments.get(petition));
    }

    public boolean isAssigned(Petition petition) {
        return assignments.containsKey(petition);
    }

    public void unassign(Petition petition) {
        if (!isAssigned(petition)) {
            throw new IllegalArgumentException("Petition not assigned");
        }
        Truck assignedTruck = assignments.remove(petition);
        assignedTruck.deletePetition(petition);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        trucks.forEach(truck -> builder.append(truck).append('\n'));
        return builder.toString();
    }

}
