package com.project.googlecardboard.graph;

import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.sensors.HeadTracker;
import com.project.googlecardboard.R;
import com.project.googlecardboard.WorldLayoutData;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.matrix.ProjectionMatrix;
import com.project.googlecardboard.matrix.ViewMatrix;
import com.project.googlecardboard.meshDrawing.*;
import com.project.googlecardboard.render.StaticShader;


import android.content.Context;
import java.util.Random;

/**
 * Created by Garrett on 24/11/2015.
 */

public class LineGraph extends Object implements Runnable {

    private Mesh graphMesh;
    private GraphAxisMesh axis;
    private GUI guiParent;

    public LineGraph(int size, Context context, GUI gui)
    {
        this.axis = new GraphAxisMesh(context, gui);
        this.graphMesh = new Mesh(context, size, gui);
        this.guiParent = gui;
        //super(size);
    }

    public void addData()
    {

    }

    public void draw(float[] headView, ViewMatrix viewMatrix, ProjectionMatrix projectionMatrix)
    {
        this.axis.draw(guiParent, guiParent.isLookingAtMe(headView), viewMatrix, projectionMatrix);
        this.graphMesh.draw(guiParent, guiParent.isLookingAtMe(headView), viewMatrix, projectionMatrix);
    }

    public void clean()
    {
        axis.teardown();
        graphMesh.teardown();
    }

    Thread runningThread = null;

    @Override
    public void run()
    {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        runningThread = Thread.currentThread();
        Random rand = new Random();
        graphMesh.addPoint(rand.nextFloat());
    }
}
