package model;

import representation.Global;

import java.util.Iterator;
import java.util.LinkedList;

class Trips extends LinkedList<Trip> {

    @Override
    public boolean add(Trip t) {
        throw new RuntimeException("Use addTripWithPetition instead");
    }

    boolean addTripWithPetition(Petition p) throws RestrictionViolationException {
        if (size() == Global.MAX_TRIPS_PER_TRUCK) {
            throw new RestrictionViolationException("Trips per truck violation");
        } else if (p == null) {
            throw new IllegalArgumentException("Petition is null");
        }
        Trip trip = new Trip();
        trip.addPetition(p);
        return super.add(trip);
    }

    boolean remove(Petition p) {
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
