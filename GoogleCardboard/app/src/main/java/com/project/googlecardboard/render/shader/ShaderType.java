package com.project.googlecardboard.render.shader;

import com.project.googlecardboard.matrix.ProjectionMatrix;
import com.project.googlecardboard.matrix.ViewMatrix;
import com.project.googlecardboard.util.IO;
import com.project.googlecardboard.R;

/**
 * Created by Garrett on 07/01/2016.
 */
public enum ShaderType {

    GRAPH(new StaticShader(IO.readFile(R.raw.vertex), IO.readFile(R.raw.graph_fragment))),
    VIDEO(new StaticShader(IO.readFile(R.raw.vertex), IO.readFile(R.raw.fragment)));

    private final StaticShader shader;
    private boolean isBeingUsed;

    private ShaderType(StaticShader shader){
        this.shader = shader;
        shader.init();
        shader.loadUniformLocations();
        shader.loadAttributeLocations();
        this.isBeingUsed = false;
    }

    public Shader getShader(){
        return shader;
    }

    public void start(){
        if(!isBeingUsed){
            for(ShaderType shaderType : values()){
                shaderType.isBeingUsed = false;
            }
            shader.start();
            isBeingUsed = true;
        }
    }
}
