package model;

import IA.Gasolina.Distribucion;
import representation.Global;

import java.util.Optional;
import java.util.function.Predicate;

public class Truck {

    private static int idGenerator = 0;

    private final int id;
    private Distribucion origin;
    private Trips trips;

    public Truck(Distribucion origin) {
        this.id = idGenerator++;
        this.origin = origin;
        trips = new Trips();
    }

    public Truck(Truck copy) {
        this.id = copy.id;
        this.origin = copy.origin;
        trips = new Trips(copy.trips);
    }

    public int getId() {
        return id;
    }

    public boolean hasAssignments() {
        return !trips.isEmpty();
    }

    public boolean isFull() {
        return trips.isFull();
    }

    public int getTravelledDistance() { // In km
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
                // Move the petition with different station to another trip
                trip.remove(distinct);
                try {
                    addPetitionToFirstAvailableTrip(distinct, t -> !t.equals(trip) && !t.isFull());
                } catch (RestrictionViolationException cannotMove) {
                    // Petition cannot be moved to another trip, so put it again to this trip
                    trip.addPetition(distinct, this);
                    throw cannotMove;
                }
                // Then add the original petition to this trip
                trip.addPetition(petition, this);
            }
        } else {
            // There is no current available trip that travels to the same station, so add to the first available trip
            addPetitionToFirstAvailableTrip(petition, t -> !t.isFull());
        }
    }

    private void addPetitionToFirstAvailableTrip(Petition petition, Predicate<Trip> notFullFunction) throws RestrictionViolationException {
        Optional<Trip> tripNotFull = trips.stream().filter(notFullFunction).findFirst();
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
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() && id == ((Truck) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Truck [" + getTravelledDistance() + " km] (" + origin.getCoordX() + ',' + origin.getCoordY() + "): " + trips.toString();
    }
}
