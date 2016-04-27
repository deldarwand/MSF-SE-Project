package com.project.googlecardboard.graph;

import com.project.googlecardboard.mesh.*;
import com.project.googlecardboard.render.shader.ShaderType;

/**
 * Created by Garrett on 24/11/2015.
 */

public class LineGraph implements Graph {

    private GraphMesh graphMesh;
    private GraphAxisMesh axisMesh;

    public LineGraph(int capacity){
        this.graphMesh = new GraphMesh(ShaderType.GRAPH, capacity);
        this.axisMesh = new GraphAxisMesh(ShaderType.GRAPH);
    }

    public void add(float value){
        graphMesh.addPoint(value);
    }

    /* DRAWING */

    public void draw(){
        axisMesh.draw();
        graphMesh.draw();
    }
}
