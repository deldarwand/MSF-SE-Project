package com.project.googlecardboard.render;

import android.opengl.Matrix;

/**
 * Created by Garrett on 11/11/2015.
 */
public class Camera {

    private static final float CAMERA_Z = 0.01f;

    public static final float Z_NEAR = 0.1f;
    public static final float Z_FAR = 100.0f;

    private float[] camera;
    private float[] view;
    private float[] headView;

    public Camera(){
        this.camera = new float[16];
        this.view = new float[16];
        this.headView = new float[16];

        Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    public float[] getPosition(){
        return camera;
    }

    public float[] getView(){
        return view;
    }

    public float[] getHeadView(){
        return headView;
    }
}
