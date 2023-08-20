package com.circuit_builder.game;

public class Segment {
    int x1, y1, x2, y2;

    public Segment(int x1, int y1, int x2, int y2) {
        this.x1 = x1; this.y1 = y1; 
        this.x2 = x2; this.y2 = y2;
    }

    /* returns float[4] where the elements are x1, y1, x2, y2 screen space coordinates */
    public float[] getScreenSpaceCoordinatesForEndpoints(float board_x, float board_y) {
        return new float[] {
            (float) x1 * (float) Configuration.grid_box_width + board_x,
            (float) y1 * (float) Configuration.grid_box_height + board_y,
            (float) x2 * (float) Configuration.grid_box_width + board_x, 
            (float) y2 * (float) Configuration.grid_box_height + board_y
        };
    }
}
