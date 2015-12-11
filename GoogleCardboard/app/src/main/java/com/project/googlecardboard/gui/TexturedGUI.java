package com.project.googlecardboard.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.project.googlecardboard.meshDrawing.QuadMesh;
/**
 * Created by danieleldar on 11/12/2015.
 */

public class TexturedGUI extends GUI implements Runnable
{
    public QuadMesh quad;
    Context context;
    Bitmap[] arrayOfFrames = new Bitmap[49];


    public TexturedGUI(float radius, float pitch, float yaw, Context context)
    {
        super(radius, pitch, yaw, context);
        this.context = context;
        quad = new QuadMesh(context, this);

        for (int i = 1; i < 50; i++)
        {
            String ident;
            if (i < 10)
            {
                ident = "frame_0000" + i;
            }
            else {
                ident = "frame_000" + i;
            }

            int id = context.getResources().getIdentifier(ident, "drawable", context.getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
            arrayOfFrames[i-1] = bitmap;
        }

    }

    @Override
    public void run()
    {
        int fN = quad.frameNumber;
        fN++;
        if (fN > 48)
        {
            fN = 0;
        }
        /*String ident;
        if (fN < 10)
        {
            ident = "frame_0000" + fN;
        }
        else {
            ident = "frame_000" + fN;
        }

        int id = context.getResources().getIdentifier(ident, "drawable", context.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);*/
        quad.bitmapToDisplay = arrayOfFrames[fN];

        quad.frameNumber = fN;
        //bitmap.recycle();


    }
}
