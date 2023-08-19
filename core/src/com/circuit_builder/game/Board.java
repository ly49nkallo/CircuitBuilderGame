package com.circuit_builder.game;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader.Config;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Board {
    // GRID DIMENSIONS
    public int width;
    public int height;
    public Color boardColor;
    public Color gridColor;
    // 
    public Array<Component> components;
    public Array<Wire> wires;

    public Color getDefaultBoardColor() {
        return new Color(0.1f, 0.15f, 0.12f, 1f);
    }

    public Color getDefaultGridColor() {
        return new Color(0.3f, 0.3f, 0.3f, 1f);
    }

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.boardColor = getDefaultBoardColor();
        this.gridColor = getDefaultGridColor();
        this.components = new Array<Component>();
        this.wires = new Array<Wire>();
    }

    public void addWire(Wire wire) {
        this.wires.add(wire);
    }

    public final int[] getGridDimensions() {
        return new int[] {
            (this.width - 1) * Configuration.grid_box_width, 
            (this.height - 1) * Configuration.grid_box_height
        };
    }

    public void render(ShapeRenderer sr, int x, int y) {
        sr.begin(ShapeType.Filled);
        // draw background
        sr.setColor(this.boardColor);
        sr.rect((float) x, (float) y, getGridDimensions()[0], getGridDimensions()[1]);
        // draw grid
        sr.setColor(this.gridColor);
        // vertical lines
        for (int i = 0; i < this.width; i++) {
            sr.rectLine((float) x + (float) i * Configuration.grid_box_width,
                        (float) y,
                        (float) x + (float) i * Configuration.grid_box_width,
                        (float) y + (float) getGridDimensions()[1],
                        Configuration.grid_line_width);
        }
        // horizontal lines
        for (int i = 0; i < this.height; i++) {
            sr.rectLine((float) x - (float) Configuration.grid_line_width / 2, 
                        (float) y + (float) i * Configuration.grid_box_height,
                        (float) x + (float) getGridDimensions()[0] + (float) Configuration.grid_line_width / 2,
                        (float) y + (float) i * Configuration.grid_box_height,
                        Configuration.grid_line_width);
        }
        sr.end();
        for (Wire w : wires) {
            w.render(sr, this, (float) x, (float) y);
        }
        for (Component c: components) {
            c.render(sr, this);
        }
    }
}
