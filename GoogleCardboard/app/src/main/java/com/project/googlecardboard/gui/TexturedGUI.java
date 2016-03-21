package com.project.googlecardboard.gui;

import android.graphics.Bitmap;

import com.project.googlecardboard.WorldLayoutData;
import com.project.googlecardboard.mesh.QuadMesh;
import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;
import com.project.googlecardboard.util.BitmapBuffer;
import com.project.googlecardboard.util.IO;

import pl.packet.Packet;

/**
 * Created by danieleldar on 11/12/2015.
 */

public class TexturedGUI extends GUI {
    private final QuadMesh quad;
    private final GUITexture texture;
    private BitmapBuffer videoStream;
    private int newFrameNumber;
    private int oldFrameNumber;

    private Bitmap[] frame = new Bitmap[49];

    public TexturedGUI(float radius, float pitch, float yaw){
        super(radius, pitch, yaw, ShaderType.VIDEO);
        this.quad = new QuadMesh(shaderType);
        this.texture = new GUITexture();
        this.videoStream = new BitmapBuffer();
        this.newFrameNumber = 1;
        this.oldFrameNumber = 1;

        init();
    }

    /* DRAWING */

    public void draw(){
        Shader shader = getShader();
        shader.loadTransformationMatrix(getMatrix());
        shader.loadColour(isBeingViewed() ? WorldLayoutData.CUBE_FOUND_COLORS : WorldLayoutData.CUBE_COLORS);
        if(oldFrameNumber != newFrameNumber){
            updateTexture();
        }
        shader.loadTextureCoordinates(texture.getTextureCoordinates());
        shader.loadTexture(texture.getID());
        quad.draw();
    }

    /**
     * Update texture to latest frame
     */
    private void updateTexture(){
        texture.loadBitmap(videoStream.pull());
        oldFrameNumber = newFrameNumber;
    }

    /* UPDATE */

    public void update(Packet packet) {
        int frameNumber = newFrameNumber;
        frameNumber++;
        if(frameNumber > 48){
            frameNumber = 0;
        }
        videoStream.push(frame[frameNumber]);
        newFrameNumber = frameNumber;
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
