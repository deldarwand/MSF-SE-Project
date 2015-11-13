package com.project.googlecardboard.render;

/**
 * Created by Garrett on 11/11/2015.
 */
public class Camera {

    public static final float Z_NEAR = 0.1f;
    public static final float Z_FAR = 100.0f;

    private float[] camera;
    private float[] view;
    private float[] headView;

    public Camera(){
        this.camera = new float[16];
        this.view = new float[16];
        this.headView = new float[16];
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
