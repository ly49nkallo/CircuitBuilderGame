package com.circuit_builder.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScene implements Screen{
    final CircuitBuilderGame game;
    OrthographicCamera camera;
    Texture background_texture;
    int elapsed;

    public MainMenuScene(final CircuitBuilderGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Configuration.screen_width, Configuration.screen_height);
        this.background_texture = new Texture(Gdx.files.internal("assets/background_mm.png"));
    }
    @Override
    public void render(float delta) {
        float ptrX = Gdx.input.getX();
        float ptrY = Gdx.input.getY();
        elapsed++;
        ScreenUtils.clear(1f, 1f, 1f, 1f);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(this.background_texture, 0, 0, Configuration.screen_width, Configuration.screen_height);
        game.batch.end();
        if (ptrX > Configuration.screen_width / 2) {
            if (Gdx.input.isTouched() && elapsed > 30) {
                game.setScreen(game.loadScreen);
                dispose();
            }
        }
        else {
            if (Gdx.input.isTouched() && elapsed > 30) {
                game.setScreen(game.boardScreen);
                dispose();
            }

        }
    }
    @Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
        elapsed = 0;
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
