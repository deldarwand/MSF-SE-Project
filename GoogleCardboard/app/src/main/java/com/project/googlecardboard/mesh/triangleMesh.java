package com.project.googlecardboard.mesh;

import android.opengl.GLES20;

import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;

/**
 * Created by danieleldar on 16/03/2016.
 */
public class triangleMesh extends Mesh {
    private static final float[] TRI = new float[]{
            -0.5f, -0.5f, 0.0f,     // Bottom left corner
            -0.5f, 0.5f, 0.0f,      // Top left corner
            0.5f, 0.5f, 0.0f,       // Top Right corner
    };

    public triangleMesh(ShaderType shaderType){
        super(shaderType, TRI, TRI.length/3);
    }

    public void draw(){
        Shader shader = getShader();
        shader.loadPosition(multiply(vertex, 4.0f));
        shader.enableAttributes();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, this.capacity);
    }
}
