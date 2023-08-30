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
    ComponentBar componentBar;
    private float last_ptrX, last_ptrY;
    private Segment last_segment;
    private Vertex last_vertex;
    public int selected_color;
    public int selected_component;
    public long last_time_touched;
    public int clock;
    
    @Override
    public void create () {
        clock = 0;
        last_time_touched = TimeUtils.nanoTime();
        selected_color = Configuration.default_wire_color;
        selected_component = -1;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Configuration.screen_width, Configuration.screen_height);
        sr = new ShapeRenderer();
        sb = new SpriteBatch(200);
        board = new Board(20, 20);
        board.setLocation( // centered location
            Configuration.screen_width / 2 - (this.board.getGridDimensions()[0] / 2),
            Configuration.screen_height / 2 - (this.board.getGridDimensions()[1] / 2));
        board.constructVertexObjects();
        board.constructSegments();
        colorbar = new Colorbar();
        colorbar.setLocation(
            Configuration.screen_width / 2 - (Colorbar.getWidth / 2),
            20 - (Colorbar.getHeight / 2));
        componentBar = new ComponentBar();
        componentBar.setLocation(
            Configuration.screen_width - 100f,
            Configuration.screen_height - (Configuration.number_of_components * 100f)
        );
        this.last_ptrX = 0; this.last_ptrY = 0;
    }

    @Override
    public void render () {
        long start = TimeUtils.nanoTime();

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
            if (selected_color != -1) {
                Segment closestSegment;
                if (last_ptrX == ptrX && last_ptrY == ptrY && last_segment != null) {
                    closestSegment = last_segment;
                }
                else {
                    closestSegment = board.getClosestSegment(ptrX, ptrY);
                    last_segment = closestSegment;
                }
                closestSegment.selectedRender(sr, board); // mouseover select render
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && TimeUtils.nanoTime() - last_time_touched > cooldown) {
                    board.segments.removeValue(closestSegment, false);
                    board.segments.add(new Wire(selected_color, closestSegment.x1, closestSegment.y1, closestSegment.x2, closestSegment.y2));
                    board.compile();
                    last_time_touched = TimeUtils.nanoTime();
                }
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && TimeUtils.nanoTime() - last_time_touched > cooldown) {
                    board.segments.removeValue(closestSegment, false);
                    board.segments.add(new Segment(closestSegment.x1, closestSegment.y1, closestSegment.x2, closestSegment.y2));
                    last_time_touched = TimeUtils.nanoTime();
                }
            }
            // place component
            else if (selected_component != -1) {
                 // render vertexes
                Vertex closestVertex;
                if (last_ptrX == ptrX && last_ptrY == ptrY && last_vertex != null) {
                    closestVertex = last_vertex;
                }
                else {
                    closestVertex = board.getClosestVertex(ptrX, ptrY);
                    last_vertex = closestVertex;
                }
                int _width = Configuration.getWidthFromComponentID(selected_component);
                int _height = Configuration.getHeightFromComponentID(selected_component);
                sr.begin(ShapeType.Filled);
                sr.setColor(Board.getDefaultVertexColor);
                for (int i = 0; i < _width; i++) {
                    for (int j = 0; j < _height; j++) {
                        sr.circle(
                            closestVertex.bounds.x + ((float) i * Configuration.grid_box_width),
                            closestVertex.bounds.y + ((float) j * Configuration.grid_box_height), 
                            Configuration.grid_line_width);
                    }
                }
                sr.end();
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && TimeUtils.nanoTime() - last_time_touched > cooldown) {
                    Component component_instance = Configuration.getComponentInstanceFromComponentID(
                        selected_component,
                        closestVertex.x,
                        closestVertex.y);
                    if (!board.addComponent(component_instance))
                        System.out.println("Cannot Place Component!");
                    last_time_touched = TimeUtils.nanoTime();
                }
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && TimeUtils.nanoTime() - last_time_touched > cooldown) {
                    Component component_instance = board.getComponent(closestVertex.y, closestVertex.x);
                    if (component_instance != null) {
                        board.removeComponent(component_instance);
                    }
                }
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
                        selected_component = -1;
                    }
                    i++;
                }
                }
                last_time_touched = TimeUtils.nanoTime();
            }
        }
        /* COMPONENT BAR */
        this.componentBar.render(sr, sb, selected_component);
        if (componentBar.getBoundingBox().contains(ptrX, ptrY)) {
            if ((Gdx.input.isButtonPressed(Input.Buttons.LEFT)) && TimeUtils.nanoTime() - last_time_touched > cooldown) {
                for (int i = 0; i < componentBar.boxes.length; i++) {
                    if (componentBar.boxes[i].contains(ptrX, ptrY)) {
                        selected_component = i + 1;
                        selected_color = -1;
                    }
                }
                last_time_touched = TimeUtils.nanoTime();
            }
        }
        if (clock % 10 == 0) {

            // System.out.println("==============");
            // System.out.println("Selected Component :" + selected_component);
            // System.out.println("Selected Wire Color :" + selected_color);
        }
        this.last_ptrX = ptrX; this.last_ptrY = ptrY;
    if (clock % 30 == 0) System.out.println("Render Loop took  " + (TimeUtils.timeSinceNanos(start)) + " nanoseconds");
    clock++;}
    
    @Override
    public void dispose () {
        sr.dispose();
    }
}
