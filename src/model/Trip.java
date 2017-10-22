package model;

import IA.Gasolina.Distribucion;
import representation.Global;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

public class Trip extends LinkedList<Petition> {

    public Trip() { }

    public Trip(Trip copy) {
        super(copy);
    }

    @Override
    public boolean add(Petition p) {
        throw new RuntimeException("Use addPetition instead");
    }

    public void addPetition(Petition p, Truck truck) throws RestrictionViolationException {
        if (p == null) {
            throw new IllegalArgumentException("Petition is null");
        } else if (isFull()) {
            throw new RestrictionViolationException("Trip assignations violation");
        }
        super.add(p);
        if (truck.getTravelledDistance() > Global.MAX_KM_PER_DAY) {
            removeLast();
            throw new RestrictionViolationException("Truck kilometers violation");
        }
    }

    public boolean isFull() {
        return size() == Global.ASSIGNATIONS_PER_TRIP;
    }

    public int getTravelledDistance(Distribucion origin) {
        int km = 0;
        Iterator<Petition> iterator = iterator();
        if (iterator.hasNext()) {
            Petition prev = iterator.next();
            km += prev.getDistanceTo(origin);
            while (iterator.hasNext()) {
                Petition next = iterator.next();
                km += prev.getDistanceTo(next);
                prev = next;
            }
            km += prev.getDistanceTo(origin);
        }
        return km;
    }

    Optional<Petition> findFirstPetitionWithDistinctStationThan(Petition petition) {
        return stream().filter(p -> !p.isSameStation(petition)).findFirst();
    }

    @Override
    public String toString() {
        return "Trip " + super.toString();
    }
}
