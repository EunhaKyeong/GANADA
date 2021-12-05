package com.pProject.ganada;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView info_foreign, app_explanation_foreign;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        btn_back = (Button) findViewById(R.id.btn_back);

        //sharedPreferences 에서 선택된 언어 가져오기
        language = getSharedPreferences("Language", MODE_PRIVATE).getString("language", null);
        setLanguageUI(language);    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수 호출

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                finish();
            }
        });
    }

    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수
    private void setLanguageUI(String language) {

        info_foreign = (TextView) findViewById(R.id.info_foreign);
        app_explanation_foreign = (TextView) findViewById(R.id.app_explanation_foreign);

        switch (language) {
            case "english":
                info_foreign.setText(R.string.information_en);
                app_explanation_foreign.setText(R.string.information_detail_en);
                break;
            case "china":
                info_foreign.setText(R.string.information_cn);
                app_explanation_foreign.setText(R.string.information_detail_cn);
                break;
            case "vietnam":
                info_foreign.setText(R.string.information_vn);
                app_explanation_foreign.setText(R.string.information_detail_vn);
                break;
            default:
                info_foreign.setText(R.string.information_jp);
                app_explanation_foreign.setText(R.string.information_detail_jp);
                break;
        }
    }
}