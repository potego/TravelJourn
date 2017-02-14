package com.example.admin.traveljourn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class landingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        ImageView avatar = (ImageView)findViewById(R.id.avatar_id);

        avatar.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                //Intent to the profile activity
                Intent profileIntent = new Intent(landingPage.this, Profile.class);
                startActivity(profileIntent);
            }
        });

        ImageView new_note = (ImageView)findViewById(R.id.new_note_id);

            new_note.setOnClickListener(new View.OnClickListener(){

                public void onClick(View view){
                    //Intent to the profile activity
                    Intent new_noteIntent = new Intent(landingPage.this, New_note.class);
                    startActivity(new_noteIntent);
                }
            });

        ImageView chats = (ImageView)findViewById(R.id.chats_id);

            chats.setOnClickListener(new View.OnClickListener(){

                public void onClick(View view){
                    //Intent to the profile activity
                    Intent chatsIntent = new Intent(landingPage.this, Chats.class);
                    startActivity(chatsIntent);
                }
        });

        Button save_me = (Button)findViewById(R.id.save_id);

            save_me.setOnClickListener(new View.OnClickListener(){

                public void onClick(View view){
                    //Intent to the profile activity
                    Intent sosIntent = new Intent(landingPage.this, MainActivity.class);
                    startActivity(sosIntent);
                }
        });


    }
}
