package com.circuit_builder.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Source extends Component{
    public int x, y;
    public Texture texture;
    public Source(int x, int y) {
        super(x, y, Configuration.battery_width, Configuration.battery_height, "Source");
        this.x = x; this.y = y;
        texture = new Texture(Gdx.files.internal("assets/Source.png"));
    }

    public void render(ShapeRenderer sr, SpriteBatch sb, Board parent) {
        super.render(sr, sb, parent);
        Rectangle bounds = getBoundingBox(parent);

        sb.begin();
        sb.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        sb.end();
    } 
}
