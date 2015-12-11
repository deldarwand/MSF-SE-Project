package com.project.googlecardboard.meshDrawing;

import android.content.Context;
import android.opengl.GLES20;

import com.project.googlecardboard.WorldLayoutData;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.matrix.ProjectionMatrix;
import com.project.googlecardboard.matrix.ViewMatrix;

/**
 * Created by danieleldar on 11/12/2015.
 */
public class GraphAxisMesh extends Mesh {


    public GraphAxisMesh(Context context, GUI gui)
    {
        super(context);
        arrayOfVertices = new float[] {1.0f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f};
        this.numberOfPoints = 4;
        setupShader(gui);
    }

}