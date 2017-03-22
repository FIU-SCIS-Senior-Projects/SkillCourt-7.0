package fiu.com.skillcourt.entities;

/**
 * Created by Chandan on 3/21/2017.
 */

public class Player {
    private String status;

    public Player(){
    }

    public Player(String status){
        this.status = status;
    }
/*
    public String getUserID(){
        return userID;
    }
*/
    public String getPlayerStatus(){
        return status;
    }
/*
    public int getGreenhits(){
        return greenhits;
    }

    public void addGreenhits(int val){
        greenhits++;
    }

    public int getRedhits(){
        return redhits;
    }
*/
}
