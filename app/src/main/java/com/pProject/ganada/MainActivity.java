package com.pProject.ganada;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private View takePictureView;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //이미지/텍스트 촬영 클릭 리스너 -> 물체 인식인지 텍스트 인식인지 클릭하는 다이얼로그 띄우기
        takePictureView = (View) findViewById(R.id.take_picture_view);
        takePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialog();
            }
        });
    }

    //카메라 사용권한 콜백 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (isCameraPermissionGranted()) {    //카메라 권한이 허용됐으면
            startCameraActivity();  //CameraActivity 실행
        } else {
            Toast.makeText(MainActivity.this, "카메라 사용 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();   //다이얼로그 닫기
    }

    private void startDialog() {
        dialog = new Dialog(this);  // Dialog 초기화
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialog.setContentView(R.layout.take_picture_dialog);    // xml 레이아웃 파일과 연결
        dialog.show();

        //사진 찍기 클릭 리스너 -> 카메라 실행 로직 함수 호출
        View objectRecognitionView = (View) dialog.findViewById(R.id.object_recognition_view);
        objectRecognitionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCameraLogic();
            }
        });

        //글자 찍기 클릭 리스너 -> 카메라 실행 로직 함수 호출
        View textRecognitionView = (View) dialog.findViewById(R.id.text_recognition_view);
        textRecognitionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCameraLogic();
            }
        });

        //나가기 이미지뷰 클릭 -> 다이얼로그 닫기
        ImageView exitIv = (ImageView) dialog.findViewById(R.id.exit_iv);
        exitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    //카메라 실행 로직 함수
    private void startCameraLogic() {
        if (isCameraPermissionGranted()) {
            startCameraActivity();
            dialog.dismiss();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.CAMERA"}, 101);
        }
    }

    //CameraActivity로 이동하는 함수
    private void startCameraActivity() {
        startActivity(new Intent(this, CameraActivity.class));
    }

    //카메라 권한이 있는지 체크하는 함수
    private boolean isCameraPermissionGranted() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }
}