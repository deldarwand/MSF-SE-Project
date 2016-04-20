package com.project.googlecardboard.util;

import java.util.UUID;

/**
 * Created by Garrett on 07/01/2016.
 */
public class Constants {

    public static final float ROLL_LIMIT = 0.17f;
    public static final float PITCH_LIMIT = 0.17f;
    public static final float YAW_LIMIT = 0.17f;
    public static final String SERVER_NAME = "LENOVO-PC";
    public static final UUID SERVER_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");

    public static final float[] CUBE_COLORS = new float[] {
        // front, green
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,

        // right, blue
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,

        // back, also green
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,
        0f, 0.5273f, 0.2656f, 1.0f,

        // left, also blue
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,
        0.0f, 0.3398f, 0.9023f, 1.0f,

        // top, red
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,

        // bottom, also red
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f,
        0.8359375f,  0.17578125f,  0.125f, 1.0f
    };

    public static final float[] CUBE_FOUND_COLORS = new float[] {
        // front, yellow
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,

        // right, yellow
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,

        // back, yellow
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,

        // left, yellow
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,

        // top, yellow
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,

        // bottom, yellow
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f,
        1.0f,  0.6523f, 0.0f, 1.0f
    };

}
