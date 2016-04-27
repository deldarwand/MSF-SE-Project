package com.project.googlecardboard.mesh;

import android.opengl.GLES20;

import com.project.googlecardboard.R;
import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;
import com.project.googlecardboard.util.IO;

/**
 * Created by danieleldar on 22/03/2016.
 */
public class ArrowMesh extends Mesh {
    private static final float[] ARROW = new float[]{
            0.0f, 0.5f, 0.0f,
            0.0f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.0f,
            0.0f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.0f,
            0.5f, 0.5f, 0.5f, //Top square

            0.0f, 0.5f, 0.0f,
            0.0f, 0.5f, 0.5f,
            0.25f, 0.0f, 0.25f, //left tri

            0.0f, 0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
            0.25f, 0.0f, 0.25f, //front tri

            0.0f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.25f, 0.0f, 0.25f, //back tri

            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.25f, 0.0f, 0.25f, //right tri

    };

    public ArrowMesh(ShaderType shaderType){
        super(shaderType, IO.readFile(R.raw.arrow_model));

    }

    public void draw(){
        Shader shader = getShader();
        shader.loadPosition(multiply(vertex, 0.1f));
        shader.loadNormal(normal);
        shader.enableAttributes();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, this.capacity);
    }


}
