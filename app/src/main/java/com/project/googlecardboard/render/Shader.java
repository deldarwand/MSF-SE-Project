package com.project.googlecardboard.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.project.googlecardboard.WorldLayoutData;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * The shader program
 *
 * Created by Garrett on 22/10/2015.
 */
public abstract class Shader {

    public static final int VERTEX_SHADER = GLES20.GL_VERTEX_SHADER;
    public static final int FRAGMENT_SHADER = GLES20.GL_FRAGMENT_SHADER;

    private final String vertexShaderCode;
    private final String fragmentShaderCode;

    private int vertexShaderID;
    private int fragmentShaderID;
    private int shaderID;

    public Shader(String vertexShaderCode, String fragmentShaderCode){
        this.vertexShaderCode = vertexShaderCode;
        this.fragmentShaderCode = fragmentShaderCode;
    }

    /**
     * Initialises the shader
     */
    public void init(){
        vertexShaderID = loadGLShader(VERTEX_SHADER, vertexShaderCode);
        fragmentShaderID = loadGLShader(FRAGMENT_SHADER, fragmentShaderCode);
        shaderID = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderID, vertexShaderID);
        GLES20.glAttachShader(shaderID, fragmentShaderID);

        GLES20.glBindAttribLocation(shaderID, 0, "position");

        GLES20.glLinkProgram(shaderID);
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

    /**
     * Loads all uniform variables
     */
    public abstract void loadUniformLocations();

    /**
     * Loads all attribute variables
     */
    public abstract void loadAttributeLocations();

    /**
     * Enables attribute variables in draw call
     */
    public abstract void enableAttributes();

    /**
     * Loads a uniform variable
     * @param name
     * @return
     */
    protected int getUniformLocation(String name){
        return GLES20.glGetUniformLocation(shaderID, name);
    }

    /**
     * Loads an attribute variable
     * @param name
     * @return
     */
    protected int getAttributeLocation(String name){
        return GLES20.glGetAttribLocation(shaderID, name);
    }

    /**
     * Loads a unfirom matrix
     * @param location
     * @param matrix
     */
    protected void loadUniformMatrix(int location, float[] matrix){
        GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0);
    }

    /**
     * Loads an attribute matrix
     * @param location
     * @param matrix
     * @param perCoord
     */
    protected void loadAttributeMatrix(int location, float[] matrix, int perCoord){
        GLES20.glVertexAttribPointer(location, perCoord, GLES20.GL_FLOAT, false, 0, arrayToBuffer(matrix));
    }

    /**
     * Converts a float array to a float buffer
     * @param matrix
     * @return
     */
    private FloatBuffer arrayToBuffer(float[] matrix){
        ByteBuffer buffer = ByteBuffer.allocateDirect(matrix.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = buffer.asFloatBuffer();
        floatBuffer.put(matrix);
        floatBuffer.position(0);
        return floatBuffer;
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
            //throw new RuntimeException("Error creating shader.");
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