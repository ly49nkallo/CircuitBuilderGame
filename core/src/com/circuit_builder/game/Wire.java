package com.circuit_builder.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;

public class Wire extends Segment{
    public int color_id; // 1) red; 2) green; 3) blue; 4) black; 5) white); 6) orange
    public boolean active;

    public Wire(int color_id, int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
        this.color_id = color_id;

        // ensure wire is either horizontal, vertical, or at a 45 degree angle

        active = false;
    }

    @Override
    public Color getColor() {
        switch (color_id) {
            case 1:
                return Color.RED;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.BLACK;
            case 5:
                return Color.WHITE;
            case 6:
                return Color.ORANGE;
            default:
                return Color.PURPLE;
        }
    }

    @Override
    public Color getSelectedColor() {
        Color c = getColor();
        float brightness = Configuration.brightness_increase_wire;
        return new Color(c.r + brightness, c.g + brightness, c.b + brightness, c.a);
    }
}
