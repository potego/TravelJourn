package com.example.admin.traveljourn;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Notes_saved{

        private  String text;
        private  String name;
        private  String photoUrl;
    public  Notes_saved(){}

        public Notes_saved(String text, String name){
            this.text = text;
            this.name = name;
            this.photoUrl = photoUrl;
        }

        public String getText(){
            return text;
        }

        public  void setText(String text){
            this.text = text;
        }

        public String getName(){
            return name;
        }

        public  void setName(String name){
            this.name = name;
        }

        public String getPhotoUrl(){
            return photoUrl;
        }

        public  void setPhotoUrl(String photoUrl){
            this.photoUrl= photoUrl;
        }

    }
