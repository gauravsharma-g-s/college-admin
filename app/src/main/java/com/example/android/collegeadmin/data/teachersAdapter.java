package com.example.android.collegeadmin.data;


import android.annotation.SuppressLint;
import android.app.AlertDialog;


import android.text.TextUtils;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.collegeadmin.R;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class teachersAdapter extends FirebaseRecyclerAdapter<dataHolder, teachersAdapter.teacherViewHolder> {
    private Spinner mSpinner;
    private int mGender = 0;

    public teachersAdapter(@NonNull FirebaseRecyclerOptions<dataHolder> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull teacherViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull dataHolder model) {

        holder.teacherNameView.setText(model.getName());
        holder.teacherQualificationView.setText(model.getQualification());
        if (model.getImageUrl() == null) {
            holder.teacherImageView.setImageResource(R.drawable.sample_teacher_image);
        } else {
            Glide.with(holder.teacherImageView.getContext()).load(model.getImageUrl()).into(holder.teacherImageView);
        }


        holder.editButton.setOnClickListener(v -> {
            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.teacherImageView.getContext())
                    .setContentHolder(new ViewHolder(R.layout.editdialog)).setExpanded(true, 1100).create();
            View view = dialogPlus.getHolderView();
            EditText teacherNameEdit = view.findViewById(R.id.teacherNameUpdated);
            EditText teacherEmailEdit = view.findViewById(R.id.teacherEmailUpdated);
            EditText teacherQualificationEdit = view.findViewById(R.id.teacherQualificationUpdated);
            mSpinner = view.findViewById(R.id.spinnerUpdated);
            mGender = model.getGender();
            SetUpSpinner();
            mSpinner.setSelection(mGender);

            teacherNameEdit.setText(model.getName());
            teacherEmailEdit.setText(model.getEmail());
            teacherQualificationEdit.setText(model.getQualification());

            dialogPlus.show();

            Button saveUpdate = view.findViewById(R.id.save_updated);
            saveUpdate.setOnClickListener(v1 -> {
                Map<String, Object> updatedInfo = new HashMap<>();
                updatedInfo.put("name", teacherNameEdit.getText().toString().trim());
                updatedInfo.put("qualification", teacherQualificationEdit.getText().toString().trim());
                updatedInfo.put("email", teacherEmailEdit.getText().toString().trim());
                updatedInfo.put("gender", mGender);
                FirebaseDatabase.getInstance().getReference().child("teachers").child(Objects.requireNonNull(getRef(position).getKey()))
                        .updateChildren(updatedInfo).addOnSuccessListener(unused -> dialogPlus.dismiss()).addOnFailureListener(e -> dialogPlus.dismiss());
            });
        });
        holder.deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.teacherImageView.getContext());
            builder.setTitle("Delete");
            builder.setMessage("Do you want to delete ?");
            builder.setPositiveButton("Yes", (dialog, which) -> FirebaseStorage.getInstance().getReference().child("teacher_images").child(model.getNameImage()).delete().addOnSuccessListener(unused -> FirebaseDatabase.getInstance().getReference().child("teachers").child(Objects.requireNonNull(getRef(position).getKey())).removeValue()).addOnSuccessListener(unused -> Toast.makeText(builder.getContext(), "Deleted successfulyy", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(v.getContext(), "not Deleted", Toast.LENGTH_SHORT).show()));
            builder.setNegativeButton("No", (dialog, which) -> {
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

    }


    // It will invoke when we have to create a CardView (Inflates a view)
    @NonNull
    @Override
    public teacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teachers_list, parent, false);
        return new teacherViewHolder(view);
    }

    // Used for caching references to Views
    static class teacherViewHolder extends RecyclerView.ViewHolder {

        CircleImageView teacherImageView;
        TextView teacherNameView;
        TextView teacherQualificationView;
        ImageButton editButton;
        ImageButton deleteButton;

        public teacherViewHolder(@NonNull View itemView) {
            super(itemView);

            teacherImageView = itemView.findViewById(R.id.profile_image);
            teacherNameView = itemView.findViewById(R.id.teacher_name);
            teacherQualificationView = itemView.findViewById(R.id.teacher_qualification);
            deleteButton = itemView.findViewById(R.id.delete_button);
            editButton = itemView.findViewById(R.id.edit_button);
        }


    }


    private void SetUpSpinner() {
        // Create an ArrayAdapter using strings array and a default spinner layout
        ArrayAdapter<CharSequence> genderSpinnerAdapter = ArrayAdapter.createFromResource(mSpinner.getContext(), R.array.genderSpinner, android.R.layout.simple_spinner_item);
        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpinner.setAdapter(genderSpinnerAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selected = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selected)) {
                    if (selected.equals("Male")) {
                        mGender = 1;
                    } else if (selected.equals("Female")) {
                        mGender = 2;
                    } else {
                        mGender = 0;
                    }
                }
                mSpinner.setSelection(mGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0;
            }
        });
    }
}