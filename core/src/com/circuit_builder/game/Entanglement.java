package com.circuit_builder.game;

import com.badlogic.gdx.utils.Array;

public class Entanglement {
    Array<Wire> wires;
    Array<Pin> pins;

    public Entanglement(Array<Wire> wires, Array<Pin> pins) {
        this.wires = wires;
        this.pins = pins;
    }

    public boolean shouldBeActive() {
        for (Pin p : this.pins) {
            if (p.active) return true;
        }
        return false;
    }

    public String repr() {
        String out = "";
        out += "Entanglement:\n";
        out += "  # of Wires: " + this.wires.size + '\n';
        out += "  # of Pins: " + this.pins.size;
        return out;

    }
}
