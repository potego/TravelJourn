package com.example.admin.traveljourn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button avatar = (Button) findViewById(R.id.reg_id);

        avatar.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                //Intent to the profile activity
                Intent profileIntent = new Intent(MainActivity.this, landingPage.class);
                startActivity(profileIntent);
            }
        });
    }
}
