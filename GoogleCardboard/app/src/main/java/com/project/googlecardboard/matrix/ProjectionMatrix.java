package com.project.googlecardboard.matrix;

import com.google.vrtoolkit.cardboard.Eye;

/**
 * Created by Garrett on 17/11/2015.
 */
public class ProjectionMatrix {

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 100.0f;

    private final float[] matrix;

    public ProjectionMatrix(Eye eye){
        this.matrix = eye.getPerspective(Z_NEAR, Z_FAR);
    }

    public float[] getMatrix(){
        return matrix;
    }
}
