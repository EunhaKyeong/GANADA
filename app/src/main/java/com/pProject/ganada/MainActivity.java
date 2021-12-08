package com.pProject.ganada;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private TextView takePictureTv, vocaTv, settingTv;
    private View takePictureView, settingView, vocabularyView;
    private Dialog recognizeSelectDialog;
    private String language;

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

        //설정 클릭 리스너 -> SettingActivity 화면으로 이동
        settingView = (View) findViewById(R.id.setting_view);
        settingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettingActivity();
            }
        });

        //단어장 클릭 리스너 -> VocaBookActivity 화면으로 이동
        vocabularyView = (View) findViewById(R.id.vocabulary_view);
        vocabularyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVocaBookActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //sharedPreferences 에서 선택된 언어 가져오기
        language = getSharedPreferences("Language", MODE_PRIVATE).getString("language", null);
        setLanguageUI(language);    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수 호출
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수
    private void setLanguageUI(String language) {

        takePictureTv = (TextView) findViewById(R.id.take_picture_tv_other_language);
        vocaTv = (TextView) findViewById(R.id.voca_other_language_tv);
        settingTv = (TextView) findViewById(R.id.setting_other_language_tv);

        switch (language) {
            case "english":
                takePictureTv.setText(R.string.take_picture_en);
                vocaTv.setText(R.string.vocabulary_en);
                settingTv.setText(R.string.setting_en);
                break;
            case "china":
                takePictureTv.setText(R.string.take_picture_cn);
                vocaTv.setText(R.string.vocabulary_cn);
                settingTv.setText(R.string.setting_cn);
                break;
            case "vietnam":
                takePictureTv.setText(R.string.take_picture_vn);
                vocaTv.setText(R.string.vocabulary_vn);
                settingTv.setText(R.string.setting_vn);
                break;
            default:
                takePictureTv.setText(R.string.take_picture_jp);
                vocaTv.setText(R.string.vocabulary_jp);
                settingTv.setText(R.string.setting_jp);
                break;
        }
    }

    //이미지/텍스트 촬영 다이얼로그 띄우는 함수
    private void startDialog() {
        recognizeSelectDialog = new Dialog(this);  // Dialog 초기화
        recognizeSelectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        recognizeSelectDialog.setContentView(R.layout.recognize_select_dialog);    // xml 레이아웃 파일과 연결
        recognizeSelectDialog.show();

        setDialogLanguageUI(language);  //선택된 언어에 맞춰 다이얼로그 텍스트뷰의 텍스트 언어를 설정하는 함수 호출

        //물체 인식 클릭 리스너 -> 카메라 실행 로직 함수 호출
        View objectRecognitionView = (View) recognizeSelectDialog.findViewById(R.id.object_recognition_view);
        objectRecognitionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBottomSheetDialog("object");   //카메라 촬영인지, 앨범 이동할건지 묻는 다이얼로그를 띄우는 함수 실행
            }
        });

        //텍스트 인식 클릭 리스너 -> 카메라 실행 로직 함수 호출
        View textRecognitionView = (View) recognizeSelectDialog.findViewById(R.id.text_recognition_view);
        textRecognitionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBottomSheetDialog("text");   //카메라 촬영인지, 앨범 이동할건지 묻는 다이얼로그를 띄우는 함수 실행
            }
        });

        //나가기 이미지뷰 클릭 -> 다이얼로그 닫기
        ImageView exitIv = (ImageView) recognizeSelectDialog.findViewById(R.id.exit_iv);
        exitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recognizeSelectDialog.dismiss();
            }
        });
    }

    //선택된 언어에 맞춰 다이얼로그 텍스트뷰의 텍스트 언어를 설정하는 함수
    private void setDialogLanguageUI(String language) {

        TextView alertTv = (TextView) recognizeSelectDialog.findViewById(R.id.alert_other_language_tv);
        TextView objectRecognitionTv = (TextView) recognizeSelectDialog.findViewById(R.id.object_recognition_other_language_tv);
        TextView textRecognitionTv = (TextView) recognizeSelectDialog.findViewById(R.id.text_recognition_other_language_tv);

        switch (language) {
            case "english":
                alertTv.setText(R.string.take_photo_alert_en);
                objectRecognitionTv.setText(R.string.object_recognition_en);
                textRecognitionTv.setText(R.string.text_recognition_en);
                break;
            case "china":
                alertTv.setText(R.string.take_photo_alert_cn);
                objectRecognitionTv.setText(R.string.object_recognition_cn);
                textRecognitionTv.setText(R.string.text_recognition_cn);
                break;
            case "vietnam":
                alertTv.setText(R.string.take_photo_alert_vn);
                objectRecognitionTv.setText(R.string.object_recognition_vn);
                textRecognitionTv.setText(R.string.text_recognition_vn);
                break;
            default:
                alertTv.setText(R.string.take_photo_alert_jp);
                objectRecognitionTv.setText(R.string.object_recognition_jp);
                textRecognitionTv.setText(R.string.text_recognition_jp);
                break;
        }
    }

    //카메라 촬영인지, 앨범 이동할건지 묻는 다이얼로그를 띄우는 함수
    private void startBottomSheetDialog(String objectType) {
        BottomSheetDialogFragment dialogFragment = new BottomSheetDialogFragment(objectType);
        dialogFragment.show(getSupportFragmentManager(), dialogFragment.getTag());

        recognizeSelectDialog.dismiss();
    }

    private void startSettingActivity() {
        startActivity(new Intent(this, SettingActivity.class));
    }

    private void startVocaBookActivity() {
        startActivity(new Intent(this, VocaBookActivity.class));
    }

    void startLearnWord(Uri uri, String recognizedText) throws IOException, ExecutionException, InterruptedException {
        ExampleParse exampleParse = new ExampleParse();
        String exam = (String) exampleParse.execute(recognizedText).get();

        Intent intent = new Intent(this, LearnWordActivity.class);
        intent.putExtra("uri", uri.toString()); //intent에 사진 uri 전달
        intent.putExtra("recognizedText", recognizedText);
        intent.putExtra("exam", exam);
        startActivity(intent);  //인텐트 실행
    }

}