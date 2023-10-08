package com.circuit_builder.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class LED extends Component{
    public static Texture s_texture = new Texture(Gdx.files.internal("assets/NOT.png"));
    public boolean on;
    public Color color;

    public LED(int x, int y, Board parent) {
        super(x, y, Configuration.led_width, Configuration.led_height, "LED", LED.s_texture, parent, 6);
        this.on = false;
        this.color = Configuration.led_color;
    }
    @Override
    public void simulate() {
        if (this.pins[0].active || this.pins[1].active) {
            this.on = true;
        }
        else {
            this.on = false;
        }
    }

    @Override
    public void render(ShapeRenderer sr, SpriteBatch sb, Board parent) {
        super.render(sr, sb, parent);
        if (on) {
            sr.begin(ShapeType.Filled);
            sr.setColor(this.color);
            Rectangle bounds = getBoundingBox(parent);
            float lightx = bounds.x + bounds.x + bounds.width;
            lightx /= 2;
            float lighty = bounds.y + bounds.y + bounds.height;
            lighty /= 2;
            float lightw = bounds.width / 3;
            sr.circle(
                lightx, lighty, lightw
            );
            sr.end();
        }
    }
}
