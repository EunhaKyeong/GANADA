package com.pProject.ganada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectLanguageActivity extends AppCompatActivity {

    private Button settingUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        //설정하기 버튼 클릭 리스너 -> MainActivity로 이동
        settingUpBtn = (Button) findViewById(R.id.setting_up_btn);
        settingUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMainActivity();
            }
        });

    }

    public void goMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}