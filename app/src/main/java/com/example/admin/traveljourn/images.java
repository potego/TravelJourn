package com.example.admin.traveljourn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class images extends AppCompatActivity {

    private Button mGalleryButton;
    private Button mCameraButton;
    private ImageView mImageView;
    private StorageReference mStorage;

    private ProgressDialog mProgress;
    private StorageReference imageRef;

    private static final int GALLERY_INTENT = 2;
    private static final int CAMERA_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mStorage = FirebaseStorage.getInstance().getReference();
        imageRef = mStorage.child("images");

        mProgress = new ProgressDialog(this);

        mCameraButton = (Button) findViewById(R.id.cameraButton);
        mImageView = (ImageView) findViewById(R.id.imageView);


        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference spaceRef = mStorage.child("images/audio.png");
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setType("Image");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            mProgress.setMessage("Image Uploading...");
            mProgress.show();
            Uri uri = data.getData();

            StorageReference filePath = mStorage.child("Photos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();

                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    Picasso.with(images.this).load(downloadUri).fit().centerCrop()
                            .into(mImageView);

                    Toast.makeText(images.this, "Upload finished!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
