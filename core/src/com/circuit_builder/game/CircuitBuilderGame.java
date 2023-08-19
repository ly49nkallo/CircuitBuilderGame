package com.circuit_builder.game;

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
    
    @Override
    public void create () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Configuration.screen_width, Configuration.screen_height);
        sr = new ShapeRenderer();
        board = new Board(10, 10);
        Wire w = new Wire(1, 1f, 3f, 4f, 3f);
        board.addWire(w);
    }

    @Override
    public void render () {
        ScreenUtils.clear(Configuration.background_color);
        camera.update();
        sr.setProjectionMatrix(camera.combined);
        this.board.render(sr, Configuration.screen_width / 2 - (this.board.getGridDimensions()[0] / 2), 
                            Configuration.screen_height / 2 - (this.board.getGridDimensions()[1] / 2));
    }

    
    @Override
    public void dispose () {
        sr.dispose();
    }
}
