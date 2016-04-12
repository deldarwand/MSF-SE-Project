package com.project.googlecardboard.gui;

import android.graphics.Bitmap;

import com.project.googlecardboard.WorldLayoutData;
import com.project.googlecardboard.matrix.TransformationMatrix;
import com.project.googlecardboard.mesh.ArrowMesh;
import com.project.googlecardboard.mesh.triangleMesh;
import com.project.googlecardboard.projection.CameraUtil;
import com.project.googlecardboard.render.shader.Shader;
import com.project.googlecardboard.render.shader.ShaderType;
import com.project.googlecardboard.util.BitmapBuffer;
import java.math.*;

/**
 * Created by danieleldar on 22/03/2016.
 */
public class ArrowGUI extends GUI {

    private ArrowMesh arrowMesh;
    private float landmarkLongitude, landmarkLatitude;
    private float currentLongitude, currentLatitude;
    static final float earthRadius = 6371000.0f;


    public ArrowGUI(float radius, float pitch, float yaw, float longitude, float latitude, float cLongitude, float cLatitude){
        super(radius, pitch, yaw, ShaderType.LIGHT);
        landmarkLongitude = longitude;
        landmarkLatitude = latitude;
        currentLongitude = cLongitude;
        currentLatitude = cLatitude;
        arrowMesh = new ArrowMesh(shaderType);
    }


    /* DRAWING */

    public void updateArrow(float newCurrentLongitude, float newCurrentLatitude)
    {
        currentLongitude = newCurrentLongitude;
        currentLatitude = newCurrentLatitude;
    }

    public float[] getMatrix(){
        float x = earthRadius * (landmarkLongitude-currentLongitude) * (float)Math.cos(landmarkLatitude-currentLatitude);
        float y = earthRadius * (landmarkLatitude-currentLatitude);
        float newRadius = (float)Math.sqrt((x*x)/*squared*/ + (y*y))/*squared*/; //SQRT.




        float angle = (float)Math.atan(y / x);
/*
        if (x>0.0f)
        angle=(float)Math.atan(y / x);

        if ((y>=0.0f) && (x<0.0f))
        angle=(float)Math.PI+(float)Math.atan(y / x);

        if ((y<0.0f) && (x<0.0f))
        angle=-(float)Math.PI+(float)Math.atan(y / x);

        if ((y>0.0f) && (x==0.0f))
        angle=(float)Math.PI/2;

        if ((y<0.0f) && (x==0.0f))
        angle=-(float)Math.PI/2;

        if (angle<0.0f)
        angle=angle+2*(float)Math.PI;

*/

        angle *= 57.0f;



        setRadius(newRadius);
        TransformationMatrix matrix = new TransformationMatrix(getRadius(), 0.0f, getPitch(), 90 - angle + 100);
        return matrix.getMatrix();
    }

    public void draw(){
        Shader shader = getShader();
        shader.loadTransformationMatrix(getMatrix());
        shader.loadColour(isBeingViewed() ? WorldLayoutData.CUBE_FOUND_COLORS : WorldLayoutData.CUBE_COLORS);

        arrowMesh.draw();
    }

    /* UPDATE */

    public void update() {

    }

    public void teardown()
    {}


}
