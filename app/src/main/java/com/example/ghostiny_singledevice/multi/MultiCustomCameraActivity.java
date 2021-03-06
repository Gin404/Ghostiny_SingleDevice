package com.example.ghostiny_singledevice.multi;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ghostiny_singledevice.ActivityChangeService;
import com.example.ghostiny_singledevice.MainActivity;
import com.example.ghostiny_singledevice.R;
import com.example.ghostiny_singledevice.single.CustomCameraActivity;
import com.example.ghostiny_singledevice.single.CustomShowActivity;
import com.example.ghostiny_singledevice.utils.Colour;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MultiCustomCameraActivity extends AppCompatActivity {

    private static final String TAG = "AndroidCameraApi";
    private ImageButton btnTake;
    private TextureView textureView;
    private Uri imageUri;

    private static final SparseIntArray ORIENTATION = new SparseIntArray();
    static {
        ORIENTATION.append(Surface.ROTATION_0, 90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 270);
        ORIENTATION.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder reqBuilder;
    private Size imageDimension;
    private ImageReader reader;

    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private Timer timer;
    private TimerTask task;

    private ActivityChangeService myService;
    private ActivityChangeService.CommandBinder commandBinder;
    private ArrayList<Integer> rmCol = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    private Intent startIntent;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("multiCamera", "service 绑定");
            commandBinder = (ActivityChangeService.CommandBinder)service;
            myService = commandBinder.getService();

            //成员离开，随机消失一个非倒霉颜色，累计，传递到下一个活动
            /*myService.setMemberLeaveCallBack2(new ActivityChangeService.MemberLeaveCallBack2() {
                @Override
                public void memberLeave2(int rmColor, String nickName) {
                    Set<String> names = sharedPreferences.getStringSet("nameSet", null);
                    if (names == null){
                        throw new NullPointerException("names 为空");
                    }
                    names.remove(nickName);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("nameSet", names);
                    editor.apply();

                    rmCol.add(rmColor);
                }
            });
*/
            /*myService.setNewOwnerCallBack(new ActivityChangeService.NewOwnerCallBack() {
                @Override
                public void asNewOwner() {
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putBoolean("isowner", true);
                    editor.apply();
                }
            });*/
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("multiCamera", "service 解除绑定");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_custom_camera);

        startIntent = new Intent(MultiCustomCameraActivity.this, ActivityChangeService.class);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        textureView = (TextureView)findViewById(R.id.texture_view);
        btnTake = (ImageButton)findViewById(R.id.take_btn);
        if (textureView == null){
            throw new NullPointerException("textureView 为空");
        }

        textureView.setSurfaceTextureListener(textureViewListener);

        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                timer.cancel ();
                Intent intent = new Intent(MultiCustomCameraActivity.this, MultiCustomShowActivity.class);
                Bundle bundle = getIntent().getExtras();
                bundle.putString("photoPath", imageUri.toString());
                bundle.putSerializable("rmColor", rmCol);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        task = new TimerTask() {
            @Override
            public void run() {
                takePicture();
                Intent intent = new Intent(MultiCustomCameraActivity.this, MultiCustomShowActivity.class);
                Bundle bundle = getIntent().getExtras();
                if (bundle == null){
                    throw new NullPointerException("bundle 为空");
                }
                bundle.putString("photoPath", imageUri.toString());
                bundle.putSerializable("rmColor", rmCol);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };

        timer = new Timer();
    }

    CameraDevice.StateCallback stateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            //cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            cameraDevice = null;
        }
    };

    TextureView.SurfaceTextureListener textureViewListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(MultiCustomCameraActivity.this, "Saved "+ file, Toast.LENGTH_SHORT).show();
            createCameraPreview();
        }
    };

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void openCamera() {
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try{
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap configurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (configurationMap == null){
                throw new NullPointerException("configurationMap 为空");
            }
            imageDimension = configurationMap.getOutputSizes(SurfaceTexture.class)[0];

            //check realtime permission if run higher API 23
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallBack, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }

    private void createCameraPreview() {
        try{
            SurfaceTexture texture = textureView.getSurfaceTexture();
            if (texture == null){
                throw new NullPointerException("texture 为空");
            }
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            reqBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);//
            reqBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (cameraDevice == null){
                        return;
                    }
                    cameraCaptureSession = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(MultiCustomCameraActivity.this, "Change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void updatePreview(){
        if (cameraDevice == null){
            Log.e(TAG, "updatePreview error, return");
            Toast.makeText(MultiCustomCameraActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
        reqBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        try{
            cameraCaptureSession.setRepeatingRequest(reqBuilder.build(), null, mBackgroundHandler);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }


    private synchronized void takePicture(){
        if (cameraDevice == null){
            Log.e(TAG, "cameraDevice is null");
            return;
        }

        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSize = null;
            if (characteristics != null){
                jpegSize = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).
                        getOutputSizes(ImageFormat.JPEG);
            }

            //capture image with custom size

            int width = 640;
            int height = 480;

            if (jpegSize != null && jpegSize.length > 0){
                width = jpegSize[0].getWidth();
                height = jpegSize[0].getHeight();
            }

            reader = ImageReader.newInstance(width,height,ImageFormat.JPEG, 1);
            List<Surface> outputSurface = new ArrayList<>(2);
            outputSurface.add(reader.getSurface());
            outputSurface.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            //check orientation base on device
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATION.get(rotation));

            file = new File(getExternalCacheDir(),"custom_image.jpg");
            try {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }

            if (Build.VERSION.SDK_INT < 24) {
                imageUri = Uri.fromFile(file);
            } else {
                imageUri = FileProvider.getUriForFile(MultiCustomCameraActivity.this, "com.example.ghostiny_singledevice.fileprovider", file);
            }
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try{
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }finally {
                        {
                            if (image != null){
                                image.close();
                            }
                        }
                    }
                }
                private void save(byte[] bytes) throws IOException{
                    OutputStream outputStream = null;
                    try{
                        outputStream = new FileOutputStream(file);
                        outputStream.write(bytes);
                    }finally {
                        if (outputStream != null){
                            outputStream.close();
                        }
                    }
                }
            };

            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

            cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    }catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            },mBackgroundHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "You can't use camera without permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("multiCamera", "onStart");
        startIntent = new Intent(MultiCustomCameraActivity.this, ActivityChangeService.class);
        bindService(startIntent, serviceConnection, BIND_AUTO_CREATE);
        timer.schedule(task, 10000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (textureView.isAvailable()){
            openCamera();
        }else {
            textureView.setSurfaceTextureListener(textureViewListener);
        }
    }

    @Override
    protected void onPause() {
        if (cameraDevice != null){
            cameraDevice.close();
        }
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            try {
                cameraCaptureSession.abortCaptures();
            } catch (Exception ignore) {
            } finally {
                cameraCaptureSession = null;
            }
        }
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    protected void onStop() {
        timer.cancel();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
        Log.d("multiCustomCamera", "onDestroy");
    }

    @Override
    public void onBackPressed() {
        timer.cancel ();
        stopService(startIntent);
        clearSharedPre();
        super.onBackPressed();
        startActivity(new Intent(MultiCustomCameraActivity.this, MainActivity.class));
    }

    public void clearSharedPre(){
        Set<String> names = sharedPreferences.getStringSet("nameSet", null);
        if (names == null){
            throw new NullPointerException("names 为空");
        }
        names.clear();
        sharedPreferences.edit().clear().putStringSet("nameSet", names).apply();
    }
}
