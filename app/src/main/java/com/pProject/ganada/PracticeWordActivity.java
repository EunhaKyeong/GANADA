package com.pProject.ganada;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class PracticeWordActivity extends AppCompatActivity {

    private TextView word_tv, practice_foreign;
    private ConstraintLayout draw_linear;
    private ImageButton btn_clear;
    private String language;

    class Point {
        float x;
        float y;
        boolean check;
        int color;

        public Point(float x, float y, boolean check, int color) {
            this.x = x;
            this.y = y;
            this.check = check;
            this.color = color;
        }
    }

    class MyView extends View {
        public MyView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Paint p = new Paint();
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(25);
            for (int i = 1; i < points.size(); i++) {
                p.setColor(points.get(i).color);
                if (!points.get(i).check) {
                    continue;
                }
                canvas.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y, p);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    points.add(new Point(x, y, false, color));
                case MotionEvent.ACTION_MOVE:
                    points.add(new Point(x, y, true, color));
                case MotionEvent.ACTION_UP:
                    break;
            }
            invalidate();
            return true;
        }
    }

    ArrayList<Point> points = new ArrayList<Point>();
    ArrayList<Path> path = new ArrayList<Path>();

    int color = Color.BLACK;

//    class MyView extends View {
//        private Paint paint = new Paint();
//
//
//        //여러가지의 그리기 명령을 모았다가 한번에 출력해주는
//        //버퍼역할을 담당한다..
//        private Path path = new Path();
//
//        private int x,y;
//
//        public MyView(Context context){
//
//            super(context);
//        }
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//
//            paint.setColor(Color.BLACK);
//
//            //STROKE속성을 이용하여 테두리...선...
//            paint.setStyle(Paint.Style.STROKE);
//
//            //두께
//            paint.setStrokeWidth(10);
//
//
//            //path객체가 가지고 있는 경로를 화면에 그린다...
//            canvas.drawPath(path,paint);
//
//        }
//
//        @Override
//        public boolean onTouchEvent(MotionEvent event) {
//            x = (int)event.getX();
//            y = (int)event.getY();
//
//            switch(event.getAction()){
//                case MotionEvent.ACTION_DOWN:
//                    path.moveTo(x,y);
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    x = (int)event.getX();
//                    y = (int)event.getY();
//
//                    path.lineTo(x,y);
//                    break;
//            }
//
//            //View의 onDraw()를 호출하는 메소드...
//            invalidate();
//
//            return true;
//        }
//
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_word);

        Intent intent = getIntent();
        String word = intent.getStringExtra("word");

        //툴바 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        //상태바 설정
        View view = getWindow().getDecorView();
        if (view != null) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.parseColor("#FFF2CC"));
        }

        final MyView m = new MyView(this);

        word_tv = (TextView) findViewById(R.id.word);
        btn_clear = (ImageButton) findViewById(R.id.btn_clear);
        draw_linear = (ConstraintLayout) findViewById(R.id.draw_linear);

        word_tv.setText(word);

        //sharedPreferences 에서 선택된 언어 가져오기
        language = getSharedPreferences("Language", MODE_PRIVATE).getString("language", null);
        setLanguageUI(language);    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수 호출

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                points.clear();
                m.invalidate();
            }
        });
        draw_linear.addView(m);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}