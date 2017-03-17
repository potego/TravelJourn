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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class images extends AppCompatActivity {

    private Button mGalleryButton;
    private Button mCameraButton;
    private ImageView mImageView;
    private StorageReference mStorage;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef;
    LinearLayout ll;

    private ProgressDialog mProgress;

    private static final int GALLERY_INTENT = 2;
    private static final int CAMERA_REQUEST_CODE = 2;

    private static final int RC_PHOTO_PICKER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        ll = (LinearLayout) findViewById(R.id.activity_images);
        mStorage = FirebaseStorage.getInstance().getReference();
        mRef = mDatabase.getReference().child("Images");
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ImageView tv = new ImageView(getApplicationContext());
                tv.setElevation(4);
                tv.setPadding(8, 0,4,16);

                String image = dataSnapshot.child("image").getValue().toString();
                    Picasso.with(images.this).load(image).fit().centerCrop().into(tv);
                    ll.addView(tv);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProgress = new ProgressDialog(this);

        mCameraButton = (Button) findViewById(R.id.cameraButton);
        mImageView = (ImageView) findViewById(R.id.imageView);

//        mCameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                startActivityForResult(intent, CAMERA_REQUEST_CODE);
//            }
//        });


        // ImagePickerButton shows an image picker to upload a image for a message
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            mProgress.setMessage("Image Uploading...");
            mProgress.show();
            final Uri uri = data.getData();

            StorageReference filePath = mStorage.child("Photos").child(uri.getLastPathSegment());



            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();


                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    mRef.child("image").push().setValue(downloadUri.toString());

                    Picasso.with(images.this).load(downloadUri).fit().centerCrop()
                            .into(mImageView);

                    Toast.makeText(images.this, "Upload finished!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.setType("Image");
        startActivity(intent);
    }

}