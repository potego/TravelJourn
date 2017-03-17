package com.example.admin.traveljourn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.R.attr.data;
import static com.example.admin.traveljourn.R.color.com_facebook_blue;

/**
 * Created by admin on 2017/03/09.
 */

public class notes_messages extends AppCompatActivity {
    //class that references a specific part of the database
    private DatabaseReference mNotesDatabaseReference;
    private ChildEventListener mChildEventListener;

    private static final  String TAG = "Notes";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final String MESSAGE_LENGTH_KEY = "message_length";
    public static final int RC_SIGN_IN = 1;

    public Button new_note;
    public TextView displayNote;

    public EditText mNoteEdit;

    //for the app to be able to access the database(messages portion here)
    private FirebaseDatabase mFirebaseDatabase;

    private  String mUsername;
    private Button mImages;

    private ProgressDialog mProgress;
    private static final int CAMERA_REQUEST_CODE = 2;

    private MessageAdapter mNotesAdapter;
    private FirebaseStorage mFirebaseStorage;
    private ImageView mImageView;
    private StorageReference mNotesPhotosReference;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    String key;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_messages);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mNotesDatabaseReference = mFirebaseDatabase.getReference().child("Saved_Notes");

//        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        key = mNotesDatabaseReference.getKey();

        mProgress = new ProgressDialog(this);

        mNotesPhotosReference = mFirebaseStorage.getReference().child("Adventure_photos");

        new_note = (Button)findViewById(R.id.newNote);
        mImages = (Button)findViewById(R.id.pictures);
        displayNote = (TextView)findViewById(R.id.messageTextView);

        ll = (LinearLayout) findViewById(R.id.notes_layout);

        new_note.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                //Intent to the profile activity
                Intent profileIntent = new Intent(notes_messages.this, New_note.class);
                startActivity(profileIntent);
            }
        });

        mImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to the profile activity
                Intent profileIntent = new Intent(notes_messages.this, images.class);
                startActivity(profileIntent);
            }
        });

        mNotesDatabaseReference.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                TextView tv = new TextView(getApplicationContext());
                tv.setElevation(4);
                tv.setPadding(8, 0,4,16);

                tv.setText("***************************************************\n " +dataSnapshot.child("text").getValue());
                ll.addView(tv);
                System.out.println("****************************** "+dataSnapshot.child("text").getValue());

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

    }

    private void applyRetrievedLength() {
        Long message_length = mFirebaseRemoteConfig.getLong(MESSAGE_LENGTH_KEY);
        mNoteEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(message_length.intValue())});
        Log.d(TAG, MESSAGE_LENGTH_KEY + " = " + message_length);
    }

    private void onSignedInInitialize(String username){
        mUsername = username;
        attachDatabaseReadListener();
    }
    private void onSignedOutCleanUp(){
        mUsername = ANONYMOUS;
        mNotesAdapter.clear();
        detachDatabaseReadListener();
    }

    protected void attachDatabaseReadListener() {

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    messages msg = dataSnapshot.getValue(messages.class);
                    mNotesAdapter.add(msg);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            mNotesDatabaseReference.addChildEventListener(mChildEventListener);
        }

    }

    protected void detachDatabaseReadListener() {

        if (mChildEventListener != null) {
            mNotesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

            mProgress.show();
            Uri uri = data.getData();

            StorageReference filePath = mNotesPhotosReference.child("Photos").child(uri.getLastPathSegment());


            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();

                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    Picasso.with(notes_messages.this).load(downloadUri).fit().centerCrop()
                            .into(mImageView);

                    Toast.makeText(notes_messages.this, "finished!", Toast.LENGTH_SHORT).show();
                }
            });
    }


}
