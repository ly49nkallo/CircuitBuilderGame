package com.circuit_builder.game;
import com.badlogic.gdx.graphics.Color;

public class Configuration {
    // just a bunch of static parameters that are used elsewhere in the application
    public static final int screen_width = 800; // in pixels
    public static final int screen_height = 480; // in pixels
    public static final int grid_line_width = 5; // in pixels
    public static final int grid_box_width = 40; // in pixels
    public static final int grid_box_height = 40; // in pixels
    public static final Color background_color = new Color(0.1f, 0.1f, 0.1f, 1f);
    public static final int default_wire_color = 1; //red
    public static final long cooldown = 200000000; //0.2 seconds
    public static final Color default_segment_color = new Color(0.3f, 0.2f, 0.1f, 1f);
    public static final Color default_segment_selected_color = new Color(0.4f, 0.3f, 0.2f, 1f);
    public static final float brightness_increase_wire = 0.4f;
}
