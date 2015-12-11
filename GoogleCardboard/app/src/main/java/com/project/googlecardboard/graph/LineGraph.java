package com.project.googlecardboard.graph;

import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.sensors.HeadTracker;
import com.project.googlecardboard.R;
import com.project.googlecardboard.WorldLayoutData;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.matrix.ProjectionMatrix;
import com.project.googlecardboard.matrix.ViewMatrix;
import com.project.googlecardboard.render.StaticShader;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.project.googlecardboard.render.Renderer;
import com.project.googlecardboard.render.Shader;
import com.project.googlecardboard.render.StaticShader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Garrett on 24/11/2015.
 */

class Mesh extends Object {
    public volatile float[] arrayOfVertices;

    protected StaticShader shader;
    protected Context context;
    private int numberOfPoints, numberOfPointsFilled;
    private volatile int index = 0;
    Thread runningThread = null;

    public Mesh(Context context)
    {
        this.context = context;
        shader = new StaticShader(readRawTextFile(R.raw.vertex), readRawTextFile(R.raw.fragment));
    }

    public void addPoint(float newPoint)
    {
        float[] newArrayOfVertices = new float[numberOfPoints*3];
        if (numberOfPointsFilled == numberOfPoints)
        {
            System.arraycopy(arrayOfVertices, 3, newArrayOfVertices, 0, (numberOfPoints-1)*3);
            numberOfPointsFilled--;
        }
        else
        {
            System.arraycopy(arrayOfVertices, 0, newArrayOfVertices, 0, numberOfPoints*3);
        }
        for (int i = 0; i < numberOfPoints*3; i +=3)
        {
            newArrayOfVertices[i] = 1.0f - (((float)i/3.0f)*1.0f / (float)numberOfPoints); //X-Position of points.
        }
        newArrayOfVertices[(numberOfPointsFilled*3)+1] = newPoint;
        numberOfPointsFilled++;

        synchronized (this) {
            arrayOfVertices = newArrayOfVertices;
        }
    }

    public Mesh(Context context, int numberOfPoints, GUI gui)
    {
        this(context);
        this.numberOfPoints = numberOfPoints;
        this.numberOfPointsFilled = 0;
        arrayOfVertices = new float[numberOfPoints*3];
        for (int i = 0; i < numberOfPoints*3; i++)
        {
            switch (i%3) {
                case 0: {
                    arrayOfVertices[i] = 1.0f - (((float)i/3.0f)*1.0f / (float)numberOfPoints); //X-Position of points.
                }
                break;

                case 1: {
                    arrayOfVertices[i] = -1.0f; //These need to be filled with the y information for the graph.
                }
                break;

                case 2: {
                    arrayOfVertices[i] = 0.0f; //Z-Position of the points. All 0 as graph is 2D
                }
                break;

                default:{} break;
            }
        }

        setupShader(gui);
    }

    private String readRawTextFile(int resourceID) {
        InputStream inputStream = context.getResources().openRawResource(resourceID);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setupShader(GUI gui)
    {
        shader.init();
        shader.loadUniformLocations();
        checkGLError("A");
        shader.loadAttributeLocations();
        checkGLError("B");

    }

    public void checkGLError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("MyApp", op + ": glError " + error);
        }
    }

    public void draw(GUI gui, boolean isLookingAtGui, ViewMatrix viewMatrix, ProjectionMatrix projectionMatrix) {
        shader.start();
        shader.loadPosition(arrayOfVertices);
        shader.loadTransformationMatrix(gui.getMatrix());
        shader.loadViewMatrix(viewMatrix.getMatrix());
        shader.loadProjectionMatrix(projectionMatrix.getMatrix());
        shader.loadColour(isLookingAtGui ? WorldLayoutData.CUBE_FOUND_COLORS : WorldLayoutData.CUBE_COLORS);
        shader.enableAttributes();
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, this.numberOfPoints);
        //shader.stop();
    }

    public void teardown()
    {
        shader.clean();
    }

}

class graphAxis extends Mesh {

    //drawLine(0.0f, 0.0f, 1.0f, 0.0f, 0);
    //drawLine(0.0f, 0.0f, 0.0f, 1.0f, 6);


    public graphAxis(Context context, GUI gui)
    {
        super(context);
        arrayOfVertices = new float[] {1.0f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f};
        setupShader(gui);
    }

    public void draw(GUI gui, boolean isLookingAtGui, ViewMatrix viewMatrix, ProjectionMatrix projectionMatrix) {
        shader.start();
        shader.loadPosition(arrayOfVertices);
        checkGLError("C");
        shader.loadViewMatrix(viewMatrix.getMatrix());
        checkGLError("E");
        shader.loadProjectionMatrix(projectionMatrix.getMatrix());
        checkGLError("F");
        shader.loadTransformationMatrix(gui.getMatrix());
        checkGLError("D");
        shader.loadColour(isLookingAtGui ? WorldLayoutData.CUBE_FOUND_COLORS : WorldLayoutData.CUBE_COLORS);
        checkGLError("F");
        shader.loadTexture(gui.getTexture().getID());
        checkGLError("G");
        shader.enableAttributes();
        checkGLError("G1");
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, 4);
        checkGLError("H");
        //shader.stop();
    }

}

public class LineGraph extends Object implements Runnable {

    private Mesh graphMesh;
    private graphAxis axis;
    private GUI guiParent;

    public LineGraph(int size, Context context, GUI gui)
    {
        this.axis = new graphAxis(context, gui);
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
        //clear();
        //drawAxes();
        //Queue<Float> dataCopy = new LinkedList<Float>();
        //float increment = 1.0f / 10.0f;

        //Float[] array = new Float[data.size()];
        //array = data.toArray(array);

        /*for(int i = 0, j = 1; j < arrayOfPoints.length; i++, j++){
            float y1 = arrayOfPoints[i];
            float y2 = arrayOfPoints[j];
            drawLine(i*increment, y1, j*increment, y2, (i*6)+12);
            //dataCopy.offer(y1);
            //drawLine(i, data.get(i), j, data.get(j));
        }*/
        //data = dataCopy;
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
