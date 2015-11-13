package com.project.googlecardboard.gui;

/**
 * Created by Garrett on 10/11/2015.
 */
public class GUIModel {

    private final int id;
    private final float[] textureCoordinates = {
            -1, 1,
            -1, -1,
            1, 1,
            1, -1
    };
    private final int vertexCount;

    public GUIModel(){
        this.id = 0;
        this.vertexCount = textureCoordinates.length / 2;
    }

    public int getID(){
        return id;
    }

    public float[] getTextureCoordinates(){
        return textureCoordinates;
    }

    public int getVertexCount(){
        return vertexCount;
    }
}
