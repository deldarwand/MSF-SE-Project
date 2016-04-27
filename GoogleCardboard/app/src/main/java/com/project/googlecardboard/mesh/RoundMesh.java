package com.project.googlecardboard.mesh;

import android.opengl.GLES20;

import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;
import com.project.googlecardboard.util.OpenGL;

/**
 * Created by Garrett on 10/01/2016.
 */
public class RoundMesh extends Mesh{

    public RoundMesh(float a, float b, float r){
        super(ShaderType.GRAPH, calculateCircle(a, b, (double) r));
    }

    public static float[] calculateCircle(float a, float b, double r){
        float[] vertex = new float[3 * 36 + 3 + 3];
        int index = 0;
        vertex[index++] = a;
        vertex[index++] = b;
        vertex[index++] = 0.0f;
        for(double theta = 0.0; theta <= 360.0; theta += 10.0){
            float y = (float) (Math.sin(Math.toRadians(theta)) * r);
            float x = (float) (Math.cos(Math.toRadians(theta)) * r);
            vertex[index++] = a + x;
            vertex[index++] = a + y;
            vertex[index++] = 0.0f;
        }
        return vertex;
    }

    public void draw(){
        Shader shader = getShader();
        OpenGL.checkGLError("RoundMesh", "Get Shader");
        shader.loadPosition(multiply(vertex, 4.0f));
        OpenGL.checkGLError("RoundMesh", "Load Position");
        shader.enableAttributes();
        OpenGL.checkGLError("RoundMesh", "Enable Attributes");
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, capacity);
        OpenGL.checkGLError("RoundMesh", "Draw Call");
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, this.capacity);
    }
}
