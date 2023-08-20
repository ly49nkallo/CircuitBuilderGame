package com.circuit_builder.game;

import java.util.Iterator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Board {
    // GRID DIMENSIONS
    public int width;
    public int height;
    public Color boardColor;
    public Color gridColor;
    public float x, y; // board location
    // 
    public Array<Component> components;
    public Array<Wire> wires;
    public Array<Segment> segments;

    public Rectangle[] vertices;

    public static final Color getDefaultBoardColor = new Color(0.1f, 0.15f, 0.12f, 1f);
    public static final Color getDefaultVertexColor = new Color(Color.GRAY);

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.boardColor = getDefaultBoardColor;
        this.gridColor = Configuration.default_segment_color;
        this.components = new Array<Component>();
        this.vertices = new Rectangle[this.height * this.width];
        this.segments = new Array<Segment>();
    }

    public void constructVertexObjects(){
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                float coord_x = i * Configuration.grid_box_width + x;
                float coord_y = j * Configuration.grid_box_height + y;
                this.vertices[(i * this.width) + j] = new Rectangle(coord_x, coord_y, 
                                                                    Configuration.grid_line_width, 
                                                                    (float) Configuration.grid_line_width);
            }
        }
    }

    public void constructSegments() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (i != this.height - 1) segments.add(new Segment(j, i, j, i + 1));
                if (j != this.width - 1) segments.add(new Segment(j, i, j + 1, i));
            }
        }
    }

    public void renderSegments(ShapeRenderer sr) {
        sr.begin(ShapeType.Filled);
        sr.setColor(gridColor);
        for (Segment s : segments) {
            s.render(sr, this);
        }
        sr.end();
    }

    public void renderVertexObjects(ShapeRenderer sr) {
        sr.begin(ShapeType.Filled);
        sr.setColor(getDefaultVertexColor);
        for (Rectangle v : vertices) {
            sr.circle(v.x, v.y, Configuration.grid_line_width);
        }
        sr.end();
    }
    public Segment getClosestSegment(float ptrX, float ptrY) {
        // @TODO Super inefficient maybe fix later make better code dang it!
        float lowestDist = Float.MAX_VALUE;
        int lowestIdx = segments.size;
        for (int i = 0; i < segments.size; i++) {
            Segment s = segments.get(i);
            float [] coords = s.getScreenSpaceCoordinatesForEndpoints(this.x, this.y);
            float x1 = coords[0]; float y1 = coords[1]; float x2 = coords[2]; float y2 = coords[3];
            float dist = ((x1 - ptrX) * (x1 - ptrX))
                        + ((y1 - ptrY) * (y1 - ptrY))
                        + ((x2 - ptrX) * (x2 - ptrX))
                        + ((y2 - ptrY) * (y2 - ptrY));
            if (dist < lowestDist) {
                lowestDist = dist;
                lowestIdx = i;
            }
        }
        Segment closestSegment = segments.get(lowestIdx);
        return closestSegment;
    }
    //@TODO add wires remove segment behavior
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void addWires(Array<Wire> wires) {
        for (Iterator<Wire> iter = wires.iterator(); iter.hasNext(); ) {
            this.addWire(iter.next());
        }
    }

    public void addWire(Wire wire) {
        this.wires.add(wire);
    }

    public final float[] getGridDimensions() {
        return new float[] {
            (this.width - 1) * Configuration.grid_box_width, 
            (this.height - 1) * Configuration.grid_box_height
        };
    }

    public Rectangle getBoundingBox() {
        float[] grid_dims = getGridDimensions();
        return new Rectangle((float) x, (float) y, (float) grid_dims[0], (float) grid_dims[1]);
    }

    public void renderBackground(ShapeRenderer sr) {
        sr.begin(ShapeType.Filled);
        // draw background
        sr.setColor(this.boardColor);
        sr.rect((float) x, (float) y, getGridDimensions()[0], getGridDimensions()[1]);
        sr.end();
    }

    public void render(ShapeRenderer sr) {
        renderBackground(sr);
        for (Component c: components) {
            c.render(sr, this);
        }
        renderSegments(sr);
        // renderVertexObjects(sr);
    }
}
