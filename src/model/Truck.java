package model;

import IA.Gasolina.Distribucion;

import java.util.List;

public class Truck {

    private Distribucion origin;
    private List<Trip> trips;

    public Distribucion getOrigin() {
        return origin;
    }

    public void setOrigin(Distribucion origin) {
        this.origin = origin;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }
}
