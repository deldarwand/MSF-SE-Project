package com.project.googlecardboard.mesh;

import android.opengl.GLES20;

import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;

/**
 * Created by danieleldar on 11/12/2015.
 */

public class QuadMesh extends Mesh {

    private static final float[] QUAD = new float[]{
        -0.5f, -0.5f, 0.0f,     // Bottom left corner
        -0.5f, 0.5f, 0.0f,      // Top left corner
        0.5f, 0.5f, 0.0f,       // Top Right corner
        0.5f, -0.5f, 0.0f,      // Bottom right corner
        -0.5f, -0.5f, 0.0f,     // Bottom left corner
        0.5f, 0.5f, 0.0f        // Top Right corner
    };

    public QuadMesh(ShaderType shaderType){
        super(shaderType, QUAD, 6);
    }

    public void draw(){
        Shader shader = getShader();
        shader.loadPosition(multiply(vertex, 14.0f));
        shader.enableAttributes();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, this.capacity);
    }

   /* public QuadMesh(Context context, GUI gui)
    {
        bitmapToDisplay = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame_00001);

    }*/
}
