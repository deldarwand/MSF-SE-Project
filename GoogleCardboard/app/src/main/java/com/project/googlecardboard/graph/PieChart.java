package com.project.googlecardboard.graph;

import com.project.googlecardboard.mesh.RoundMesh;

/**
 * Created by Garrett on 10/01/2016.
 */
public class PieChart implements Graph{

    private RoundMesh pieMesh;

    public PieChart(){
        this.pieMesh = new RoundMesh(0.0f, 0.0f, 0.5f);
    }

    public void add(float value){

    }

    public void draw(){
        pieMesh.draw();
    }
}
