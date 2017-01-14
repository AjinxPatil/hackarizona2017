package com.example.ghanshyam.depression;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TransparentActivity extends Activity {
    private Camera mCam;
    private MirrorView mCamPreview;
    private int mCameraId = 0;
    private FrameLayout mPreviewLayout;
    BroadcastReceiver broadcastReceiver;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        mCameraId = findFirstFrontFacingCamera();

        mPreviewLayout = (FrameLayout) findViewById(R.id.camPreview);
        mPreviewLayout.removeAllViews();
        button= (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCam.takePicture(null,pictureCallback,pictureCallback);
            }
        });
        mPreviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCam.takePicture(null,pictureCallback,pictureCallback);
            }
        });

        startCameraInLayout(mPreviewLayout, mCameraId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            //Do something
            mCam.takePicture(null,pictureCallback,pictureCallback);
        }
        return true;
    }

    private int findFirstFrontFacingCamera() {
        int foundId = -1;
        int numCams = Camera.getNumberOfCameras();
        for (int camId = 0; camId < numCams; camId++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(camId, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                foundId = camId;
                break;
            }
        }
        return foundId;
    }
    private void startCameraInLayout(FrameLayout layout, int cameraId) {
        mCam = Camera.open(cameraId);
        if (mCam != null) {
            Camera.Parameters parameters = mCam.getParameters();
            parameters.setPictureFormat(PixelFormat.JPEG);
            mCam.setParameters(parameters);
            mCam.setDisplayOrientation(90);
            mCamPreview = new MirrorView(this, mCam);
            layout.addView(mCamPreview);
        }
    }

    Camera.PictureCallback pictureCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);
                Matrix matrix = new Matrix();
                matrix.postRotate(270);
                bitmap= Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                        matrix, true);
                if(bitmap!=null){

                    File file=new File(Environment.getExternalStorageDirectory()+"/dirr");
                    if(!file.isDirectory()){
                        file.mkdir();
                    }

                    file=new File(Environment.getExternalStorageDirectory()+"/dirr",System.currentTimeMillis()+".jpg");


                    try
                    {
                        FileOutputStream fileOutputStream=new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100, fileOutputStream);


                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                    catch(Exception exception)
                    {
                        exception.printStackTrace();
                    }

                }
            }
        }

    };

    public class MirrorView extends SurfaceView implements
            SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;

        public MirrorView(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera.setPreviewDisplay(holder);


                mCamera.startPreview();
            } catch (Exception error) {
                Log.d("error",
                        "Error starting mPreviewLayout: " + error.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }


        public void surfaceChanged(SurfaceHolder holder, int format, int w,
                                   int h) {
            if (mHolder.getSurface() == null) {
                return;
            }

            // can't make changes while mPreviewLayout is active
            try {
                mCamera.stopPreview();
            } catch (Exception e) {

            }

            try {

                // start up the mPreviewLayout
                mCamera.setPreviewDisplay(mHolder);

                mCamera.startPreview();

            } catch (Exception error) {
                Log.d("error",
                        "Error starting mPreviewLayout: " + error.getMessage());
            }
        }
    }
}
