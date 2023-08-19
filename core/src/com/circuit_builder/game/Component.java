package com.circuit_builder.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Component {
    public int coord_x, coord_y; // from bottom left and most bottom left node
    public int width, height; // number of nodes to span
    public abstract void render(ShapeRenderer sr, Board parent);
}
