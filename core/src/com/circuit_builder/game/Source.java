package com.circuit_builder.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Source extends Component{
    public int x, y;
    public Source(int x, int y) {
        super(x, y, Configuration.battery_width, Configuration.battery_height, "Source");
        this.x = x; this.y = y;
    }
    public void render(ShapeRenderer sr, Board parent) {
        super.render(sr, parent);
    } 
}
