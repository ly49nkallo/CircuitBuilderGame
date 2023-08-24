package com.circuit_builder.game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Configuration {
    // just a bunch of static parameters that are used elsewhere in the application
    public static final int screen_width = 1300; // in pixels
    public static final int screen_height = 800; // in pixels

    public static final float grid_line_width = (float) screen_width / 320; // in pixels
    public static final float grid_box_width = (float) screen_width / 40; // in pixels
    public static final float grid_box_height = (float) grid_box_width; // in pixels
    public static final Color background_color = new Color(0.1f, 0.1f, 0.1f, 1f);
    public static final int default_wire_color = 1;
    public static final long cooldown = 200000000; //0.2 seconds
    public static final Color default_segment_color = new Color(0.3f, 0.2f, 0.1f, 1f);
    public static final Color default_segment_selected_color = new Color(0.4f, 0.3f, 0.2f, 1f);
    public static final float brightness_increase_wire = 0.4f;

    public static final int number_of_colors = 6;
    public static final Color color_map(int color_id) {
        switch (color_id) {
            case 1:
                return Color.RED;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.BLACK;
            case 5:
                return Color.WHITE;
            case 6:
                return Color.ORANGE;
            default:
                return Color.PURPLE;
        }
    }

    public static final float color_box_side_length = screen_width / 15; // in pixels
    public static final float color_box_selected_expand_percent = 0.1F;
    public static final float color_box_selected_expand = (float) color_box_side_length * (1f + color_box_selected_expand_percent);
    public static final float space_between_boxes = color_box_side_length * 0.2f;

    public static final Color default_component_color = Color.LIGHT_GRAY;
    public static final Color stud_color = Color.WHITE;
    public static final float component_overhang = grid_box_width * 0.3f;

    public static final int battery_width = 2;
    public static final int battery_height = 3;

    public static final int resistor_width = 1;
    public static final int resistor_height = 3;

    public static final int number_of_components = 2;
    public static final float component_sprite_width = screen_width / 20;
    public static final float component_bar_selected_background = component_sprite_width * 1.2f;

    /* I hate this shit */

    public static Texture getTextureFromComponentID(int component_id) {
        switch(component_id) {
            case 1:
                return Source.s_texture;
            case 2:
                return Resistor.s_texture;
            default:
                return null;
        }
    }
    public static int getWidthFromComponentID(int component_id) {
        switch(component_id) {
            case 1:
                return battery_width;
            case 2:
                return resistor_width;
            default:
                return -1;
        }
    }
    public static int getHeightFromComponentID(int component_id) {
        switch(component_id) {
            case 1:
                return battery_height;
            case 2:
                return resistor_height;
            default:
                return -1;
        }
    }
    public static Component getComponentInstanceFromComponentID(int component_id, int x, int y) {
        switch(component_id) {
            case 1:
                return new Source(x, y);
            case 2:
                return new Resistor(x, y);
            default:
                return null;
        }
    }
}
