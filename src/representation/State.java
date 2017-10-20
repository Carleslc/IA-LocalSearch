package representation;

import model.Petition;
import model.Truck;
import model.Trip;

import java.util.List;
import java.util.Map;
import java.util.Collections;

public class State {

    private List<Truck> trucks;
    private Map<Petition, Truck> assignments;

    public State() {
        // generación del estado inicial
        Global global = new Global();
        for(Petition petition : global.getAllPetitions()){
            if(!assignments.containsKey(petition)){
                Collections.shuffle(trucks);
                boolean found = false;
                int i = 0;
                while(i < trucks.size() && !found){
                    boolean available = true;
                    int n = trucks.get(i).getTrips().size();
                    if(n == 5){
                        if(trucks.get(i).getTrips().get(n-1).size() == 2) available = false;
                    }
                    if(available){
                        found = true;
                        if(n > 0) --n;
                        if(n == 0){
                            Trip trip = new Trip();
                            trip.add(petition);
                            trucks.get(i).getTrips().add(trip);
                        }
                        else{
                            if(trucks.get(i).getTrips().get(n).size() == 2) n++;
                            trucks.get(i).getTrips().get(n).add(petition);
                        }
                    }
                }
            }
        }
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
