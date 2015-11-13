package com.project.googlecardboard.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.Buffer;
import java.nio.FloatBuffer;

/**
 * The shader program
 *
 * Created by Garrett on 22/10/2015.
 */
public class Shader {

    public static final int VERTEX_SHADER = GLES20.GL_VERTEX_SHADER;
    public static final int FRAGMENT_SHADER = GLES20.GL_FRAGMENT_SHADER;

    private final int vertexShaderID;
    private final int fragmentShaderID;
    private final int shaderID;

    private int locationPosition;
    private int locationEye;

    public Shader(String vertexShaderCode, String fragmentShaderCode){
        this.vertexShaderID = loadGLShader(VERTEX_SHADER, vertexShaderCode);
        this.fragmentShaderID = loadGLShader(FRAGMENT_SHADER, fragmentShaderCode);
        this.shaderID = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderID, vertexShaderID);
        GLES20.glAttachShader(shaderID, fragmentShaderID);

        GLES20.glBindAttribLocation(shaderID, 0, "position");

        GLES20.glLinkProgram(shaderID);

        locationPosition = getAttributeLocation("position");
        locationEye = getAttributeLocation("eye");
    }

    /**
     * Starts the shader
     */
    public void start(){
        GLES20.glUseProgram(shaderID);
    }

    /**
     * Stops the shader
     */
    public void stop(){
        GLES20.glUseProgram(0);
    }

    private int getUniformLocation(String name){
        return GLES20.glGetUniformLocation(shaderID, name);
    }

    private int getAttributeLocation(String name){
        return GLES20.glGetAttribLocation(shaderID, name);
    }

    public void loadMatrix(int location, float[] matrix){
        FloatBuffer buffer = FloatBuffer.allocate(16);
        buffer.put(matrix);
        buffer.flip();
        GLES20.glUniformMatrix4fv(location, 0, false, buffer);
    }

    /**
     * Loads a shader type (vertex shader or fragment shader)
     * @param type The type of shader to load (vertex shader or fragment shader)
     * @param code The source code represented as a String
     * @return The created shader's ID
     */
    private int loadGLShader(int type, String code){
        int shaderID = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shaderID, code);
        GLES20.glCompileShader(shaderID);
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderID, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if(compileStatus[0] == 0){
            Log.e("Shader", "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderID));
            GLES20.glDeleteShader(shaderID);
            shaderID = 0;
        }
        if(shaderID == 0){
            throw new RuntimeException("Error creating shader.");
        }
        return shaderID;
    }

    /**
     * Cleans up the shader
     */
    public void clean(){
        stop();
        GLES20.glDetachShader(shaderID, vertexShaderID);
        GLES20.glDetachShader(shaderID, fragmentShaderID);
        GLES20.glDeleteShader(vertexShaderID);
        GLES20.glDeleteShader(fragmentShaderID);
        GLES20.glDeleteProgram(shaderID);
    }
}