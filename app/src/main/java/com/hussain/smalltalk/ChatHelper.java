package com.hussain.smalltalk;

/**
 * Created by Hussain on 06-Aug-17.
 */

class ChatHelper {
    private String Name;
    private String Email;
    private String Image_Url;

    public ChatHelper(){

    }

    public ChatHelper(String name, String email, String image_Url) {
        Name = name;
        Email = email;
        Image_Url = image_Url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage_Url() {
        return Image_Url;
    }

    public void setImage_Url(String image_Url) {
        Image_Url = image_Url;
    }
}

