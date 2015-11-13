package com.project.googlecardboard.render;

/**
 * Created by Garrett on 13/11/2015.
 */
public class StaticShader extends Shader {

    public StaticShader(String vertexShaderCode, String fragmentShaderCode){
        super(vertexShaderCode, fragmentShaderCode);
    }

    @Override
    public void loadUniformLocations(){

    }

    @Override
    public void loadAttributeLocations(){

    }
}
