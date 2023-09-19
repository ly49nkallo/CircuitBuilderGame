package com.circuit_builder.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// public class Resistor extends Component{
//     public static Texture s_texture = new Texture(Gdx.files.internal("assets/Resistor.png"));

//     public Resistor(int x, int y, Board parent) {
//         super(x, y, Configuration.resistor_width, Configuration.resistor_height, "Resistor", Resistor.s_texture, parent);
//     }
//     /* obsolete */
//     public static void render_sprite(ShapeRenderer sr, SpriteBatch sb, float x, float y) {
//         Component.render_sprite_background(sr, x, y);
//         sb.begin();
//         sb.draw(s_texture, x, y, Configuration.component_sprite_width, Configuration.component_sprite_width);
//         sb.end();
//     }
// }
