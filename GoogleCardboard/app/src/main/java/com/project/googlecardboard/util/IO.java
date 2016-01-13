package com.project.googlecardboard.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Vibrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Garrett on 07/01/2016.
 */
public class IO {

    private static Context context;
    private static Vibrator vibrator;

    public static void init(Context context, Vibrator vibrator){
        IO.context = context;
        IO.vibrator = vibrator;
    }

    /**
     * Reads a raw file as a String
     * @param resourceID The resource's ID
     * @return The source code, represented as a String
     */
    public static String readFile(int resourceID) {
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

    public static int readID(String identifier, String pakcage){
        return context.getResources().getIdentifier(identifier, pakcage, context.getPackageName());
    }

    public static Bitmap readBitmap(int resourceID){
        return BitmapFactory.decodeResource(context.getResources(), resourceID);
    }

    /**
     * Vibrates the phone
     * @param time Time in milliseconds
     */
    public static void vibrate(long time){
        vibrator.vibrate(time);
    }
}
