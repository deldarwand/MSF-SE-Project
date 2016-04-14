package com.project.googlecardboard.projection;


import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.graphics.Matrix;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class CameraUtil extends Object {
    private static final String TAG = "VideoProcessing";
    private static final int CAMERA = CameraCharacteristics.LENS_FACING_BACK;
    private CameraDevice camera;
    private CameraCaptureSession session;
    private ImageReader imageReader;
    private Bitmap currentFrame;
    private Activity activity;
    public static CameraUtil INSTANCE;


    public CameraUtil(Activity ac)
    {
        if (INSTANCE == null) {
            activity = ac;
            onCreate();
            INSTANCE = this;
        }
    }


    //CAMERA PARAMETERS


    private CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            CameraUtil.this.camera = camera;
            try {
                camera.createCaptureSession(Arrays.asList(imageReader.getSurface()), sessionStateCallback, null);
            } catch (CameraAccessException e){
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {}

        @Override
        public void onError(CameraDevice camera, int error) {}
    };

    private CameraCaptureSession.StateCallback sessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            CameraUtil.this.session = session;
            try {
                session.setRepeatingRequest(createCaptureRequest(), null, null);
            } catch (CameraAccessException e){
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {}
    };

    private ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader){
            Image img = reader.acquireLatestImage();
            processImage(img);
            img.close();
        }
    };

    public void onCreate() {
        Log.d(TAG, "Service Started");

        CameraManager manager = (CameraManager) activity.getSystemService(activity.CAMERA_SERVICE);
        try {

            manager.openCamera(getCamera(manager), cameraStateCallback, null);
            imageReader = ImageReader.newInstance(320, 240, ImageFormat.JPEG, 30 * 600); //fps * 10 min
            imageReader.setOnImageAvailableListener(onImageAvailableListener, null);
        } catch (CameraAccessException e){
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     *  Return the Camera Id which matches the field CAMERA.
     */
    public String getCamera(CameraManager manager){
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cOrientation == CAMERA) {
                    return cameraId;
                }
            }
        } catch (CameraAccessException e){
            e.printStackTrace();
        }
        return null;
    }

    public void onDestroy() {
        try {
            session.abortCaptures();
        } catch (CameraAccessException e){
            Log.e(TAG, e.getMessage());
        }
        session.close();
    }

    /**
     *  Process image data as desired.
     */
    private void processImage(Image image){
        //Process image data
        System.out.print("Camera Image" + image);

        Bitmap bitmap = null;
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
         try {
             final Image.Plane[] planes = image.getPlanes();
             final ByteBuffer buffer = planes[0].getBuffer();
             final byte[] data = new byte[buffer.capacity()];
             buffer.get(data);
             bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }

        Matrix matrix = new Matrix();
            matrix.preScale(-1.0f, 1.0f);
        // return transformed image
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        /*ByteBuffer buffer = planes[0].getBuffer();
        for (int i = 0; i < imageHeight; ++i) {
            for (int j = 0; j < imageWidth; ++j) {
                int pixel = 0;
                pixel |= (buffer.get(offset) & 0xff) << 16;     // R
                pixel |= (buffer.get(offset + 1) & 0xff) << 8;  // G
                pixel |= (buffer.get(offset + 2) & 0xff);       // B
                pixel |= (buffer.get(offset + 3) & 0xff) << 24; // A
                newFrameBitmap.setPixel(j, i, pixel);
                offset += pixelStride;
            }
            offset += rowPadding;
        }*/
            currentFrame = bitmap;
    }



    private CaptureRequest createCaptureRequest() {
        try {
            CaptureRequest.Builder builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_MANUAL);
            builder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO);
            builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
            builder.addTarget(imageReader.getSurface());
            return builder.build();
        } catch (CameraAccessException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public Bitmap getCurrentFrame()
    {
            return currentFrame;
    }
}