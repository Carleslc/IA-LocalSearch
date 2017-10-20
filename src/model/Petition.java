package model;

public class Petition {

    private int pendingDays;
    private int coordX;
    private int coordY;

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
}
