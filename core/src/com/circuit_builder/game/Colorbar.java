package com.circuit_builder.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Colorbar {
    public Rectangle[] boxes;
    public float x, y; 

    public Colorbar() {
        boxes = new Rectangle[Configuration.number_of_colors];
    }

    public void setLocation(float x, float y) {
        this.x = x; this.y = y;
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new Rectangle(
                x 
                + (i * Configuration.color_box_side_length) 
                + (i * Configuration.space_between_boxes), 
                y, 
                Configuration.color_box_side_length, 
                Configuration.color_box_side_length);
        }
    }

    public void render(ShapeRenderer sr, int selected_color) {
        sr.begin(ShapeType.Filled);
        Rectangle selectedBox = boxes[selected_color-1];
        sr.setColor(Color.WHITE);
        float sl = (Configuration.color_box_selected_expand - Configuration.color_box_side_length) / 2;
        sr.rect(
            selectedBox.x - sl,
            selectedBox.y - sl,
            Configuration.color_box_selected_expand,
            Configuration.color_box_selected_expand);
        int i = 1;
        for (Rectangle box : boxes) {
            sr.setColor(Configuration.color_map(i));
            sr.rect(box.x, box.y, box.width, box.height);
            i++;
        }
        sr.end();
    }

    public static final float getWidth = (Configuration.number_of_colors - 1f) * Configuration.space_between_boxes
            + (Configuration.number_of_colors * Configuration.color_box_side_length);
    
    public static final float getHeight = Configuration.color_box_side_length;

    /* Logical bounds of entire color bar */
    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, getWidth, getHeight);
    }

}
