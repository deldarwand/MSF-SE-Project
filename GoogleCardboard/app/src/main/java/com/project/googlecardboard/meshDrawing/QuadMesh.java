package com.project.googlecardboard.meshDrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.project.googlecardboard.R;
import com.project.googlecardboard.WorldLayoutData;
import com.project.googlecardboard.gui.GUI;
import com.project.googlecardboard.gui.GUITexture;
import com.project.googlecardboard.matrix.ProjectionMatrix;
import com.project.googlecardboard.matrix.ViewMatrix;
import com.project.googlecardboard.render.StaticShader;

/**
 * Created by danieleldar on 11/12/2015.
 */

public class QuadMesh extends Mesh {

    GUITexture quadTexture;
    public int frameNumber = 1;
    private int currentFrameNumber = 1;
    public Bitmap bitmapToDisplay = null;

    public static final float[] textureCoordinates = {
            // Front face
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 0.0f
    };

    public int textureID;

    public QuadMesh(Context context, GUI gui)
    {
        super(context);
        arrayOfVertices = new float[] {
                -0.5f, -0.5f, -1.0f, // Bottom left corner
                -0.5f, 0.5f, -1.0f, // Top left corner
                0.5f, 0.5f, -1.0f, // Top Right corner
                0.5f, -0.5f, -1.0f, // Bottom right corner
                -0.5f, -0.5f, -1.0f, // Bottom left corner
                0.5f, 0.5f, -1.0f // Top Right corner
        };

        bitmapToDisplay = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame_00001);
        quadTexture = new GUITexture(bitmapToDisplay);
        this.textureID = quadTexture.getID();

        numberOfPoints = 6;
        shader = new StaticShader(readRawTextFile(R.raw.vertex), readRawTextFile(R.raw.fragment));
        setupShader(gui);

    }

    private synchronized void loadTexture()
    {
        //GUITexture newTex = new GUITexture(bitmapToDisplay);
        quadTexture.loadBitmap(bitmapToDisplay);
        //this.textureID = newTex.getID();
        //quadTexture = newTex;
        //bitmapToDisplay.recycle();
        currentFrameNumber = frameNumber;
    }

    public void draw(GUI gui, boolean isLookingAtGui, ViewMatrix viewMatrix, ProjectionMatrix projectionMatrix) {
        if (currentFrameNumber != frameNumber)
        {
            loadTexture();
        }
        shader.start();
        checkGLError("BeforeQQ");
        shader.loadPosition(arrayOfVertices);
        checkGLError("BeforeQ");
        shader.loadTransformationMatrix(gui.getMatrix());
        checkGLError("BeforeF");
        shader.loadViewMatrix(viewMatrix.getMatrix());
        checkGLError("BeforeE");
        shader.loadProjectionMatrix(projectionMatrix.getMatrix());
        checkGLError("BeforeD");
        shader.loadColour(isLookingAtGui ? WorldLayoutData.CUBE_FOUND_COLORS : WorldLayoutData.CUBE_COLORS);
        checkGLError("BeforeC");
        shader.loadTextureCoordinates(textureCoordinates);
        checkGLError("BeforeB");
        shader.loadTexture(textureID);
        checkGLError("BeforeA");
        shader.enableAttributes();
        checkGLError("Before");
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, this.numberOfPoints);
        checkGLError("After");
    }



}
