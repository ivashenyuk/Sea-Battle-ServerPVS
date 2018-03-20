package com.company.serverPVP;

public class Ship {
    public Coord coord;
    public boolean isHere;
    public boolean slip;
    public boolean beaten;

    public Ship() {
        this.isHere = false;
        this.coord = new Coord(0, 0);
        this.slip = false;
        this.beaten = false;
        this.slip = false;
    }
}
