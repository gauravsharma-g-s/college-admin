package com.example.android.collegeadmin;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.content.Intent;


import com.example.android.collegeadmin.data.dataHolder;
import com.example.android.collegeadmin.data.teachersAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherDisplay extends AppCompatActivity {
    private teachersAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_display);


        FloatingActionButton teacherAdd = findViewById(R.id.add_teachers);

        teacherAdd.setOnClickListener(v -> {
            Intent editActivityIntent =new Intent (TeacherDisplay.this,TeachersEdit.class);
            startActivity(editActivityIntent);
        });
        // View Recycling Showing the Teachers information
        RecyclerView teachersRecyclerView = findViewById(R.id.teachers_list);
        teachersRecyclerView.setLayoutManager(new WrapLinearLayoutManager(this));  // Setting the LayoutManager
        FirebaseRecyclerOptions<dataHolder> options = new FirebaseRecyclerOptions.Builder<dataHolder>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("teachers"),dataHolder.class).build();

        adapter = new teachersAdapter(options);
        teachersRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}