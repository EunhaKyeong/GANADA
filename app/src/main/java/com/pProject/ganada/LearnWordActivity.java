package com.pProject.ganada;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import androidx.appcompat.widget.LinearLayoutCompat$InspectionCompanion;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LearnWordActivity extends AppCompatActivity {

    private VocaDB vocaDB = null;
    private Context mContext;

    private TextView word_tv, ex_sentence_tv, practice_foreign;
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

        word_tv = (TextView) findViewById(R.id.word);
        ex_sentence_tv = (TextView) findViewById(R.id.ex_sentence);

        word_pic = (ImageView) findViewById(R.id.word_pic);
        word_pic.setImageURI(uri);

        btn_bookmark = (CheckBox) findViewById(R.id.btn_bookmark);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_word_pronunciation = (ImageButton) findViewById(R.id.btn_word_pronunciation);
        btn_sentence_pronunciation = (ImageButton) findViewById(R.id.btn_sentence_pronunciation);
        practice_view = (View) findViewById(R.id.practice_view);

        //DB 생성
        vocaDB = VocaDB.getInstance(this);
        mContext = getApplicationContext();

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

        class InsertRunnable implements Runnable {
            @Override
            public void run() {
                Voca voca = new Voca();
                voca.word = word_tv.getText().toString();
                voca.ex_sentence = ex_sentence_tv.getText().toString();
                voca.picture_uri = uri.toString();
                VocaDB.getInstance(mContext).vocaDao().insert(voca);
            }
        }

        class DeleteRunnable implements Runnable {
            @Override
            public void run() {
                Voca voca = new Voca();
                VocaDB.getInstance(mContext).vocaDao().delete(VocaDB.getInstance(mContext).vocaDao().findByUri(uri.toString()));
            }
        }

        btn_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_bookmark.isChecked()) {
                    //insert DB
                    InsertRunnable insertRunnable = new InsertRunnable();
                    Thread addThread = new Thread(insertRunnable);
                    addThread.start();

                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.save_word_en), Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if(v != null) v.setGravity(Gravity.CENTER);
                    toast.show();

                    btn_bookmark.setEnabled(true);
                } else {
                    //delete DB
                    DeleteRunnable deleteRunnable = new DeleteRunnable();
                    Thread deleteThread = new Thread(deleteRunnable);
                    deleteThread.start();

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
                Intent intent = new Intent(getApplicationContext(), PracticeWordActivity.class);
                intent.putExtra("word", word_tv.getText());
                startActivity(intent);
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