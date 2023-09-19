package com.circuit_builder.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;

public class Component {
    public int x, y; // from bottom left and most bottom left node
    public int width, height; // number of nodes to span
    public String name;
    public Rectangle cachedBounds;
    public int rotation; // 0) UP 1) RIGHT 2) DOWN 3) LEFT
    public Texture texture;
    public Pin[] pins;
    public Board parent;
    public int id;

    public Component(int x, int y, int width, int height, String name, Texture texture, Board parent, int id) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.name = name;
        this.rotation = 0;
        this.texture = texture;
        this.parent = parent;
        this.id = id;
        pins = new Pin[this.width * this.height];
        int c = 0;
        for(int i = x; i < x + width; i++){
            for (int j = y; j < y + height; j++) {
                pins[c] = new Pin(i, j, this);
                c++;
            }
        }
    }

    public void simulate() {}

    public void render(ShapeRenderer sr, SpriteBatch sb, Board parent) {
        sr.begin(ShapeType.Filled);
        sr.setColor(Configuration.default_component_color);
        Rectangle bounds = getBoundingBox(parent);
        sr.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        sr.setColor(Configuration.stud_color);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                float coord_x = j * Configuration.grid_box_width + (x * Configuration.grid_box_width) + parent.x;
                float coord_y = i * Configuration.grid_box_height + (y * Configuration.grid_box_height) + parent.y;
                Rectangle r = new Rectangle(
                    coord_x, coord_y, 
                    Configuration.grid_line_width, 
                    Configuration.grid_line_width);
                sr.circle(r.x, r.y, r.width);
            }
        }
        sr.end();

        sb.begin();
        sb.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        sb.end();
    }

    public void setLocation(int x, int y) {
        this.x = x; this.y = y;
        this.cachedBounds = null;
    }

    public void rotate(int rotation) {
        System.out.println("rotation not implemented");
        if (rotation == this.rotation) return;
        this.rotation = rotation;
        cachedBounds = null;
    }

    // get logical screen space bounding box
    public Rectangle getBoundingBox(Board parent) {
        if (cachedBounds != null)
            return cachedBounds;
        final float o = Configuration.component_overhang;
        Rectangle out = new Rectangle(
            (float) x * Configuration.grid_box_width + parent.x - o, 
            (float) y * Configuration.grid_box_height + parent.y - o, 
            (float) (width - 1) * Configuration.grid_box_width + (2 * o), 
            (float) (height - 1) * Configuration.grid_box_height + (2 * o));
        System.out.println("Recalculate bounds for component");
        cachedBounds = out;
        return out;
    }

    public static void render_sprite_background(ShapeRenderer sr, float x, float y) {
        sr.begin(ShapeType.Filled);
        sr.setColor(Configuration.default_component_color);
        sr.rect(x, y, Configuration.component_sprite_width, Configuration.component_sprite_width);
        sr.end();
    }
}
