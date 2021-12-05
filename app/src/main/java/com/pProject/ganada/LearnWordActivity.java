package com.pProject.ganada;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LearnWordActivity extends AppCompatActivity {

    private TextView word, example_sentence, practice_foreign;
    private ImageButton btn_back, btn_word_pronunciation, btn_sentence_pronunciation;
    private CheckBox btn_bookmark;
    private ImageView word_pic;
    private String language;
    private View practice_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_word);

        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getStringExtra("uri"));

        word_pic = (ImageView) findViewById(R.id.word_pic);
        word_pic.setImageURI(uri);

        btn_bookmark = (CheckBox) findViewById(R.id.btn_bookmark);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_word_pronunciation = (ImageButton) findViewById(R.id.btn_word_pronunciation);
        btn_sentence_pronunciation = (ImageButton) findViewById(R.id.btn_sentence_pronunciation);
        practice_view = (View) findViewById(R.id.practice_view);

        //sharedPreferences 에서 선택된 언어 가져오기
        language = getSharedPreferences("Language", MODE_PRIVATE).getString("language", null);
        setLanguageUI(language);    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수 호출

        //뒤로가기 버튼
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //단어 저장 및 삭제시 토스트메세지 출력
        btn_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_bookmark.isChecked()) {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.save_word_en), Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if( v != null) v.setGravity(Gravity.CENTER);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.delete_word_en), Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if( v != null) v.setGravity(Gravity.CENTER);
                    toast.show();
                }
            }
        });

        //따라쓰기 페이지로 이동
        practice_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PracticeWordActivity.class));
            }
        });


    }

    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수
    private void setLanguageUI(String language) {
        practice_foreign = (TextView) findViewById(R.id.practice_foreign);

        switch (language) {
            case "english":
                practice_foreign.setText(R.string.practice_en);
                break;
            case "china":
                practice_foreign.setText(R.string.practice_cn);
                break;
            case "vietnam":
                practice_foreign.setText(R.string.practice_vn);
                break;
            default:
                practice_foreign.setText(R.string.practice_jp);
                break;
        }
    }
}