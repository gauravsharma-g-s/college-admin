package com.example.android.collegeadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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



public class galleryUpload extends AppCompatActivity {
    private Button ImageSelectButton;
    private Button CancelButton;
    private Button ImageUploaded;
    private ProgressBar progressBar;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_upload);
        ImageSelectButton = findViewById(R.id.upload_gallery_button);
        ImageUploaded = findViewById(R.id.uploaded_gallery_button);
        progressBar = findViewById(R.id.progressBar);
        Button imageUploadButton = findViewById(R.id.upload_button);
        CancelButton = findViewById(R.id.cancel_gallery_button);
        ImageUploaded.setVisibility(View.INVISIBLE);
        CancelButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        CancelButton.setOnClickListener(v -> {
            ImageSelectButton.setVisibility(View.VISIBLE);
            ImageUploaded.setVisibility(View.INVISIBLE);
            CancelButton.setVisibility(View.INVISIBLE);
        });

         /*
       Managing Image Selection Button Click
         */
        ImageSelectButton.setOnClickListener(v -> Dexter.withContext(ImageSelectButton.getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent ImageChooser = new Intent();
                ImageChooser.setAction(Intent.ACTION_PICK);
                ImageChooser.setType("image/*");
                startActivityForResult(ImageChooser, 2);
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
        Managing Image Upload Button Click
         */
        imageUploadButton.setOnClickListener(v -> {
            if (!isNetworkConnectedApp()) {
                Toast.makeText(getApplicationContext(), "Your Internet Connection is weak!", Toast.LENGTH_SHORT).show();
            } else if (ImageUploaded.getVisibility() == View.INVISIBLE) {
                Toast.makeText(getApplicationContext(), "Add Image ", Toast.LENGTH_SHORT).show();
            } else {

                progressBar.setVisibility(View.VISIBLE);

                if (filePath != null) {
                    String imageName = System.currentTimeMillis() + "." + getFileExtension(filePath);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("gallery");
                    StorageReference uploadedImage = FirebaseStorage.getInstance().getReference().child("gallery/" + imageName);
                    uploadedImage.putFile(filePath)
                            .addOnSuccessListener(taskSnapshot -> uploadedImage.getDownloadUrl().addOnSuccessListener(uri -> {

                                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();

                                String ImageDetails = uri.toString();
                                databaseReference.child("Image"+System.currentTimeMillis()).setValue(ImageDetails);
                                CancelButton.setVisibility(View.INVISIBLE);
                                ImageSelectButton.setVisibility(View.VISIBLE);
                                ImageUploaded.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            })).addOnProgressListener(snapshot -> {
                            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show());
                   }
               }
            });

        }

        /*
        It will execute if image has been selected
         */
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 2 && resultCode == RESULT_OK) {
                if (data != null) {
                    filePath = data.getData();
                }
                ImageSelectButton.setVisibility(View.INVISIBLE);
                CancelButton.setVisibility(View.VISIBLE);
                ImageUploaded.setVisibility(View.VISIBLE);
            }
        }
    /*
    Function to check if network is available or not
     */
        private boolean isNetworkConnectedApp () {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }

        // This function tells the extension of image
        private String getFileExtension (Uri uri){
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(contentResolver.getType(uri));
        }
    }