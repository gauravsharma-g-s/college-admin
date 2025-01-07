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

import com.example.android.collegeadmin.data.noticeInfo;


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

import java.text.DateFormat;

import java.util.Date;


public class UploadNotice extends AppCompatActivity {
    private Button NoticeSelectButton;
    private Button CancelButton;
    private Button NoticeUploaded;
    private EditText noticeName;
    private ProgressBar progressBar;
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);
        NoticeSelectButton = findViewById(R.id.upload_notice_button);
        NoticeUploaded = findViewById(R.id.uploaded_notice_button);
        progressBar = findViewById(R.id.progress);
        Button noticeUploadButton = findViewById(R.id.upload_button);
        CancelButton = findViewById(R.id.cancel_notice_button);
        noticeName = findViewById(R.id.fileName);

        NoticeUploaded.setVisibility(View.INVISIBLE);
        CancelButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        CancelButton.setOnClickListener(v -> {
            NoticeSelectButton.setVisibility(View.VISIBLE);
            NoticeUploaded.setVisibility(View.INVISIBLE);
            CancelButton.setVisibility(View.INVISIBLE);
            noticeName.setText("");
        });


        /*
       Managing Notice Selection Button Click
         */

        NoticeSelectButton.setOnClickListener(v -> Dexter.withContext(NoticeSelectButton.getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent pdfsOpen = new Intent(Intent.ACTION_GET_CONTENT);
                pdfsOpen.setType("application/pdf");

                startActivityForResult(Intent.createChooser(pdfsOpen, "Please select Notice "), 2);
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
        Managing Notice Upload Button Click
         */
        noticeUploadButton.setOnClickListener(v -> {
            if (!isNetworkConnected1()) {
                Toast.makeText(getApplicationContext(), "Your Internet Connection is weak!", Toast.LENGTH_SHORT).show();
            } else if (NoticeUploaded.getVisibility() == View.INVISIBLE) {
                Toast.makeText(getApplicationContext(), "Add Notice ", Toast.LENGTH_SHORT).show();
            } else if (noticeName.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Add Notice Name", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notices");
                StorageReference uploadedPdf = FirebaseStorage.getInstance().getReference().child("notices/" + System.currentTimeMillis() + ".pdf");
                uploadedPdf.putFile(filePath)
                        .addOnSuccessListener(taskSnapshot -> uploadedPdf.getDownloadUrl().addOnSuccessListener(uri -> {

                            Toast.makeText(getApplicationContext(), "Notice Uploaded", Toast.LENGTH_SHORT).show();
                            DateFormat simpleDateFormat = DateFormat.getDateInstance();
                            Date date = new Date();
                            String currentDate = simpleDateFormat.format(date);   // Getting the notice date
                            noticeInfo noticeDetails = new noticeInfo(noticeName.getText().toString(), uri.toString(), currentDate);
                            databaseReference.child("Notice" + System.currentTimeMillis()).setValue(noticeDetails);
                            CancelButton.setVisibility(View.INVISIBLE);
                            NoticeSelectButton.setVisibility(View.VISIBLE);
                            NoticeUploaded.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            noticeName.setText("");
                        })).addOnProgressListener(snapshot -> {

                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show());
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
            NoticeSelectButton.setVisibility(View.INVISIBLE);
            CancelButton.setVisibility(View.VISIBLE);
            NoticeUploaded.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkConnected1() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}