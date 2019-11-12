/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xianzhi.baidumap.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xianzhi.baidumap.R;
import com.xianzhi.baidumap.base.BaseApplication;
import com.xianzhi.baidumap.view.CameraPreview;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoService extends Service {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutInflater mLayoutInflater;
    private View mFloatView;
    private LinearLayout cameraPreview;

    private static final String TAG = VideoService.class.getSimpleName();
    private static final int FOCUS_AREA_SIZE = 500;

    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private static boolean cameraFront = false;
    private static boolean flash = false;
    private int quality = CamcorderProfile.QUALITY_480P;
    private boolean recording = false;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化WindowManager对象和LayoutInflater对象  
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutInflater = LayoutInflater.from(this);
        initViiew();
        initData();

    }

    private void initViiew() {
        mFloatView = mLayoutInflater.inflate(R.layout.activity_camera, null);
        cameraPreview = mFloatView.findViewById(R.id.camera_preview);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.x = 10;
        mLayoutParams.y = 10;
        mLayoutParams.width = 1;
        mLayoutParams.height = 1;
        mWindowManager.addView(mFloatView, mLayoutParams);
    }

    private void initData() {
        initialize();
        if (!hasCamera(getApplicationContext())) {
            //这台设备没有发现摄像头
            Toast.makeText(getApplicationContext(), R.string.dont_have_camera_error, Toast.LENGTH_SHORT).show();
            releaseCamera();
            releaseMediaRecorder();
            stopSelf();
        }
        if (mCamera == null) {
            releaseCamera();
            final boolean frontal = cameraFront;

            int cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                //前置摄像头不存在
                switchCameraListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), R.string.dont_have_front_camera, Toast.LENGTH_SHORT).show();
                        stopSelf();
                    }
                };
                if (flash) {
                    mPreview.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                }
            } else if (!frontal) {
                if (flash) {
                    mPreview.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                }
            }
            mCamera = Camera.open(cameraId);
            mPreview.refreshCamera(mCamera);
            reloadQualities(cameraId);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //准备开始录制视频
                if (!prepareMediaRecorder()) {
                    releaseCamera();
                    releaseMediaRecorder();
                    stopSelf();
                }
                //开始录制视频
                try {
                    mediaRecorder.start();
                } catch (final Exception ex) {
                    Log.i("---", "Exception in thread");
                    releaseCamera();
                    releaseMediaRecorder();
                    stopSelf();
                }
                recording = true;
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (recording) {
                    //如果正在录制点击这个按钮表示录制完成
                    mediaRecorder.stop(); //停止
                    releaseMediaRecorder();
                    recording = false;
                    releaseCamera();
                    releaseMediaRecorder();
                    stopSelf();
                }
            }
        }).start();
        return START_REDELIVER_INTENT;
    }

    /**
     * 找前置摄像头,没有则返回-1
     *
     * @return cameraId
     */
    private int findFrontFacingCamera() {
        int cameraId = -1;
        //获取摄像头个数
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    /**
     * 点击对焦
     */
    public void initialize() {
        mPreview = new CameraPreview(getApplicationContext(), mCamera);
        cameraPreview.addView(mPreview);
        cameraPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        focusOnTouch(event);
                    } catch (Exception e) {
                        Log.i(TAG, getString(R.string.fail_when_camera_try_autofocus, e.toString()));
                        //do nothing
                    }
                }
                return true;
            }
        });

    }

    /**
     * reload成像质量
     *
     * @param idCamera
     */
    private void reloadQualities(int idCamera) {
        SharedPreferences prefs = getSharedPreferences("RECORDING", Context.MODE_PRIVATE);
        quality = prefs.getInt("QUALITY", CamcorderProfile.QUALITY_480P);
        changeVideoQuality(quality);
        int maxQualitySupported = CamcorderProfile.QUALITY_480P;
        if (CamcorderProfile.hasProfile(idCamera, CamcorderProfile.QUALITY_480P)) {
            maxQualitySupported = CamcorderProfile.QUALITY_480P;
            changeVideoQuality(CamcorderProfile.QUALITY_480P);
        } else if (CamcorderProfile.hasProfile(idCamera, CamcorderProfile.QUALITY_720P)) {
            maxQualitySupported = CamcorderProfile.QUALITY_720P;
            changeVideoQuality(CamcorderProfile.QUALITY_720P);
        } else if (CamcorderProfile.hasProfile(idCamera, CamcorderProfile.QUALITY_1080P)) {
            maxQualitySupported = CamcorderProfile.QUALITY_1080P;
            changeVideoQuality(CamcorderProfile.QUALITY_1080P);
        } else if (CamcorderProfile.hasProfile(idCamera, CamcorderProfile.QUALITY_2160P)) {
            maxQualitySupported = CamcorderProfile.QUALITY_2160P;
            changeVideoQuality(CamcorderProfile.QUALITY_2160P);
        }

        if (!CamcorderProfile.hasProfile(idCamera, quality)) {
            quality = maxQualitySupported;
        }
    }

    /**
     * 切换前置后置摄像头
     */
    View.OnClickListener switchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!recording) {
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    releaseCamera();
                    chooseCamera();
                } else {
                    //只有一个摄像头不允许切换
                    Toast.makeText(getApplicationContext(), R.string.only_have_one_camera, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    /**
     * 选择摄像头
     */
    public void chooseCamera() {
        if (cameraFront) {
            //当前为后置摄像头
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview
                mCamera = Camera.open(cameraId);
                if (flash) {
                    flash = false;
                    mPreview.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                }
                // mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
                reloadQualities(cameraId);
            }
        }
    }

    /**
     * 检查设备是否有摄像头
     *
     * @param context
     * @return
     */
    private boolean hasCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            mCamera.lock();
        }
    }

    private boolean prepareMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (cameraFront) {
                mediaRecorder.setOrientationHint(270);
            } else {
                mediaRecorder.setOrientationHint(90);
            }
        }

        mediaRecorder.setProfile(CamcorderProfile.get(quality));
        File file = new File(BaseApplication.path + "/");
        if (!file.exists()) {
            file.mkdirs();
        }
        Date d = new Date();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(d.getTime()));
        BaseApplication.videoPath = BaseApplication.path + "/" + timestamp + ".mp4";
        File file1 = new File(BaseApplication.videoPath);
        if (file1.exists()) {
            file1.delete();
        }
        mediaRecorder.setOutputFile(BaseApplication.videoPath);
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    //修改录像质量
    private void changeVideoQuality(int quality) {
        SharedPreferences prefs = getSharedPreferences("RECORDING", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("QUALITY", quality);
        editor.commit();
        this.quality = quality;
    }

    private void focusOnTouch(MotionEvent event) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.getMaxNumMeteringAreas() > 0) {
                Rect rect = calculateFocusArea(event.getX(), event.getY());
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);
                mCamera.setParameters(parameters);
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            } else {
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            }
        }
    }

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / mPreview.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / mPreview.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
            if (touchCoordinateInCameraReper > 0) {
                result = 1000 - focusAreaSize / 2;
            } else {
                result = -1000 + focusAreaSize / 2;
            }
        } else {
            result = touchCoordinateInCameraReper - focusAreaSize / 2;
        }
        return result;
    }

    private Camera.AutoFocusCallback mAutoFocusTakePictureCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // do something...
                Log.i("tap_to_focus", "success!");
            } else {
                // do something...
                Log.i("tap_to_focus", "fail!");
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

