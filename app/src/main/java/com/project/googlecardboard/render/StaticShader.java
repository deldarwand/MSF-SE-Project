package com.project.googlecardboard.render;

/**
 * Created by Garrett on 13/11/2015.
 */
public class StaticShader extends Shader {

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationEye;
    private int locationLight;

    private int locationPosition;
    private int locationColour;
    private int locationNormal;

    public StaticShader(String vertexShaderCode, String fragmentShaderCode){
        super(vertexShaderCode, fragmentShaderCode);
    }

    @Override
    public void loadUniformLocations(){
        locationTransformationMatrix = getUniformLocation("transformationMatrix");
        locationProjectionMatrix = getUniformLocation("projectionMatrix");
        locationViewMatrix = getUniformLocation("viewMatrix");
        locationLight = getUniformLocation("light");
    }

    @Override
    public void loadAttributeLocations(){
        locationPosition = getAttributeLocation("position");
        locationColour = getAttributeLocation("colour");
        locationNormal = getAttributeLocation("normal");

        locationEye = getAttributeLocation("eye");
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
        loadAttributeMatrix(locationNormal, matrix, 3);
    }
}
