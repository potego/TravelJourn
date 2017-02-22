package com.example.admin.traveljourn;

import android.app.ProgressDialog;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import static android.os.Looper.prepare;

public class Audio extends AppCompatActivity {

    private TextView mRecordView;
    private Button mRecordBtn;
    private MediaRecorder mRecoder;
    private String mFileName = null;

    private static final String LOG_TAG = "Record_log";

    private StorageReference mStorage;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);

        mRecordView = (TextView)findViewById(R.id.recordTextView);
        mRecordBtn = (Button)findViewById(R.id.recordBtn);

        mFileName = Environment.getExternalStorageState();
        mFileName += "/Recorded_audio.3gp";


        mRecordBtn.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    startRecording();
                    mRecordView.setText("Recording Started...");

                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    stopRecording();
                    mRecordView.setText("Recording Stopped!");
                }
                return false;
            }
        });


    }

    private void startRecording(){
        mRecoder = new MediaRecorder();
        mRecoder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecoder.setOutputFile(mFileName);
        mRecoder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecoder.prepare();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "IllegalStateException called", Toast.LENGTH_LONG).show();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "prepare() failed", Toast.LENGTH_LONG).show();

        }

        mRecoder.start();
    }

    private void stopRecording(){
        mRecoder.stop();
        mRecoder.release();
        mRecoder = null;

        uploadAudio();
    }

    private void uploadAudio() {

        mProgress.setMessage("Uploading Audio...");
        mProgress.show();
        StorageReference filePath = mStorage.child("Audio").child("new_audio.3gp");

        Uri uri = Uri.fromFile(new File(mFileName));

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgress.dismiss();
                mProgress.setMessage("Audio File saved!");
            }
        });


    }
}
