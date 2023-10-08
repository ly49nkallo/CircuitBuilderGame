package com.circuit_builder.game;

import java.util.Stack;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.Arrays;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Gdx;

public class Board implements Serializable {
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

    public void save(FileHandle handle) {
        // file format: 
        //   (2 bytes) "TY", 
        //   (2 bytes) 0
        //   (1 bytes) board.width
        //   (1 bytes) board.height
        //   (1 byte) 0
        //   (2 bytes) # of Wires, 
        //   For each wire
        //   (1 byte) wire.color_id
        //   (2 bytes) wire.x1
        //   (2 bytes) wire.y1
        //   (2 bytes) wire.x2
        //   (2 bytes) wire.y2
        //   (2 bytes) # of Components
        //   For each component
        //   (1 byte) component.id
        //   (2 bytes) component.x
        //   (2 bytes) component.y
        //   (2 bytes) component.width
        //   (2 bytes) component.height
        byte[] data = new byte[11 + (this.segments.size * 9) + (this.components.size * 9)];
        System.out.println("Components size: " + this.components.size);
        System.out.println("Segments size: " + this.segments.size);
        System.out.println("Total size: " + data.length);
        data[0] = (byte) 'T';
        data[1] = (byte) 'Y';
        data[2] = 0;
        data[3] = 0;
        data[4] = (byte) this.width;
        data[5] = (byte) this.height;
        data[6] = 0;
        data[7] = (byte) (this.segments.size >> 8);
        data[8] = (byte) (this.segments.size & 0xff);
        for (int i = 0; i < this.segments.size; i++) {
            Segment seg = this.segments.get(i);
            if (seg instanceof Wire) {
                data[9 + (i * 9)] = (byte)((Wire) seg).color_id;
            }
            else {
                data[9 + (i * 9)] = (byte) seg.color_id;
            }
            int x1 = this.segments.get(i).x1;
            int y1 = this.segments.get(i).y1;
            int x2 = this.segments.get(i).x2;
            int y2 = this.segments.get(i).y2;
            data[10 + (i * 9)] = (byte) (x1 >> 8);
            data[11 + (i * 9)] = (byte) (x1 & 0xff);
            data[12 + (i * 9)] = (byte) (y1 >> 8);
            data[13 + (i * 9)] = (byte) (y1 & 0xff);
            data[14 + (i * 9)] = (byte) (x2 >> 8);
            data[15 + (i * 9)] = (byte) (x2 & 0xff);
            data[16 + (i * 9)] = (byte) (y2 >> 8);
            data[17 + (i * 9)] = (byte) (y2 & 0xff);
        }
        int offset = (this.segments.size * 9);
        data[9 + offset] = (byte) (this.components.size >> 8);
        data[10 + offset] = (byte) (this.components.size & 0xff);
        for (int i = 0; i < this.components.size; i++) {
            data[11 + offset + (i * 9)] = (byte) this.components.get(i).id;
            int x = this.components.get(i).x;
            int y = this.components.get(i).y;
            int width = this.components.get(i).width;
            int height = this.components.get(i).height;
            data[12 + offset + (i * 9)] = (byte) (x >> 8);
            data[13 + offset + (i * 9)] = (byte) (x & 0xff);
            data[14 + offset + (i * 9)] = (byte) (y >> 8);
            data[15 + offset + (i * 9)] = (byte) (y & 0xff);
            data[16 + offset + (i * 9)] = (byte) (width >> 8);
            data[17 + offset + (i * 9)] = (byte) (width & 0xff);
            data[18 + offset + (i * 9)] = (byte) (height >> 8);
            data[19 + offset + (i * 9)] = (byte) (height & 0xff);
        }
        System.out.println("Bytes saved: " + data.length);
        handle.writeBytes(data, false);
        printSummary();
    }

    public void load(FileHandle handle) {
        // file format: 
        //   (2 bytes) "TY", 
        //   (2 bytes) 0
        //   (1 bytes) board.width
        //   (1 bytes) board.height
        //   (1 byte) 0
        //   (2 bytes) # of Wires, 
        //   For each wire
        //   (1 byte) wire.color_id
        //   (2 bytes) wire.x1
        //   (2 bytes) wire.y1
        //   (2 bytes) wire.x2
        //   (2 bytes) wire.y2
        //   (2 bytes) # of Components
        //   For each component
        //   (1 byte) component.id
        //   (2 bytes) component.x
        //   (2 bytes) component.y
        //   (2 bytes) component.width
        //   (2 bytes) component.height
        byte[] bytes;
        try {
            bytes = handle.readBytes();
        } catch (GdxRuntimeException e) {
            return;
        }
        if (bytes == null || bytes.length == 0) return; // exit if the save file is empty
        System.out.println("Attempting load");
        this.segments = new Array<Segment>(); // reset segments array
        this.components = new Array<Component>(); // reset components array
        int ptr = 4;
        this.width = (int) bytes[ptr] & 0xff; ptr++;
        this.height = (int) bytes[ptr] & 0xff; ptr++;
        ptr++;
        int num_wires = 0;
        num_wires += (int) bytes[ptr] << 8; ptr++;
        num_wires += (int) bytes[ptr] & 0xff; ptr++;
        System.out.println("Found wires: " + num_wires + "ptr: " + ptr);
        for (int i = 0; i < num_wires; i++) {
            int color_id = (int) bytes[ptr] & 0xff; ptr++;
            int x1 = (int) bytes[ptr] << 8 & 0xff; ptr++;
            x1 += (int) bytes[ptr] & 0xff; ptr++;
            int y1 = (int) bytes[ptr] << 8 & 0xff; ptr++;
            y1 += (int) bytes[ptr] & 0xff; ptr++;
            int x2 = (int) bytes[ptr] << 8 & 0xff; ptr++;
            x2 += (int) bytes[ptr] & 0xff; ptr++;
            int y2 = (int) bytes[ptr] << 8 & 0xff; ptr++;
            y2 += (int) bytes[ptr] & 0xff; ptr++;
            if (color_id == 0) {
                this.segments.add(new Segment(x1, y1, x2, y2));
            }
            else {
                System.out.println("Color_id is non zero");
                this.segments.add(new Wire(color_id, x1, y1, x2, y2));
            }
        }
        int num_c = 0;
        num_c += (int) bytes[ptr] << 8; ptr++;
        num_c += (int) bytes[ptr]; ptr++;
        System.out.println("Found components: " + num_c);
        for (int i = 0; i < num_c; i++) {
            int id = (int) bytes[ptr] & 0xff; ptr++;
            int x = (int) bytes[ptr] << 8; ptr++;
            x += (int) bytes[ptr] & 0xff; ptr++;
            int y = (int) bytes[ptr] << 8; ptr++;
            y += (int) bytes[ptr] & 0xff; ptr++;
            int width = (int) bytes[ptr] << 8; ptr++;
            width += (int) bytes[ptr] & 0xff; ptr++;
            int height = (int) bytes[ptr] << 8; ptr++;
            height += (int) bytes[ptr] & 0xff; ptr++;
            Component c = Configuration.getComponentInstanceFromComponentID(id, x, y);
            if (c == null) {
                System.out.println("component instance is null");
                continue;
            }
            c.parent = this;
            addComponent(c);
        }
        System.out.println("Successfully loaded board state!");
    }

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.boardColor = Configuration.getDefaultBoardColor;
        this.gridColor = Configuration.default_segment_color;
        this.components = new Array<Component>();
        this.vertices = new Vertex[this.height * this.width];
        this.segments = new Array<Segment>();
        setLocation( // centered location
            Configuration.screen_width / 2 - (this.getGridDimensions()[0] / 2),
            Configuration.screen_height / 2 - (this.getGridDimensions()[1] / 2));
        this.constructSegments();
        this.constructVertexObjects();
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

    public void renderSegments(ShapeRenderer sr, boolean simulationMode) {
        sr.begin(ShapeType.Filled);
        sr.setColor(gridColor);
        for (Segment s : segments) {
            s.render(sr, this, simulationMode);
        }
        sr.end();
    }

    public void renderVertexObjects(ShapeRenderer sr) {
        sr.begin(ShapeType.Filled);
        sr.setColor(Configuration.getDefaultVertexColor);
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
        int[] endpoints_reversed = new int[] {endpoints[2], endpoints[3], endpoints[0], endpoints[1]};
        for (int i = 0; i < segments.size; i++) {
            Segment s = segments.get(i);
            if (Arrays.equals(s.getEndpoints(), endpoints) || Arrays.equals(s.getEndpoints(), endpoints_reversed)){
                return s;
            }
        }
        return null;
    }

    public Wire getWire(int[] endpoints) {
        Segment s = getSegment(endpoints);
        if (s instanceof Wire) {
            return (Wire) s;
        }
        return null;
    }

    public Array<Segment> getSegmentsFromCoordinate(int x, int y) {
        Array<Segment> out = new Array<Segment>();
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
        Array<Wire> out = new Array<Wire>();
        for (Segment s : segments) {
            if (s instanceof Wire)
                out.add((Wire) s);
        }
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

    public Pin getPin(int x, int y) {
        for (Component c : components) {
            for (Pin p : c.pins) {
                if (p.x == x && p.y == y)
                    return p;
            }
        }
        return null;
    }

    public Vertex getVertex(int x, int y) {
        return this.vertices[(y * this.width) + x];
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

    public void renderBackground(ShapeRenderer sr, boolean simulationMode) {
        sr.begin(ShapeType.Filled);
        // draw background
        if (simulationMode) {
            sr.setColor(Configuration.simulationBackgroundColor);
        }
        else {
            sr.setColor(this.boardColor);
        }
        sr.rect((float) x, (float) y, getGridDimensions()[0], getGridDimensions()[1]);
        sr.end();
    }

    public void render(ShapeRenderer sr, SpriteBatch sb, boolean simulationMode) {
        renderBackground(sr, simulationMode);
        renderSegments(sr, simulationMode);
        for (Component c: components) {
            c.render(sr, sb, this);
        }
        // renderVertexObjects(sr);
    }
    public void simulate() {
        System.out.println("Simulating...");
        Array<Entanglement> entanglements = compile();
        for (Entanglement ent: entanglements) {
            System.out.println(ent.repr());
        }
        for (Segment s : segments) {
            if (s instanceof Wire) {
                Wire w = (Wire) s;
                w.active = false;
            }
        }
        for (Component c : components) {
            for (Pin p : c.pins) {
                p.active = false;
            }
        }
        int i = 0;
        while (i < 100) {
            for (Entanglement e : entanglements) {
                if(e.shouldBeActive()) {
                    for (Pin p: e.pins) {
                        p.active = true;
                    }
                }
                else {
                    for (Pin p: e.pins) {
                        p.active = false;
                    }
                }
            }
            for (Component c : components) {
                c.simulate();
            }
            i++;
        }
        // paint wires funny colors
        for (Entanglement e : entanglements) {
            if (e.shouldBeActive()) {
                for (Wire w : e.wires) {
                    w.active = true;
                }
            }
        }
        printSummary();
    }
    public Array<Wire> getAttached(int[] endpoints) {
        Wire w = getWire(endpoints);
        Array<Wire> attached1 = getWiresFromCoordinate(endpoints[0], endpoints[1]);
        attached1.removeValue(w, false);
        Array<Wire> attached2 = getWiresFromCoordinate(endpoints[2], endpoints[3]);
        attached2.removeValue(w, false);
        Array<Wire> attached = new Array<Wire>(attached1.size + attached2.size);
        attached.addAll(attached1);
        attached.addAll(attached2);
        return attached;
    }

    // public void simulate() {
    //     if (this.mostRecentCompilation == null) 
    //         return;
    //     Graph<Pin, Array<Wire>> g = this.mostRecentCompilation;
    //     for (Edge<Array<Wire>> edge : g.edges) {
            
    //     }
    // }

    public Array<Entanglement> compile() {
        /* Create Undirected Graph representing the structure of the circuit */
        long start = TimeUtils.nanoTime();
        Array<Segment> accounted_for = new Array<Segment>(this.height * this.width);
        Array<Entanglement> result = new Array<Entanglement>();
        for (int i = 0; i < this.segments.size; i++) {
            Segment seg = this.segments.get(i);
            if (seg instanceof Wire && !accounted_for.contains(seg, false)) {
                //depth first search (maybe optimal maybe not... java stack is pretty sexy)
                Stack<Wire> stack = new Stack<Wire>();
                Array<Pin> new_entanglement = new Array<Pin>();
                Array<Wire> new_wire_entanglement = new Array<Wire>();
                Wire wire = (Wire) seg;
                int color = wire.color_id;
                stack.push(wire);
                while (!stack.empty()) {
                    Wire v = stack.pop();
                    int[] endpoints = v.getEndpoints();
                    if (!accounted_for.contains(v, false)){
                        accounted_for.add(v);
                        new_wire_entanglement.add(v);
                        Pin c1 = getPin(endpoints[2], endpoints[3]);
                        Pin c2 = getPin(endpoints[0], endpoints[1]);
                        if (c1 != null && !new_entanglement.contains(c1, false)) new_entanglement.add(c1);
                        if (c2 != null && !new_entanglement.contains(c2, false)) new_entanglement.add(c2);
                        Array<Wire> attached = getAttached(endpoints);
                        for (Wire s1 : attached) {
                            if (s1 != v && s1.color_id == color)
                                stack.push(s1);
                        }
                    }
                }
                result.add(new Entanglement(new_wire_entanglement, new_entanglement));
            }
        }
        System.out.println("DFS search took " + (TimeUtils.nanoTime() - start) + " ns");
        return result;
    }
    public void printSummary() {
        System.out.println("Summary:");
        for (Segment s : segments) {
            if (s instanceof Wire) {
                Wire w = (Wire) s;
                int[] endpoints = w.getEndpoints();
                System.out.print("(Wire)");
                for (int pt : endpoints) {
                    System.out.print(pt + " ");
                }
                if (w.active)
                    System.out.print("(Active)");
                else   
                    System.out.print("(Not Active)");

                Array<Wire> attached = getAttached(endpoints);
                System.out.print("Conn: " + attached.size);
                System.out.print('\n');
            }
        }
        for (Component c : components) {
            System.out.println("(Component) " + c.name);
            for (Pin p : c.pins) {
                if (p.active)
                    System.out.println("    " + "(Pin) " + p.x + " " + p.y + "(Active)");
                else
                    System.out.println("    " + "(Pin) " + p.x + " " + p.y + "(Not Active)");
            }
        }
        int num_of_wires = 0;
        for (Segment s : segments) {
            if (s instanceof Wire) {
                num_of_wires++;
                System.out.println("(Wire)" + ((Wire)s).color_id);
            }
        }
        System.out.println("Number of wires: " + num_of_wires);
    }
}
