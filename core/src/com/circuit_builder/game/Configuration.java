package com.circuit_builder.game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Configuration {
    // just a bunch of static parameters that are used elsewhere in the application
    public static final int screen_width = 1300; // in pixels
    public static final int screen_height = 800; // in pixels

    public static final float grid_line_width = (float) screen_width / 300; // in pixels
    public static final float grid_box_width = (float) screen_width / 40; // in pixels
    public static final float grid_box_height = (float) grid_box_width; // in pixels
    public static final Color background_color = new Color(0.1f, 0.1f, 0.1f, 1f);
    public static final int default_wire_color = 1;
    public static final long cooldown = 200000000; //0.2 seconds
    public static final Color default_segment_color = new Color(0.3f, 0.3f, 0.3f, 1f);
    public static final Color default_segment_selected_color = new Color(0.45f, 0.45f, 0.45f, 1f);
    public static final float brightness_increase_wire = 0.4f;

    public static final Color getDefaultBoardColor = new Color(0.8f, 0.8f, 0.8f, 1f);
    public static final Color getDefaultVertexColor = new Color(Color.GRAY);

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

    public static final float component_sprite_width = screen_width / 20;
    public static final float component_bar_selected_background = component_sprite_width * 1.2f;

    public static final int battery_width = 2;
    public static final int battery_height = 3;

    public static final int resistor_width = 2;
    public static final int resistor_height = 3;

    public static final int and_gate_width = 2;
    public static final int and_gate_height = 2;

    public static final int not_gate_width = 1;
    public static final int not_gate_height = 2;

    public static final int or_gate_width = 2;
    public static final int or_gate_height = 2;

    public static final int xor_gate_width = 2;
    public static final int xor_gate_height = 2;

    public static final int led_width = 1;
    public static final int led_height = 2;
    public static final Color led_color = Color.RED;
    /* I hate this shit */

    public static final int number_of_components = 6;
    public static Texture getTextureFromComponentID(int component_id) {
        switch(component_id) {
            case 1:
                return Source.s_texture;
            case 2:
                return AndGate.s_texture;
            case 3:
                return NotGate.s_texture;
            case 4:
                return OrGate.s_texture;
            case 5:
                return XORGate.s_texture;
            case 6:
                return LED.s_texture;
            default:
                return null;
        }
    }
    public static int getWidthFromComponentID(int component_id) {
        switch(component_id) {
            case 1:
                return battery_width;
            case 2:
                return and_gate_width;
            case 3:
                return not_gate_width;
            case 4:
                return or_gate_width;
            case 5:
                return xor_gate_width;
            case 6:
                return led_width;
            default:
                return -1;
        }
    }
    public static int getHeightFromComponentID(int component_id) {
        switch(component_id) {
            case 1:
                return battery_height;
            case 2:
                return and_gate_height;
            case 3:
                return not_gate_height;
            case 4:
                return or_gate_height;
            case 5:
                return xor_gate_height;
            case 6:
                return led_height;
            default:
                return -1;
        }
    }
    public static Component getComponentInstanceFromComponentID(int component_id, int x, int y) {
        switch(component_id) {
            case 1:
                return new Source(x, y, null);
            case 2:
                return new AndGate(x, y, null);
            case 3:
                return new NotGate(x, y, null);
            case 4:
                return new OrGate(x, y, null);
            case 5:
                return new XORGate(x, y, null);
            case 6:
                return new LED(x, y, null);
            default:
                return null;
        }
    }
    public static Color simulationBackgroundColor = new Color(0.1f, 0.2f, 0.1f, 1f);
}
