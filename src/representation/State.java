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

    private boolean statisfiesAllRestrictions() {
        return true; // TODO: stub
    }

    public void changeTruck(Petition petition, Truck truckOne, Truck truckTwo) { // move(Pi, Tj, Tk)
        if (statisfiesAllRestrictions()) {
            // TODO: update trucks
            assignments.put(petition, truckTwo);
        } else {
            System.err.println("The application of this operator does not satisfy all restrictions");
        }
    }

    public void assignTruck(Petition petition, Truck truck) { // add(Pi, Tj)
        if (statisfiesAllRestrictions()) {
            assignments.put(petition, truck);
        } else {
            System.err.println("The application of this operator does not satisfy all restrictions");
        }
    }

}
