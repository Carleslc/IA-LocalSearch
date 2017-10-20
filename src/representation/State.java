package representation;

import model.Petition;
import model.Truck;

import java.util.List;
import java.util.Map;

public class State {

    private List<Truck> trucks;
    private Map<Petition, Truck> assignments;

    public State() {
        // generación del estado inicial
    }

    public List<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
    }

    public Map<Petition, Truck> getAssignments() {
        return assignments;
    }

    public void setAssignments(Map<Petition, Truck> assignments) {
        this.assignments = assignments;
    }

    public void changeTruck(Petition petition, Truck truckOne, Truck truckTwo) { // move(Pi, Tj, Tk)
        truckTwo.deletePetition(petition);
        truckOne.addPetition(petition);
    }

    public void assignTruck(Petition petition, Truck truck) { // add(Pi, Tj)
        truck.addPetition(petition);
    }

}
