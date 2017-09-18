package com.example.aakashb.demo_cameraapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    private ImageView mImageView;
    private StorageReference storage;
    private ProgressDialog mProgressDialog;
    public static final int GALLERY_INTENT = 2;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.

       /*
        Button capture = (Button) findViewById(R.id.cameraButton);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                // Here, the counter will be incremented each time, and the
                // picture taken by camera will be stored as 1.jpg,2.jpg
                // and likewise.
                count++;
                String file = count + ".jpg";
                File newfile = new File(dir, file);
                try {
                    newfile.createNewFile();
                } catch (IOException e) {
                }


                Uri outputFileUri = FileProvider.getUriForFile(v.getContext() ,((Activity) v.getContext()).getPackageName(), newfile);





                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                Log.d("Success","Success");

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);


        });
        */

        Button upload = (Button) findViewById(R.id.uploadButton);
        upload.setOnClickListener(new View.OnClickListener(){



            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("ResultOK", String.valueOf(RESULT_OK));
            Log.d("CameraDemo", "Pic saved");
            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();
            Uri uri = data.getData();

            StorageReference filepath = storage.child("images").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });

        }
    }
}
