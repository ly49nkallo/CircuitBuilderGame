package com.circuit_builder.game;

import java.sql.Time;
import java.util.Iterator;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader.Config;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;

// public static final int screen_width = 800; // in pixels
// public static final int screen_height = 480; // in pixels
// public static final int grid_width = 10; // in sectors
// public static final int grid_height = 10; // in sectors
// public static final int grid_line_width = 5; // in pixels
// public static final int grid_box_width = 40; // in pixels
public class CircuitBuilderGame extends Game {

    private OrthographicCamera camera;
    ShapeRenderer sr;
    Board board;
    public int selected_color;
    public long last_time_touched;
    
    @Override
    public void create () {
        last_time_touched = TimeUtils.nanoTime();
        selected_color = Configuration.default_wire_color;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Configuration.screen_width, Configuration.screen_height);
        sr = new ShapeRenderer();
        board = new Board(10, 10);
        board.setLocation( // centered location
            Configuration.screen_width / 2 - (this.board.getGridDimensions()[0] / 2),
            Configuration.screen_height / 2 - (this.board.getGridDimensions()[1] / 2));
        Wire w1 = new Wire(1, 1, 3, 4, 3);
        Wire w2 = new Wire(2, 3, 5, 7, 5);
        Wire w3 = new Wire(6, 8, 4, 8, 7);
        Array<Wire> wires = new Array<Wire>(); 
        wires.add(w1); 
        wires.add(w2); 
        wires.add(w3);
        board.addWires(wires);
        board.constructVertexObjects();
        board.constructSegments();
    }

    @Override
    public void render () {
        ScreenUtils.clear(Configuration.background_color);
        camera.update();
        sr.setProjectionMatrix(camera.combined);
        this.board.render(sr);
        long cooldown = Configuration.cooldown;
        if (Gdx.input.isTouched() && TimeUtils.nanoTime() - last_time_touched > cooldown) {
            float ptrX = Gdx.input.getX();
            float ptrY = Gdx.input.getY();
            Vector3 touchPosition = new Vector3();
            touchPosition.set(ptrX, ptrY, 0f);
            camera.unproject(touchPosition);
            ptrX = touchPosition.x; 
            ptrY = touchPosition.y;

            float lowestDist = Float.MAX_VALUE;
            int lowestIdx = board.wires.size;
            for (int i = 0; i < board.wires.size; i++) {
                Segment w = board.segments.get(i);
                float [] coords = w.getScreenSpaceCoordinatesForEndpoints(this.board.x, this.board.y);
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
            Segment closestSegment = board.wires.get(lowestIdx);
            last_time_touched = TimeUtils.nanoTime();
        }
    }

    
    @Override
    public void dispose () {
        sr.dispose();
    }
}
