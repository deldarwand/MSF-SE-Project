package com.project.googlecardboard.render.shader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Garrett on 10/01/2016.
 */
public class ShaderManager {

    private Map<ShaderType, Shader> shaders;

    public ShaderManager(){
        this.shaders = new HashMap<>();
    }

    public Shader getShader(ShaderType shaderType){
        return shaders.get(shaderType);
    }

}
