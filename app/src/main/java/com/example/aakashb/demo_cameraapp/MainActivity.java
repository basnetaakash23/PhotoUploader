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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    private ImageView mImageView;



    public static final int GALLERY_INTENT = 2;
    private Uri uri;

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
       mImageView = (ImageView) findViewById(R.id.imageView1);



       Button choose = (Button) findViewById(R.id.chooseButton);
        choose.setOnClickListener(this);

        Button upload = (Button) findViewById(R.id.uploadButton);
        upload.setOnClickListener(this);

    }



    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select an image"),PICK_IMAGE_REQUEST);
    }

    private void uploadFile(){
        final ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setTitle("Uploading...");
        mProgressDialog.show();
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        StorageReference filepath = storage.child("images").child(uri.getLastPathSegment());
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        })
        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                mProgressDialog.setMessage("Uploaded " + ((int)progress)+"%...");


            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null) {
            Log.d("ResultOK", String.valueOf(RESULT_OK));
            Log.d("CameraDemo", "Pic saved");

            uri = data.getData();



            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }





        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.chooseButton){
            showFileChooser();


        }else if(view.getId() == R.id.uploadButton){
            uploadFile();


        }

    }
}
