package com.example.admin.traveljourn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button avatar = (Button) findViewById(R.id.regBtn);

        avatar.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                //Intent to the profile activity
                Intent profileIntent = new Intent(MainActivity.this, landingPage.class);
                startActivity(profileIntent);
            }
        });

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            //user already signed in
            Log.d("AUTH", auth.getCurrentUser().getEmail());
        }else{
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setProviders(
                            AuthUI.EMAIL_PROVIDER,
                            AuthUI.FACEBOOK_PROVIDER,
                            AuthUI.GOOGLE_PROVIDER)
                    .build(), RC_SIGN_IN);
        }
        findViewById(R.id.logout).setOnClickListener(this);

    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                //user logged in
                Log.d("AUTH", auth.getCurrentUser().getEmail());
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED){
                //user not authenticated
                Log.d("AUTH", "NOT AUTHENTICATED");
                Toast.makeText(this, "Sign in cancelled!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.logout){
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("AUTH", "USER LOGGED OUT!");
                            finish();
                        }
                    });
        }
    }
}
