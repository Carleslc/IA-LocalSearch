package model;

import IA.Gasolina.Distribucion;

public class Petition {

    private int pendingDays;
    private int coordX, coordY;

    public int getPendingDays() {
        return pendingDays;
    }

    public Petition setPendingDays(int pendingDays) {
        this.pendingDays = pendingDays;
        return this;
    }

    public int getCoordX() {
        return coordX;
    }

    public Petition setCoordX(int coordX) {
        this.coordX = coordX;
        return this;
    }

    public int getCoordY() {
        return coordY;
    }

    public Petition setCoordY(int coordY) {
        this.coordY = coordY;
        return this;
    }

    public int getDistanceTo(int coordX, int coordY) {
        return Math.abs(this.coordX - coordX) + Math.abs(this.coordY - coordY); // Manhattan distance
    }

    public int getDistanceTo(Distribucion d) {
        return getDistanceTo(d.getCoordX(), d.getCoordY());
    }

    public boolean isSameStation(Petition petition) {
        return this.coordX == petition.coordX && this.coordY == petition.coordY;
    }

}
