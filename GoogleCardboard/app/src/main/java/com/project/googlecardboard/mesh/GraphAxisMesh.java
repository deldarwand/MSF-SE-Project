package com.project.googlecardboard.mesh;


import com.project.googlecardboard.render.shader.ShaderType;

/**
 * Created by danieleldar on 11/12/2015.
 */
public class GraphAxisMesh extends Mesh{

    private static final float[] AXES = new float[]{
        0.5f, 0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f
    };

    public GraphAxisMesh(ShaderType shaderType) {
        super(shaderType, AXES, 4);
    }
}