package com.circuit_builder.game;

import java.util.Iterator;
import java.util.Stack;
import java.util.Queue;

import com.badlogic.gdx.utils.Array;
import com.circuit_builder.tree_utils.Node;
import com.circuit_builder.tree_utils.Series;
import com.circuit_builder.tree_utils.Parellel;

import java.util.Arrays;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    public Array<Segment> segments;

    public Vertex[] vertices;

    public static final Color getDefaultBoardColor = new Color(0.1f, 0.15f, 0.12f, 1f);
    public static final Color getDefaultVertexColor = new Color(Color.GRAY);

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.boardColor = getDefaultBoardColor;
        this.gridColor = Configuration.default_segment_color;
        this.components = new Array<Component>();
        this.vertices = new Vertex[this.height * this.width];
        this.segments = new Array<Segment>();
    }

    public void constructVertexObjects(){
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                float coord_x = j * Configuration.grid_box_width + x;
                float coord_y = i * Configuration.grid_box_height + y;
                Rectangle bounds = new Rectangle(coord_x, coord_y, 
                                                        Configuration.grid_line_width, 
                                                        (float) Configuration.grid_line_width);
                this.vertices[(i * this.width) + j] = new Vertex(j, i);
                this.vertices[(i * this.width) + j].bounds = bounds;
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
        for (Vertex v : vertices) {
            sr.circle(v.bounds.x, v.bounds.y, Configuration.grid_line_width);
        }
        sr.end();
    }

    public boolean hasWire(int[] endpoints) {
        int[] endpoints_reversed = new int[] {endpoints[3], endpoints[2], endpoints[1], endpoints[0]};
        for (Segment w : segments) {
            if (w instanceof Wire && (Arrays.equals(w.getEndpoints(), endpoints) || Arrays.equals(w.getEndpoints(), endpoints_reversed))) {
                return true;
            }
        }
        return false;
    }

    public Segment getSegment(int[] endpoints) {
        int[] endpoints_reversed = new int[] {endpoints[3], endpoints[2], endpoints[1], endpoints[0]};
        for (Segment s : segments) {
            if (Arrays.equals(s.getEndpoints(), endpoints) || Arrays.equals(s.getEndpoints(), endpoints_reversed)){
                return s;
            }
        }
        return null;
    }

    public Array<Segment> getSegmentsFromCoordinate(int x, int y) {
        Array<Segment> out = new Array<Segment>(4);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((i != 0 && j != 0) || (i == 0 && j == 0)) continue;
                int[] endpoints = new int[] {x, y, x + i, y + j};
                Segment s = this.getSegment(endpoints);
                if (s != null) {
                    out.add(s);
                }
                else {
                    endpoints = new int[] {x + i, y + j, x, y};
                    s = this.getSegment(endpoints);
                    if (s != null) {
                        out.add(s);
                    }
                }
            }
        }
        return out;
    }

    public Array<Wire> getWiresFromCoordinate(int x, int y) {
        Array<Segment> segments = getSegmentsFromCoordinate(x, y);
        Array<Wire> out = new Array<Wire>(4);
        for (Segment s : segments) {
            if (s instanceof Wire)
                out.add((Wire) s);
        }
        System.out.println("For cordinate "+x+", "+y+", found "+out.size+" wires");
        return out;
    }
    public boolean hasComponent(int i, int j) {
        for (Component c : components) {
            if (c.x <= j && c.x + c.width -1 >= j && c.y <= i && c.y + c.height -1 >= i) {
                return true;
            }
        }
        return false;
    }

    public Component getComponent(int i, int j) {
        for (Component c : components) {
            if (c.x <= j && c.x + c.width -1 >= j && c.y <= i && c.y + c.height -1 >= i) {
                return c;
            }
        }
        return null;
    }

    public boolean addComponent(Component component) {
        if (component.x + component.width > this.width || component.y + component.height > this.height)
            return false;
        for (int i = 0; i < component.height; i++) {
            for (int j = 0; j < component.width; j++){
                if (hasComponent(i + component.y, j + component.x))
                    return false;
            }
        }
        for (int i = 0; i < component.height; i++) {
            for (int j = 0; j < component.width; j++){
                int cx = j + component.x;
                int cy = i + component.y;
                Segment s1 = null;
                Segment s2 = null;
                if (i != component.height - 1) s1 = getSegment(new int[] {cx, cy, cx, cy + 1});
                if (j != component.width - 1) s2 = getSegment(new int[] {cx, cy, cx + 1, cy});
                if (s1 != null) segments.removeValue(s1, false);
                if (s2 != null) segments.removeValue(s2, false);
            }
        }
        this.components.add(component);
        return true;
    }

    public boolean removeComponent(Component component) {
        if (!this.components.contains(component, false))
            return false;
        for (int i = 0; i < component.height; i++) {
            for (int j = 0; j < component.width; j++){
                int cx = j + component.x;
                int cy = i + component.y;
                Segment s1 = null;
                Segment s2 = null;
                if (i != component.height - 1) s1 = new Segment(cx, cy, cx, cy + 1);
                if (j != component.width - 1) s2 = new Segment(cx, cy, cx + 1, cy);
                if (s1 != null) segments.add(s1);
                if (s2 != null) segments.add(s2);
            }
        }
        this.components.removeValue(component, false);
        return true;
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
        if (lowestIdx == segments.size) return null;
        Segment closestSegment = segments.get(lowestIdx);
        return closestSegment;
    }

    public Vertex getClosestVertex(float ptrX, float ptrY) {
        float lowestDist = Float.MAX_VALUE;
        int lowestIdx = vertices.length;
        for (int i = 0; i < vertices.length; i++) {
            Vertex v = vertices[i];
            float dist = ((v.bounds.x - ptrX) * (v.bounds.x - ptrX)) + ((v.bounds.y - ptrY) * (v.bounds.y - ptrY));
            if (dist < lowestDist) {
                lowestDist = dist;
                lowestIdx = i;
            }
        }
        if (lowestIdx == vertices.length) return null;
        Vertex closestVertex = vertices[lowestIdx];
        return closestVertex;
    }
    //@TODO add wires remove segment behavior
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
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

    public void render(ShapeRenderer sr, SpriteBatch sb) {
        renderBackground(sr);
        renderSegments(sr);
        for (Component c: components) {
            c.render(sr, sb, this);
        }
        // renderVertexObjects(sr);
    }

    public void simulate() {
    }

    public Node compile() {
        // for each wire
        //   if wire not in accounted for
        //      perform DFS on the wire to find all components 
        Node root = new Series();
        Array<Segment> accounted_for = new Array<Segment>(this.height * this.width);
        // Array<Array<Segment>> logical_sections = new Array<Array<Segment>>();
        Array<Array<Component>> entangled_components = new Array<Array<Component>>();
        Array<Array<Segment>> entangled_segments = new Array<Array<Segment>>();
        for (int i = 0; i < this.segments.size; i++) {
            Segment seg = this.segments.get(i);
            if (seg instanceof Wire && !accounted_for.contains(seg, false)) {
                Array<Segment> new_entanglement = new Array<Segment>();
                //dfs
                Stack<Wire> stack = new Stack<Wire>();
                stack.push((Wire) seg);
                while (!stack.empty()) {
                    Segment v = stack.pop();
                    int[] endpoints = v.getEndpoints();
                    if (!accounted_for.contains(v, false)){
                        accounted_for.add(v);
                        new_entanglement.add(v);
                        Array<Wire> attached1 = getWiresFromCoordinate(endpoints[0], endpoints[1]);
                        Array<Wire> attached2 = getWiresFromCoordinate(endpoints[2], endpoints[3]);
                        Array<Wire> attached = new Array<Wire>(attached1.size + attached2.size);
                        attached.addAll(attached1);
                        attached.addAll(attached2);
                        for (Wire s1 : attached) {
                            stack.push(s1);
                        }
                    }
                }
                entangled_segments.add(new_entanglement);
            }
        }
        System.out.println("accounted_for.size = "+accounted_for.size);
        System.out.println();
        System.out.println("Entangled Wires");
        System.out.println("===============");
        for (Array<Segment> entanglement : entangled_segments) {
            System.out.println("Entaglement of size "+entanglement.size);
        }
        System.out.println();
        return new Series();
    }
}
