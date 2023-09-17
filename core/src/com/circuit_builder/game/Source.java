package com.circuit_builder.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Source extends Component{
    public static Texture s_texture = new Texture(Gdx.files.internal("assets/Source.png"));

    public Source(int x, int y, Board parent) {
        super(x, y, Configuration.battery_width, Configuration.battery_height, "Source", Source.s_texture, parent);

    }

    @Override
    public void simulate() {
        this.pins[2].active = true;
        this.pins[5].active = true;
    }
    /* obsolete */
    // public static void render_sprite(ShapeRenderer sr, SpriteBatch sb, float x, float y) {
    //     Component.render_sprite_background(sr, x, y);
    //     sb.begin();
    //     sb.draw(s_texture, x, y, Configuration.component_sprite_width, Configuration.component_sprite_width);
    //     sb.end();
    // }
}
