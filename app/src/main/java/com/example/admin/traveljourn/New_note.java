package com.example.admin.traveljourn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.message;
import static com.example.admin.traveljourn.Chats.ANONYMOUS;
import static com.example.admin.traveljourn.R.drawable.audio;

public class New_note extends AppCompatActivity {

    private static final  String TAG = "Notes";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final String MESSAGE_LENGTH_KEY = "message_length";
    public static final int RC_SIGN_IN = 1;

    private ListView mNotesListView;
    private MessageAdapter mNotesAdapter;
    private ProgressBar mProgressBar;
    public Button mSaveNote;
    public EditText mNoteEdit;

    //for the app to be able to access the database(messages portion here)
    private FirebaseDatabase mFirebaseDatabase;

    //class that references a specific part of the database
    private DatabaseReference mNotesDatabaseReference;
    private ChildEventListener mChildEventListener;

    //for authorising the users
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    private  String mUsername;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mNotesPhotosReference;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

//        ImageView audio = (ImageView)findViewById(R.id.audio_id);
        ImageView image = (ImageView)findViewById(R.id.imageView);
        mSaveNote = (Button)findViewById(R.id.saveNote);
        mNoteEdit = (EditText) findViewById(R.id.line_1);

        mUsername = ANONYMOUS;

        //instantiating the two objects
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mNotesDatabaseReference = mFirebaseDatabase.getReference().child("Saved_Notes");
        mNotesPhotosReference = mFirebaseStorage.getReference().child("Adventure_photos");

        //Initialize references to views
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mNotesListView = (ListView) findViewById(R.id.message_list);


        image.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                //Intent to the profile activity
                Intent audioIntent = new Intent(New_note.this, images.class);
                startActivity(audioIntent);
            }
        });


        mNoteEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() > 0){
                    mSaveNote.setEnabled(true);
                }else{
                    mSaveNote.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //add code for editing allowing user to edit journal
            }
        });

        mNoteEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        //Send button sends a message and clears the editText
        mSaveNote.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Notes_saved notes = new Notes_saved(mNoteEdit.getText().toString(), null);
                mNotesDatabaseReference.push().setValue(notes);

                //clear input box
                mNoteEdit.setText("");
            }
        });
    }

    private void applyRetrievedLength() {
        Long message_length = mFirebaseRemoteConfig.getLong(MESSAGE_LENGTH_KEY);
        mNoteEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(message_length.intValue())});
        Log.d(TAG, MESSAGE_LENGTH_KEY + " = " + message_length);
    }

}