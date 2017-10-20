package model;

import IA.Gasolina.Distribucion;

import java.util.List;

public class Truck {

    private Distribucion origin;
    private List<Trip> trips;

    public void addPetition(Petition petition) {
        boolean found = false;
        for (Trip trip : trips) {
            int i = 0;
            while (i < trip.size() && !found) {
                Petition auxPetition = trip.get(i);
                if (petition.isSameStation(auxPetition)) {
                    trip.add(petition);
                    found = true;
                }
            }
        }

        if (!found) {
            Trip trip = new Trip();
            trip.add(petition);
            trips.add(trip);
        }
    }

    public void deletePetition(Petition petition) {
        boolean found = false;
        int i = 0;
        while (i < trips.size() && !found) {
            Trip trip = trips.get(i);
            if (trip.contains(petition)) {
                trip.remove(petition);
                found = true;
            }
        }
    }
}
