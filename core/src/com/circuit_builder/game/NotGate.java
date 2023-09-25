package com.circuit_builder.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

public class NotGate extends Component{
    public static Texture s_texture = new Texture(Gdx.files.internal("assets/NOT.png"));

    public NotGate(int x, int y, Board parent) {
        super(x, y, Configuration.not_gate_width, Configuration.not_gate_height, "NOT", NotGate.s_texture, parent, 3);
        this.pins[1].mutable = false;
    }

    @Override
    public void simulate() {
         if (this.pins[0].active && true) {
            this.pins[1].active = false;
        }
        else {
            this.pins[1].active = true;
        }

    }
}
