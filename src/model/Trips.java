package model;

import representation.Global;

import java.util.Iterator;
import java.util.LinkedList;

public class Trips extends LinkedList<Trip> {

    public Trips() { }

    public Trips(Trips copy) {
        for (Trip copyTrip : copy) {
            super.add(new Trip(copyTrip));
        }
    }

    public boolean isAtMaximumSize() {
        return size() == Global.MAX_TRIPS_PER_TRUCK;
    }

    public boolean isFull() {
        return isAtMaximumSize() && parallelStream().allMatch(Trip::isFull);
    }

    @Override
    public boolean add(Trip t) {
        throw new RuntimeException("Use addTripWithPetition instead");
    }

    public void addTripWithPetition(Petition p, Truck truck) throws RestrictionViolationException {
        if (p == null) {
            throw new IllegalArgumentException("Petition is null");
        } else if (isAtMaximumSize()) {
            throw new RestrictionViolationException("Trips per truck violation");
        }
        Trip trip = new Trip();
        trip.addPetition(p, truck);
        super.add(trip);
        if (truck.getDistanceTraveled() > Global.MAX_KM_PER_DAY) {
            removeLast();
            throw new RestrictionViolationException("Truck kilometers violation");
        }
    }

    public boolean remove(Petition p) {
        Iterator<Trip> iterator = iterator();
        while (iterator.hasNext()) {
            Trip tripWithPetitions = iterator.next();
            if (tripWithPetitions.remove(p)) {
                if (tripWithPetitions.isEmpty()) {
                    iterator.remove();
                }
                return true;
            }
        }
        return false;
    }
}
