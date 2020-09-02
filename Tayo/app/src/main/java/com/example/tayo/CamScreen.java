package com.example.tayo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class CamScreen extends Activity {

    class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        SurfaceHolder mHolder;
        Camera mCamera;

        public MySurfaceView(Context context) {
            super(context);
            mHolder = getHolder();
            mHolder.addCallback(this);
        }

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                mCamera.release();
                mCamera = null;
            }
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
            mCamera.startPreview();
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
            }
        }

        MySurfaceView mf = new MySurfaceView(this);
        setContentView(mf);
    }
}
