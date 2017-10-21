package model;

import IA.Gasolina.Distribucion;
import representation.Global;

import java.util.Optional;
import java.util.function.Predicate;

public class Truck {

    private Distribucion origin;
    private Trips trips;

    public Truck(Distribucion origin) {
        this.origin = origin;
        trips = new Trips();
    }

    public boolean hasAssignments() {
        return !trips.isEmpty();
    }

    public boolean isFull() {
        return trips.isFull();
    }

    public int getDistanceTraveled() { // In km
        return trips.parallelStream().mapToInt(trip -> trip.getDistanceTraveled(origin)).sum();
    }

    public void addPetition(Petition petition) throws RestrictionViolationException {
        // Check if there is already a trip with a petition in the same station that can afford a new petition
        // or it is full but at least one of its petitions are from another station
        Optional<Trip> tripWithTheSameStation = trips.stream()
                .filter(sameStationAndWithCapacity(petition))
                .findFirst();

        if (tripWithTheSameStation.isPresent()) {
            // Completing petitions of the same station with a single trip optimizes distance
            Trip trip = tripWithTheSameStation.get();
            if (!trip.isFull()) {
                // If this trip is not at full capacity, add to this trip
                trip.addPetition(petition, this);
            } else {
                // Trip is full so we need to reallocate a petition from different station to another trip
                Petition distinct = trip.findFirstPetitionWithDistinctStationThan(petition).get();
                addPetitionToFirstAvailableTrip(distinct);
                trip.remove(distinct);
                // Then add the original petition to this trip
                trip.addPetition(petition, this);
            }
        } else {
            // There is no current available trip that travels to the same station, so add to the first available trip
            addPetitionToFirstAvailableTrip(petition);
        }
    }

    private void addPetitionToFirstAvailableTrip(Petition petition) throws RestrictionViolationException {
        Optional<Trip> tripNotFull = trips.stream().filter(trip -> !trip.isFull()).findFirst();
        if (tripNotFull.isPresent()) {
            tripNotFull.get().addPetition(petition, this);
        } else {
            // All current trips are full so create a new one
            trips.addTripWithPetition(petition, this);
        }
    }

    private Predicate<Trip> sameStationAndWithCapacity(Petition petition) {
        return trip -> {
            int sameCount = (int) trip.parallelStream()
                    .map(current -> current.isSameStation(petition))
                    .filter(same -> same).count();
            return sameCount >= 1 && sameCount < Global.ASSIGNATIONS_PER_TRIP;
        };
    }

    public void deletePetition(Petition petition) {
        trips.remove(petition);
    }

    public Distribucion getOrigin() {
        return origin;
    }

    public Trips getTrips() {
        return trips;
    }

    @Override
    public String toString() {
        return "Truck [" + getDistanceTraveled() + " km] (" + origin.getCoordX() + ',' + origin.getCoordY() + "): " + trips.toString();
    }
}
