package com.project.googlecardboard.render;

import android.opengl.GLES20;

/**
 * Created by Garrett on 13/11/2015.
 */
public class StaticShader extends Shader {

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    //private int locationLight;
    //private int locationTexture;

    private int locationPosition;
    //private int locationTextureCoordinates;
    private int locationColour;
    //private int locationNormal;

    public StaticShader(String vertexShaderCode, String fragmentShaderCode){
        super(vertexShaderCode, fragmentShaderCode);
    }

    @Override
    public void loadUniformLocations(){
        locationTransformationMatrix = getUniformLocation("transformationMatrix");
        locationProjectionMatrix = getUniformLocation("projectionMatrix");
        locationViewMatrix = getUniformLocation("viewMatrix");
       // locationLight = getUniformLocation("light");

        //locationTexture = getUniformLocation("texture");
    }

    @Override
    public void loadAttributeLocations(){
        locationPosition = getAttributeLocation("position");
        //locationTextureCoordinates = getAttributeLocation("textureCoordinates");
        locationColour = getAttributeLocation("colour");
        //locationNormal = getAttributeLocation("normal");
    }

    @Override
    public void enableAttributes(){
        GLES20.glEnableVertexAttribArray(locationPosition);
       // GLES20.glEnableVertexAttribArray(locationTextureCoordinates);
        GLES20.glEnableVertexAttribArray(locationColour);
        //GLES20.glEnableVertexAttribArray(locationNormal);
    }

    public void loadTransformationMatrix(float[] matrix){
        loadUniformMatrix(locationTransformationMatrix, matrix);
    }

    public void loadProjectionMatrix(float[] matrix){
        loadUniformMatrix(locationProjectionMatrix, matrix);
    }

    public void loadViewMatrix(float[] matrix){
        loadUniformMatrix(locationViewMatrix, matrix);
    }

    public void loadPosition(float[] matrix){
        loadAttributeMatrix(locationPosition, matrix, 3);
    }

    public void loadColour(float[] matrix) {
        loadAttributeMatrix(locationColour, matrix, 4);
    }

    public void loadNormal(float[] matrix){
      //  loadAttributeMatrix(locationNormal, matrix, 3);
    }

    public void loadTexture(int id){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, id);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
        //GLES20.glUniform1i(locationTexture, 0);
    }

    public void loadTextureCoordinates(float[] matrix){
//        loadAttributeMatrix(locationTextureCoordinates, matrix, 2);
    }
}
