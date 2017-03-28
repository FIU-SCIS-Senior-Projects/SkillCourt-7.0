package fiu.com.skillcourt.entities;

import java.util.HashMap;

/**
 * Created by Chandan on 3/24/2017.
 */

public class User {
    private String email;
    private HashMap games;
    private String photoUrl;
    private String role;
    private String room;
    private HashMap teams;

    public User(){}

    public User(String email, String photoUrl, String role, String room){
        this.email = email;
        //this.games = games;
        this.photoUrl = photoUrl;
        this.role = role;
        this.room = room;
        //this.teams = teams;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String s){
        email = s;
    }

    public String getPhotoUrl(){return photoUrl;}

    public void setPhotoUrl(String s){photoUrl = s;}

    public String getRole(){return role;}

    public void setRole(String s){role = s;}

    public String getRoom(){return room;}

    public void setRoom(String s){room = s;}
}
