package com.pProject.ganada;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {

    private PreviewView previewView;
    private ImageButton captureBtn;
    private ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = (PreviewView) findViewById(R.id.viewFinder);

        startCamera();  //카메라 실행

        captureBtn = (ImageButton) findViewById(R.id.camera_capture_btn);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();    //사진 찍기 함수 호출
            }
        });
    }

    //카메라 실행 함수
    public void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        imageCapture = new ImageCapture.Builder().build();

        cameraProviderListenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    //사진 찍기 함수
    private void takePhoto() {

        Log.d("CameraActivity", "takePhoto");

        //찍힌 사진을 저장할 파일 생성
        File photoFile = new File(
                getOutputDirectory(),
                new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA).format(System.currentTimeMillis()) + ".jpg"
        );

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {    //사진 찍기 성공
                        //file -> Uri 변경
                        Uri savedUri = Uri.fromFile(photoFile);
                        startLearnWord(savedUri); //CameraAfterActivity 실행
                        Log.d("CameraActivity", savedUri.toString());
                    }

                    @Override
                    public void onError(ImageCaptureException error) {  //사진 찍기 오류
                        Log.e("CameraActivity", error.toString());
                    }
                }
        );
    }

    //사진 저장할 디렉토리 생성 or 가져오기
    private File getOutputDirectory() {
        File mediaDir = getExternalMediaDirs()[0];

        if (mediaDir != null && mediaDir.exists()) {
            return mediaDir;
        } else {
            return getFilesDir();
        }
    }

    //CameraAfterActivity 이동 함수
    private void startLearnWord(Uri uri) {
        Intent intent = new Intent(this, LearnWordActivity.class);
        intent.putExtra("uri", uri.toString()); //intent에 사진 uri 전달
        startActivity(intent);  //인텐트 실행

        finish();   //현재 액티비티 종료
    }

}