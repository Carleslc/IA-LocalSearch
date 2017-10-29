package representation;

import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolinera;
import IA.Gasolina.Gasolineras;
import model.Petition;

import java.util.ArrayList;
import java.util.List;

public final class Global {

    public static final int SEED = 1234; // new Random().nextInt()
    private static final int DISTRIBUTION_CENTERS = 10;
    private static final int FUEL_STATIONS = 100;
    private static final int MAX_HOURS_PER_DAY = 7; // hours
    private static final int TRUCK_SPEED = 80; // km/h
    public static final int MAX_KM_PER_DAY = MAX_HOURS_PER_DAY * TRUCK_SPEED;
    public static final int TRUCKS_PER_DISTRIBUTION_CENTER = 1;
    public static final int KM_COST = 2;
    public static final int PETITION_SERVED_PROFIT = 1000;
    public static final int MAX_TRIPS_PER_TRUCK = 5;
    public static final int ASSIGNATIONS_PER_TRIP = 2; // number of tanks a truck can carry

    private static final Global INSTANCE = new Global();

    private CentrosDistribucion distributionCenters;
    private List<Petition> allPetitions;

    private Global() {
        distributionCenters = new CentrosDistribucion(DISTRIBUTION_CENTERS, TRUCKS_PER_DISTRIBUTION_CENTER, SEED);
        Gasolineras fuelStations = new Gasolineras(FUEL_STATIONS, SEED);
        allPetitions = new ArrayList<>();

        for (Gasolinera station : fuelStations) {
            for (int pendingDays : station.getPeticiones()) {
                Petition petition = new Petition();
                petition.setCoordX(station.getCoordX())
                        .setCoordY(station.getCoordY())
                        .setPendingDays(pendingDays);
                allPetitions.add(petition);
            }
        }
    }

    public CentrosDistribucion getDistributionCenters() {
        return distributionCenters;
    }

    public List<Petition> getAllPetitions() {
        return allPetitions;
    }

    public static Global getInstance() {
        return INSTANCE;
    }
}
