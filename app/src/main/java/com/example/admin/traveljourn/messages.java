package com.example.admin.traveljourn;

/**
 * Created by admin on 2017/02/14.
 */

public class messages {
    private  String text;
    private  String name;
    private  String photoUrl;

    public messages(){

    }

    public messages(String text, String name, String photoUrl){
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
