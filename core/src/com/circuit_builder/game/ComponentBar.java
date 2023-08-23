package com.circuit_builder.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader.Config;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class ComponentBar {
    float x, y;
    public Rectangle[] boxes;

    public ComponentBar() { 
        boxes = new Rectangle[Configuration.number_of_components];
    }

    public void setLocation(float x, float y) {
        this.x = x; this.y = y;
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new Rectangle(
                x,
                y + Configuration.number_of_components * Configuration.component_sprite_width 
                - ((float) (i + 1) *  Configuration.component_sprite_width),
                Configuration.component_sprite_width,
                Configuration.component_sprite_width
            );
        }
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(
            this.x, this.y, 
            Configuration.component_sprite_width,
            Configuration.component_sprite_width * Configuration.number_of_components);
    }

    public void render(ShapeRenderer sr, SpriteBatch sb, int selected_component) {
        Rectangle bounds = getBoundingBox(); 
        // selected box
        sr.begin(ShapeType.Filled);
        sr.setColor(Color.WHITE);
        Rectangle selectedBox = boxes[selected_component - 1];
        float o = ((Configuration.component_bar_selected_background - Configuration.component_sprite_width) / 2);
        sr.rect(
            selectedBox.x - o,
            selectedBox.y - o,
            Configuration.component_bar_selected_background,
            Configuration.component_bar_selected_background);
        sr.setColor(Color.GRAY);
        for (int i = 0; i < boxes.length; i++) {
            Rectangle r = boxes[i];
            sr.rect(r.x, r.y, r.width, r.height);
        }
        sr.end();

        sb.begin();
        for (int i = 0; i < boxes.length; i++){
            Rectangle r = boxes[i];
            sb.draw(
                Configuration.getTextureFromComponentID(i + 1), 
                r.x, 
                r.y, 
                r.width, 
                r.height);
        }
        sb.end();
  }
}
