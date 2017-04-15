package fiu.com.skillcourt.entities;

/**
 * Created by Chandan on 3/21/2017.
 */

public class Player {
    private String status;
    private int greenhits, redhits;

    public Player(){
    }

    public Player(String status, int greenhits, int redhits){
        this.status = status;
        this.greenhits = greenhits;
        this.redhits = redhits;
    }
/*
    public String getUserID(){
        return userID;
    }
*/
    public String getPlayerStatus(){
        return status;
    }

    public int getGreenhits(){
        return greenhits;
    }

    public void addGreenhits(int val){
        greenhits++;
    }

    public int getRedhits(){
        return redhits;
    }

    public void addRedHits(int val){
        redhits++;
    }
}
