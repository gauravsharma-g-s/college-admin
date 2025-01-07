package com.example.android.collegeadmin;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.android.collegeadmin.data.paperDetails;
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

public class papersUpload extends AppCompatActivity {
    private Button PaperSelectButton;
    private Button CancelButton;
    private Button PaperUploaded;
    private EditText PaperName,PaperYear;
    private EditText SemesterName;
    private ProgressBar progressBar;
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papers_upload);
        PaperSelectButton = findViewById(R.id.upload_paper_button);
        PaperUploaded = findViewById(R.id.uploaded_paper_button);
        progressBar = findViewById(R.id.progress);
        Button PaperUploadButton = findViewById(R.id.upload_button);
        CancelButton = findViewById(R.id.cancel_paper_button);
        PaperName = findViewById(R.id.fileName);
        SemesterName = findViewById(R.id.semName);
        PaperYear = findViewById(R.id.paperYear);
        PaperUploaded.setVisibility(View.INVISIBLE);
        CancelButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        CancelButton.setOnClickListener(v -> {
            PaperSelectButton.setVisibility(View.VISIBLE);
            PaperUploaded.setVisibility(View.INVISIBLE);
            CancelButton.setVisibility(View.INVISIBLE);
            SemesterName.setText("");
            PaperName.setText("");
            PaperYear.setText("");
        });

        PaperSelectButton.setOnClickListener(v -> Dexter.withContext(PaperSelectButton.getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent pdfsOpen = new Intent(Intent.ACTION_GET_CONTENT);
                pdfsOpen.setType("application/pdf");

                startActivityForResult(pdfsOpen, 2);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check());

          /*
        Managing Paper Upload Button Click
         */
        PaperUploadButton.setOnClickListener(v -> {
            int sem = Integer.parseInt(SemesterName.getText().toString());
            if (!isNetworkConnected1()) {
                Toast.makeText(getApplicationContext(), "Please Check your Internet Connection!", Toast.LENGTH_SHORT).show();
            } else if (PaperUploaded.getVisibility() == View.INVISIBLE) {
                Toast.makeText(getApplicationContext(), "Please add papers pdf ", Toast.LENGTH_SHORT).show();
            } else if (PaperName.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please add paper Name", Toast.LENGTH_SHORT).show();
            }
            else if (SemesterName.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Semester field is empty", Toast.LENGTH_SHORT).show();
            }else if(sem<=0|| sem>=9){
                Toast.makeText(getApplicationContext(), "Please add a valid semester", Toast.LENGTH_SHORT).show();
            }else if(PaperYear.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Year field is empty",Toast.LENGTH_SHORT).show();
            }
             else if(Integer.parseInt(PaperYear.getText().toString())<=1990 ||Integer.parseInt(PaperYear.getText().toString())>=2400){
                // Can Only enter paper between year 1990 to 2400
                Toast.makeText(getApplicationContext(),"Please enter a valid year",Toast.LENGTH_SHORT).show();
            }
            else
            {
                progressBar.setVisibility(View.VISIBLE);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("papers");
                StorageReference uploadedPdf = FirebaseStorage.getInstance().getReference().child("papers/" + System.currentTimeMillis() + ".pdf");
              //  switch statement to upload the papers according to their semesters
                switch (sem) {
                    case 1: {
                        uploadedPdf.putFile(filePath)
                                .addOnSuccessListener(taskSnapshot -> uploadedPdf.getDownloadUrl().addOnSuccessListener(uri -> {

                                    Toast.makeText(getApplicationContext(), "Paper Uploaded", Toast.LENGTH_SHORT).show();

                                    paperDetails paperDetails = new paperDetails(PaperName.getText().toString(), Integer.parseInt(PaperYear.getText().toString()),  uri.toString());
                                    databaseReference.child("semester1/" + "Paper" + System.currentTimeMillis()).setValue(paperDetails);
                                    Invisible();

                                })).addOnProgressListener(snapshot -> {

                                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show());
                    }
                        break;
                    case 2: {
                        uploadedPdf.putFile(filePath)
                                .addOnSuccessListener(taskSnapshot -> uploadedPdf.getDownloadUrl().addOnSuccessListener(uri -> {

                                    Toast.makeText(getApplicationContext(), "Paper Uploaded", Toast.LENGTH_SHORT).show();

                                    paperDetails paperDetails = new paperDetails(PaperName.getText().toString(), Integer.parseInt(PaperYear.getText().toString()),  uri.toString());
                                    databaseReference.child("semester2/" + "Paper" + System.currentTimeMillis()).setValue(paperDetails);
                                    Invisible();

                                })).addOnProgressListener(snapshot -> {

                                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show());
                    }
                    break;
                    case 3: {
                        uploadedPdf.putFile(filePath)
                                .addOnSuccessListener(taskSnapshot -> uploadedPdf.getDownloadUrl().addOnSuccessListener(uri -> {

                                    Toast.makeText(getApplicationContext(), "Paper Uploaded", Toast.LENGTH_SHORT).show();

                                    paperDetails paperDetails = new paperDetails(PaperName.getText().toString(), Integer.parseInt(PaperYear.getText().toString()),  uri.toString());
                                    databaseReference.child("semester3/" + "Paper" + System.currentTimeMillis()).setValue(paperDetails);
                                    Invisible();

                                })).addOnProgressListener(snapshot -> {

                                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show());
                    }
                    break;
                    case 4: {
                        uploadedPdf.putFile(filePath)
                                .addOnSuccessListener(taskSnapshot -> uploadedPdf.getDownloadUrl().addOnSuccessListener(uri -> {

                                    Toast.makeText(getApplicationContext(), "Paper Uploaded", Toast.LENGTH_SHORT).show();

                                    paperDetails paperDetails = new paperDetails(PaperName.getText().toString(), Integer.parseInt(PaperYear.getText().toString()),  uri.toString());
                                    databaseReference.child("semester4/" + "Paper" + System.currentTimeMillis()).setValue(paperDetails);
                                    Invisible();

                                })).addOnProgressListener(snapshot -> {

                                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show());
                    }
                    break;
                    case 5: {
                        uploadedPdf.putFile(filePath)
                                .addOnSuccessListener(taskSnapshot -> uploadedPdf.getDownloadUrl().addOnSuccessListener(uri -> {

                                    Toast.makeText(getApplicationContext(), "Paper Uploaded", Toast.LENGTH_SHORT).show();

                                    paperDetails paperDetails = new paperDetails(PaperName.getText().toString(), Integer.parseInt(PaperYear.getText().toString()),  uri.toString());
                                    databaseReference.child("semester5/" + "Paper" + System.currentTimeMillis()).setValue(paperDetails);
                                    Invisible();

                                })).addOnProgressListener(snapshot -> {

                                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show());
                    }
                    break;
                    case 6: {
                        uploadedPdf.putFile(filePath)
                                .addOnSuccessListener(taskSnapshot -> uploadedPdf.getDownloadUrl().addOnSuccessListener(uri -> {

                                    Toast.makeText(getApplicationContext(), "Paper Uploaded", Toast.LENGTH_SHORT).show();

                                    paperDetails paperDetails = new paperDetails(PaperName.getText().toString(), Integer.parseInt(PaperYear.getText().toString()),  uri.toString());
                                    databaseReference.child("semester6/" + "Paper" + System.currentTimeMillis()).setValue(paperDetails);
                                    Invisible();

                                })).addOnProgressListener(snapshot -> {

                                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show());
                    }
                    break;
                    case 7: {
                        uploadedPdf.putFile(filePath)
                                .addOnSuccessListener(taskSnapshot -> uploadedPdf.getDownloadUrl().addOnSuccessListener(uri -> {

                                    Toast.makeText(getApplicationContext(), "Paper Uploaded", Toast.LENGTH_SHORT).show();
                                    paperDetails paperDetails = new paperDetails(PaperName.getText().toString(), Integer.parseInt(PaperYear.getText().toString()),  uri.toString());
                                    databaseReference.child("semester7/" + "Paper" + System.currentTimeMillis()).setValue(paperDetails);
                                    Invisible();

                                })).addOnProgressListener(snapshot -> {

                                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show());
                    }
                    break;
                    case 8: {
                        uploadedPdf.putFile(filePath)
                                .addOnSuccessListener(taskSnapshot -> uploadedPdf.getDownloadUrl().addOnSuccessListener(uri -> {

                                    Toast.makeText(getApplicationContext(), "Paper Uploaded", Toast.LENGTH_SHORT).show();
                                    paperDetails paperDetails = new paperDetails(PaperName.getText().toString(), Integer.parseInt(PaperYear.getText().toString()),  uri.toString());
                                    databaseReference.child("semester8/" + "Paper" + System.currentTimeMillis()).setValue(paperDetails);
                                   Invisible();

                                })).addOnProgressListener(snapshot -> {

                                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show());
                        break;
                    }

                    default:

                }


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {
                filePath = data.getData();
            }
            PaperSelectButton.setVisibility(View.INVISIBLE);
            CancelButton.setVisibility(View.VISIBLE);
            PaperUploaded.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkConnected1() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void Invisible(){

            CancelButton.setVisibility(View.INVISIBLE);
            PaperSelectButton.setVisibility(View.VISIBLE);
            PaperUploaded.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            SemesterName.setText("");
            PaperYear.setText("");
            PaperName.setText("");

    }
}