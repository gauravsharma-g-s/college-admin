package com.example.android.collegeadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;


import com.example.android.collegeadmin.data.dataHolder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;

public class TeachersEdit extends AppCompatActivity {
    private Spinner mSpinner;
    private int mGender = 0;
    private EditText teacherIdEditView;
    private EditText teacherNameEditView;
    private EditText teacherEmailEditView;
    private EditText teacherQualificationEditView;
    private Uri teacherImageUri;
    private ImageView teacherImageview;
    private String imageUrl = null;
    private String CurrentTeacher = "Insert";
    private String ImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_edit);
        teacherImageview = findViewById(R.id.teacher_image);
        teacherIdEditView = findViewById(R.id.teacherId);
        teacherEmailEditView = findViewById(R.id.teacherEmail);
        teacherNameEditView = findViewById(R.id.teacherName);
        teacherQualificationEditView = findViewById(R.id.teacherQualification);
        mSpinner = findViewById(R.id.spinner);
        SetUpSpinner();


        /*
         Opening File Explorer to upload an image
         */
        teacherImageview.setOnClickListener(v -> Dexter.withContext(TeachersEdit.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
                Intent editIntent = getIntent();
                editIntent.getStringExtra("image");
            }


            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();   // Ask permission again if user deny first time
            }
        }).check());


    }

    // Showing the image selected on app
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && data != null && resultCode == RESULT_OK) {
            teacherImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(teacherImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                teacherImageview.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (isNetworkConnected()) {
                    if (insertTeacher() && CurrentTeacher.equals("Insert")) {
                        Toast.makeText(TeachersEdit.this, "Teacher added Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Please add all details", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_delete:
                showDeleteDialog();
                return true;
            //    case androidx.appcompat.R.id.home:
            // Go back to teacher activity if nothing changed
            // Save any unsaved changes if any
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean insertTeacher() {


        String teacherId = teacherIdEditView.getText().toString().trim();
        String teacherEmail = teacherEmailEditView.getText().toString().trim();
        String teacherName = teacherNameEditView.getText().toString().trim();
        String teacherQualification = teacherQualificationEditView.getText().toString().trim();


        // Check whether all the details are provided or not
        if (!teacherId.isEmpty() && !teacherEmail.isEmpty() && !teacherName.isEmpty()
                && !teacherQualification.isEmpty()) {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = db.getReference("teachers");
            // Check Whether the image has been selected or not
            if (teacherImageUri != null) {
                ImageName = System.currentTimeMillis() + "." + getFileExtension(teacherImageUri); // Name of Image
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference("teacher_images/" +
                        ImageName );
                // Put the image in Firebase Storage
                storageReference.putFile(teacherImageUri).addOnFailureListener(e -> Toast.makeText(TeachersEdit.this, "Error! Cannot add Teacher", Toast.LENGTH_SHORT).show()).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUrl = uri.toString();
                    dataHolder teacherDetails = new dataHolder(ImageName, teacherName, teacherEmail, teacherQualification, mGender, imageUrl);
                    databaseReference.child(teacherId).setValue(teacherDetails);
                    mGender = 0;
                }));
                teacherImageview.setImageResource(R.drawable.add_image);
            } else {
                dataHolder teacherDetails = new dataHolder(ImageName, teacherName, teacherEmail, teacherQualification, mGender, imageUrl);
                databaseReference.child(teacherId).setValue(teacherDetails);
                mGender = 0;
            }



            /*
        Setting all the EditView To empty after saving
         */

            teacherNameEditView.setText("");
            teacherEmailEditView.setText("");
            teacherQualificationEditView.setText("");
            teacherIdEditView.setText("");

            return true;

        }
        return false;
    }

    // This function tells the extension of image
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // Checking whether internet is available
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    void showDeleteDialog() {

    }


    private void SetUpSpinner() {
        // Create an ArrayAdapter using strings array and a default spinner layout
        ArrayAdapter<CharSequence> genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.genderSpinner, android.R.layout.simple_spinner_item);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // if nothing selected it mean gender is Others
                mGender = 0;
            }
        });

    }


}