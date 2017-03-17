package com.example.admin.traveljourn;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import static android.R.attr.editable;
import static android.R.attr.ssp;
import static android.R.id.message;
import static com.example.admin.traveljourn.R.id.disableHome;
import static com.example.admin.traveljourn.R.id.sendMsgBtn;

public class SOS extends AppCompatActivity {

    private EditText msgEdit1;
    private  Button sendMessage;
    private  EditText nameField;
    private  EditText msg1Field;
    private  EditText place;
    private String myName = "";
    private String message1 = "";
    private String location = "";
    private String sosMessage = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        //ask the user to enter the details of the emergency
        nameField = (EditText)findViewById(R.id.nameEdit);


        msg1Field = (EditText)findViewById(R.id.msgText);


        place = (EditText)findViewById(R.id.placeEdit);


        sendMessage = (Button)findViewById(R.id.sendMsgBtn);





        sendMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                myName = nameField.getText().toString();
                location = place.getText().toString();
                message1 = msg1Field.getText().toString();

                sosMessage = "Name : " + myName;
                sosMessage += "\n I am in an emergency at : " + location;
                sosMessage += "\n " + message1;
                sosMessage += "\n Please be quick and save me.";
                sosMessage += "\n Thank you";

                Intent sosIntent = new Intent(Intent.ACTION_SENDTO);
                sosIntent.setData(Uri.parse("mailto:"));   //to be handled only by email apps
                sosIntent.putExtra(Intent.EXTRA_SUBJECT, " Emergency!!!!!");
                sosIntent.putExtra(Intent.EXTRA_TEXT, sosMessage);

                if(sosIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(sosIntent);
                }
            }
        });
    }


}
