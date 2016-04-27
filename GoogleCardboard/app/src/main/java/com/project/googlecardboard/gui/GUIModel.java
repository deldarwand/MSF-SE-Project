package com.project.googlecardboard.gui;

/**
 * Created by Garrett on 10/11/2015.
 */
public class GUIModel {

    private int i = 0;

    public static final float[] model = {

        // Back face
        /*1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f*/
        1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        0.5f, 0.5f, -1.0f,
        1.0f, -1.0f, -1.0f
    };

    private static final float[] model2 = {
        1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        0.8f, 0.5f, -1.0f,
        1.0f, -1.0f, -1.0f
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
        if(i % 2 == 0){
            return model2;
        }
        return model;
    }

    public void update(){
        i++;
    }

    public float[] getNormals(){
        return normals;
    }
}
