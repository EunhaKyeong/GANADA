package com.pProject.ganada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class CameraAfterActivity extends AppCompatActivity {

    private ImageView testIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_after);

        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getStringExtra("uri"));

        testIv = (ImageView) findViewById(R.id.test_iv);
        testIv.setImageURI(uri);
    }
}