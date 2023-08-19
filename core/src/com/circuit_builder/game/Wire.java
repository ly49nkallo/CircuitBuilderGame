package com.circuit_builder.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;

public class Wire {
    public int color_id; // 1) red; 2) green; 3) blue; 4) black; 5) white); 6) orange
    public int x1, y1, x2, y2;
    public boolean active;

    public Wire(int color_id, int x1, int y1, int x2, int y2) {
        this.color_id = color_id;
        this.x1 = x1; this.x2 = x2; 
        this.y1 = y1; this.y2 = y2;
        // ensure wire is either horizontal, vertical, or at a 45 degree angle

        active = false;
    }

    public static Color getColor(int color_id) {
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

    public void render(ShapeRenderer sr, Board parent, float board_x, float board_y) {
        sr.begin(ShapeType.Filled);
        sr.setColor(getColor(this.color_id));
        sr.rectLine((float) x1 * (float) Configuration.grid_box_width + board_x,
                    (float) y1 * (float) Configuration.grid_box_height + board_y, 
                    (float) x2 * (float) Configuration.grid_box_width + board_x, 
                    (float) y2 * (float) Configuration.grid_box_height + board_y, 
                    Configuration.grid_line_width);
        sr.circle((float) x1 * (float) Configuration.grid_box_width + board_x,
                    (float) y1 * (float) Configuration.grid_box_height + board_y,
                    Configuration.grid_line_width);
        sr.circle((float) x2 * (float) Configuration.grid_box_width + board_x,
                    (float) y2 * (float) Configuration.grid_box_height + board_y,
                    Configuration.grid_line_width);
        sr.end();
    }
}
