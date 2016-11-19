package edu.galileo.andriod.appfirebase.models;

/**
 * Created by Usuario on 29/10/2016.
 */

public class Chat {
    private String username;
    private String message;
    private String photo;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }


    @SuppressWarnings("unused")
    public Chat() {
    }

    public Chat(String username, String message, String photo) {
        this.username = username;
        this.message = message;
        this.photo = photo;
    }



}
