package com.example.android.collegeadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        // Locating the buttons in Java file
        Button teachers_button = findViewById(R.id.teachers);
        Button notice_button = findViewById(R.id.notice);
        Button gallery_button = findViewById(R.id.gallery);
        Button paper_button = findViewById(R.id.paper);
        // Clicking Teacher Button
        teachers_button.setOnClickListener(v -> {
            Intent teacher_display_intent = new Intent(MainActivity.this, TeacherDisplay.class);
            if (teacher_display_intent.resolveActivity(getPackageManager()) != null) {
                startActivity(teacher_display_intent);
            }
        });
        // Clicking Notice Button
        notice_button.setOnClickListener(v -> {
            Intent notice_upload_intent = new Intent(MainActivity.this, UploadNotice.class);
            if (notice_upload_intent.resolveActivity(getPackageManager()) != null) {
                startActivity(notice_upload_intent);
            }
        });
        // Clicking Gallery Button
        gallery_button.setOnClickListener(v -> {
            Intent uploadGallery = new Intent(MainActivity.this, galleryUpload.class);
            startActivity(uploadGallery);
        });
        // Clicking papers Button
        paper_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadPaper = new Intent(MainActivity.this, papersUpload.class);
                startActivity(uploadPaper);
            }
        });
    }


}