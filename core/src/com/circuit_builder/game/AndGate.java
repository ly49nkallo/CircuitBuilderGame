package com.circuit_builder.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

public class AndGate extends Component{
    public static Texture s_texture = new Texture(Gdx.files.internal("assets/AND.png"));

    public AndGate(int x, int y, Board parent) {
        super(x, y, Configuration.and_gate_width, Configuration.and_gate_height, "AND", AndGate.s_texture, parent, 2);
    }

    @Override
    public void simulate() {
        if (this.pins[0].active && this.pins[2].active) {
            this.pins[1].active = true;
            this.pins[3].active = true;
        }
        else {
            this.pins[1].active = false;
            this.pins[3].active = false;
        }
    }
}
