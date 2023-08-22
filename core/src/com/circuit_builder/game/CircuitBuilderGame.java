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
import com.badlogic.gdx.Input;

// public static final int screen_width = 800; // in pixels
// public static final int screen_height = 480; // in pixels
// public static final int grid_width = 10; // in sectors
// public static final int grid_height = 10; // in sectors
// public static final int grid_line_width = 5; // in pixels
// public static final int grid_box_width = 40; // in pixels
public class CircuitBuilderGame extends Game {

    private OrthographicCamera camera;
    ShapeRenderer sr;
    SpriteBatch sb;
    Board board;
    Colorbar colorbar;
    public int selected_color;
    public int selected_component; //
    public long last_time_touched;
    public int clock;
    
    @Override
    public void create () {
        clock = 0;
        last_time_touched = TimeUtils.nanoTime();
        selected_color = Configuration.default_wire_color;
        selected_component = 0;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Configuration.screen_width, Configuration.screen_height);
        sr = new ShapeRenderer();
        sb = new SpriteBatch(200);
        board = new Board(10, 10);
        board.setLocation( // centered location
            Configuration.screen_width / 2 - (this.board.getGridDimensions()[0] / 2),
            Configuration.screen_height / 2 - (this.board.getGridDimensions()[1] / 2));
        board.constructVertexObjects();
        board.constructSegments();
        board.addComponent(new Source(2, 2));
        colorbar = new Colorbar();
        colorbar.setLocation(
            Configuration.screen_width / 2 - (Colorbar.getWidth / 2),
            20 - (Colorbar.getHeight / 2));
    }

    @Override
    public void render () {
        // long start = TimeUtils.nanoTime();

        ScreenUtils.clear(Configuration.background_color);
        camera.update();
        sr.setProjectionMatrix(camera.combined);

        long cooldown = Configuration.cooldown;
        float ptrX = Gdx.input.getX();
        float ptrY = Gdx.input.getY();
        Vector3 touchPosition = new Vector3();
        touchPosition.set(ptrX, ptrY, 0f);
        camera.unproject(touchPosition);
        ptrX = touchPosition.x; 
        ptrY = touchPosition.y; 
        /* BOARD */
        this.board.render(sr, sb);
        if (board.getBoundingBox().contains(ptrX, ptrY)){
            Segment closestSegment = board.getClosestSegment(ptrX, ptrY);
            closestSegment.selectedRender(sr, board); // mouseover select render
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && TimeUtils.nanoTime() - last_time_touched > cooldown) {
                board.segments.removeValue(closestSegment, false);
                board.segments.add(new Wire(selected_color, closestSegment.x1, closestSegment.y1, closestSegment.x2, closestSegment.y2));
                last_time_touched = TimeUtils.nanoTime();
            }
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && TimeUtils.nanoTime() - last_time_touched > cooldown) {
                board.segments.removeValue(closestSegment, false);
                board.segments.add(new Segment(closestSegment.x1, closestSegment.y1, closestSegment.x2, closestSegment.y2));
                last_time_touched = TimeUtils.nanoTime();
            }
        }
        /* COLORBAR */
        this.colorbar.render(sr, selected_color);
        if (colorbar.getBoundingBox().contains(ptrX, ptrY)) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && TimeUtils.nanoTime() - last_time_touched > cooldown) {
                {
                int i = 1;
                for (Rectangle r : colorbar.boxes) {
                    if (r.contains(ptrX, ptrY)) {
                        selected_color = i;
                    }
                    i++;
                }
                }
            }
        }
    // if (clock % 30 == 0) System.out.println("Render Loop took  " + (TimeUtils.timeSinceNanos(start)) + " nanoseconds");
    clock++;}
    
    @Override
    public void dispose () {
        sr.dispose();
    }
}
