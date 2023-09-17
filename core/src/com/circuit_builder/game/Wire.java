package com.circuit_builder.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Wire extends Segment{
    public int color_id; // 1) red; 2) green; 3) blue; 4) black; 5) white); 6) orange
    public boolean active;

    public Wire(int color_id, int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
        this.color_id = color_id;
    }

    public Color getActiveColor() {
        Color c = getColor(false);
        float bi = 0.3f;
        return new Color(c.r + bi, c.g + bi, c.b + bi, 1f);
    }
    public Color getNotActiveColor() {
        Color c = getColor(false);
        float bi = 0.3f;
        return new Color(c.r - bi, c.g - bi, c.b - bi, 1f);
    }

    @Override
    public Color getColor(boolean simulationMode) {
        if (simulationMode && active)
            return getActiveColor();
        else if (simulationMode && !active)
            return getNotActiveColor();
        else
            return Configuration.color_map(this.color_id);
    }

    @Override
    public Color getSelectedColor() {
        Color c = getColor(false);
        float brightness = Configuration.brightness_increase_wire;
        return new Color(c.r + brightness, c.g + brightness, c.b + brightness, c.a);
    }
}
