package com.circuit_builder.game;

public class Pin {
    public int x, y; // from bottom left of board
    public Component parent;
    public boolean active;

    public Pin(int x, int y, Component parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.active = false;
    }

}
