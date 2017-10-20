package model;

import representation.Global;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

public class Trip extends LinkedList<Petition> {

    @Override
    public boolean add(Petition p) {
        throw new RuntimeException("Use addPetition instead for restriction validation");
    }

    boolean addPetition(Petition p) throws RestrictionViolationException {
        if (isFull()) {
            throw new RestrictionViolationException("Trip assignations violation");
        } else if (p == null) {
            throw new IllegalArgumentException("Petition is null");
        }
        return super.add(p);
    }

    boolean isFull() {
        return size() == Global.ASSIGNATIONS_PER_TRIP;
    }

    Optional<Petition> findFirstPetitionWithDistinctStationThan(Petition petition) {
        return stream().filter(p -> !p.isSameStation(petition)).findFirst();
    }
}
