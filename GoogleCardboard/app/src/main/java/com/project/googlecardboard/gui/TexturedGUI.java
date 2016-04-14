package com.project.googlecardboard.gui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.IBinder;

import com.google.vrtoolkit.cardboard.HeadTransform;
import com.project.googlecardboard.WorldLayoutData;
import com.project.googlecardboard.matrix.TransformationMatrix;
import com.project.googlecardboard.mesh.QuadMesh;
import com.project.googlecardboard.mesh.triangleMesh;
import com.project.googlecardboard.projection.CameraUtil;
import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;
import com.project.googlecardboard.util.BitmapBuffer;
import com.project.googlecardboard.util.IO;
import android.opengl.Matrix;


/**
 * Created by danieleldar on 11/12/2015.
 */

public class TexturedGUI extends GUI {
    private final QuadMesh quad;
    private final GUITexture texture;
    private BitmapBuffer videoStream;
    private int newFrameNumber;
    private int oldFrameNumber;
    public HeadTransform headTran;
    private Bitmap[] frame = new Bitmap[49];

    CameraUtil cameraUtil;

    public TexturedGUI(float radius, float pitch, float yaw){
        super(radius, pitch, yaw, ShaderType.VIDEO);
        this.quad = new QuadMesh(shaderType);
        //this.tri = new arrowGUI(ShaderType.GRAPH);
        this.texture = new GUITexture();
        this.videoStream = new BitmapBuffer();
        this.newFrameNumber = 1;
        this.oldFrameNumber = 1;
        cameraUtil = CameraUtil.INSTANCE;


        init();
    }

    /* DRAWING */

/*
    public float[] getProjectionMatrix()
    {
        float[] projection = new float[16];
        float[] view = new float[16];

        Matrix.perspectiveM(projection, 0, 60.0f, 3.0f/4.0f, 0.1, 100.0f);
        Matrix.setLookAtM(view, 0, headTran.get);

    }*/

    public float[] getMatrixThis(){
        float[] tM = new float[3];
        headTran.getEulerAngles(tM, 0);
        TransformationMatrix matrix = new TransformationMatrix(getRadius(), 0.0f, tM[0]*-57.2958f, tM[1]*57.2958f + 180);
        return matrix.getMatrix();
//        TransformationMatrix matrix = new TransformationMatrix(this.getRadius(), 0.0f, this.getPitch(), 90 - this.getYaw() + 100);
//        return matrix.getMatrix();
    }

    public void draw(){
        Shader shader = getShader();
        shader.loadTransformationMatrix(getMatrixThis());
        shader.loadColour(isBeingViewed() ? WorldLayoutData.CUBE_FOUND_COLORS : WorldLayoutData.CUBE_COLORS);
        if(oldFrameNumber != newFrameNumber){
            updateTexture();
        }
        shader.loadTextureCoordinates(texture.getTextureCoordinates());
        shader.loadTexture(texture.getID());
        quad.draw();

        shader.loadTransformationMatrix(getMatrixThis());
        shader.loadTexture(0);
        //tri.draw();
    }

    /**
     * Update texture to latest frame
     */
    private void updateTexture(){
        texture.loadBitmap(videoStream.pull());
        oldFrameNumber = newFrameNumber;
    }

    /* UPDATE */

    public void update() {
        int frameNumber = newFrameNumber;
        frameNumber++;
        if(frameNumber > 48){
            frameNumber = 0;
        }
        Bitmap image = cameraUtil.getCurrentFrame();
        if (image != null) {
            videoStream.push(image);//frame[frameNumber]);
            newFrameNumber = frameNumber;
        }
        else
        {

        }
    }

    /* TEARDOWN */

    public void teardown(){
        videoStream.recycle();
        for(Bitmap bitmap : frame){
            bitmap.recycle();
        }
    }

    /* MISCElLANEOUS */

    /**
     * Initialsing the array of frames
     * Will be replaced as it will need
     * to receive frames sent over the
     * network
     */
    private void init(){
        for (int i = 1; i < 50; i++) {
            String ident;
            if (i < 10) {
                ident = "frame_0000" + i;
            } else {
                ident = "frame_000" + i;
            }
            frame[i-1] = IO.readBitmap(IO.readID(ident, "drawable"));;
        }
    }
}
