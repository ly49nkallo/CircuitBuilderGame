package com.circuit_builder.game;

public class AndGate extends Component{
    public AndGate(int x, int y, Board parent) {
        super(x, y, Configuration.and_gate_width, Configuration.and_gate_height, "AND", Source.s_texture, parent, 2);
    }
}
