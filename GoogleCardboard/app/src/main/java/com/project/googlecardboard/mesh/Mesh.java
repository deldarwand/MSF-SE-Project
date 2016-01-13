package com.project.googlecardboard.mesh;

import android.opengl.GLES20;

import com.project.googlecardboard.graph.Drawing;
import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;

/**
 * Created by danieleldar on 11/12/2015.
 */
public abstract class Mesh implements Drawing {

    private final ShaderType shaderType;
    protected final int capacity;
    protected volatile float[] vertex;

    public Mesh(ShaderType shaderType, int capacity){
        this.shaderType = shaderType;
        this.capacity = capacity;
    }

    public Mesh(ShaderType shaderType, float[] vertex, int capacity){
        this(shaderType, capacity);
        this.vertex = vertex;
    }

    public Mesh(ShaderType shaderType, float[] vertex){
        this(shaderType, vertex, vertex.length / 3);
    }

    /* GETTERS */

    public Shader getShader(){
        return shaderType.getShader();
    }

    public int capacity(){
        return capacity;
    }

    /* DRAWING */

    public void draw(){
        Shader shader = getShader();
        shader.loadPosition(multiply(vertex, 4.0f));
        shader.enableAttributes();
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, capacity);
    }

    /* MISCELLANEOUS */

    protected float[] multiply(float[] vertex, float scalar){
        float[] newVertex = new float[vertex.length];
        for(int index = 0; index < vertex.length; index++){
            newVertex[index] = vertex[index] * scalar;
        }
        return newVertex;
    }
}

