package edu.galileo.andriod.appfirebase.models;

public class Chat {
    private String username;
    private String message;
    private String photo;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public Chat(String username, String message, String photo, String token) {
        this.username = username;
        this.message = message;
        this.photo = photo;
        this.token = token;
    }



}
