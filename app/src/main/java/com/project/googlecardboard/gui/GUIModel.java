package com.project.googlecardboard.gui;

/**
 * Created by Garrett on 10/11/2015.
 */
public class GUIModel {

    private static final float[] model = {

        // Back face
        1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f
    };

    private static final float[] normals = {
            // Back face
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f
    };

    public GUIModel(){

    }

    public float[] getModel(){
        return model;
    }

    public float[] getNormals(){
        return normals;
    }
}
