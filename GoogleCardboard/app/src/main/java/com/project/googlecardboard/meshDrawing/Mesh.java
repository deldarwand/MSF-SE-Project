package com.project.googlecardboard.meshDrawing;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.project.googlecardboard.R;
import com.project.googlecardboard.WorldLayoutData;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.matrix.ProjectionMatrix;
import com.project.googlecardboard.matrix.ViewMatrix;
import com.project.googlecardboard.render.StaticShader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by danieleldar on 11/12/2015.
 */
public class Mesh extends Object {
    public volatile float[] arrayOfVertices;

    protected StaticShader shader;
    protected Context context;
    protected int numberOfPoints;
    private int numberOfPointsFilled;
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
    }

    public void teardown()
    {
        shader.clean();
    }

}

