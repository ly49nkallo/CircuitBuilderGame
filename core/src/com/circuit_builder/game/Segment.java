package com.circuit_builder.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;

public class Segment {
    int x1, y1, x2, y2;
    public int color_id;
    private float[] cachedScreenSpaceCoordinates;

    public Segment(int x1, int y1, int x2, int y2) {
        this.x1 = x1; this.y1 = y1; 
        this.x2 = x2; this.y2 = y2;
        color_id = 0;
    }
    public Color getColor() {
        return Configuration.default_segment_color;
    }
    public Color getSelectedColor() {
        return Configuration.default_segment_selected_color;
    }
    /* returns float[4] where the elements are x1, y1, x2, y2 screen space coordinates */
    public float[] getScreenSpaceCoordinatesForEndpoints(float board_x, float board_y) {
        if (cachedScreenSpaceCoordinates != null)
            return cachedScreenSpaceCoordinates;
        float[] out = new float[] {
            (float) x1 * (float) Configuration.grid_box_width + board_x,
            (float) y1 * (float) Configuration.grid_box_height + board_y,
            (float) x2 * (float) Configuration.grid_box_width + board_x, 
            (float) y2 * (float) Configuration.grid_box_height + board_y};
        cachedScreenSpaceCoordinates = out;
        return out;
    }

    public void render(ShapeRenderer sr, Board parent) {
        sr.setColor(getColor());
        float[] coords = getScreenSpaceCoordinatesForEndpoints(parent.x, parent.y);
        sr.rectLine(coords[0], coords[1],
                    coords[2], coords[3],
                    Configuration.grid_line_width);
    }

    public void selectedRender(ShapeRenderer sr, Board parent) {
        sr.begin(ShapeType.Filled);
        sr.setColor(getSelectedColor());
        float[] coords = getScreenSpaceCoordinatesForEndpoints(parent.x, parent.y);
        sr.rectLine(coords[0], coords[1],
                    coords[2], coords[3],
                    Configuration.grid_line_width);
        sr.end();
    }

    public int[] getEndpoints() {
        return new int[] {x1, y1, x2, y2};
    }
}
