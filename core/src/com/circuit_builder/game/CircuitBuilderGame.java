package com.circuit_builder.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class CircuitBuilderGame extends Game {

    protected Screen boardScreen;
    protected Screen mainMenuScreen;
    protected Screen loadScreen;
    protected FileHandle savesFileHandle;
    SpriteBatch batch;
    BitmapFont font;
    boolean fl;

    @Override
    public void create () {
        boardScreen = new BoardScene(this);
        mainMenuScreen = new MainMenuScene(this);
        loadScreen = new LoadScreen(this);
        savesFileHandle = Gdx.files.local("/saves/save1.sav");

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        this.setScreen(mainMenuScreen);
        fl = false;
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
