package fiu.com.skillcourt.entities;

import java.util.List;

/**
 * Created by Chandan on 3/20/2017.
 */

public class Room {
    private String host;
    private String name;
    private String status;
    private String subtext;
    private List<Player> players;

    //THIS CONSTRUCTOR IS MANDATORY
    private Room() {
    }

    //THIS CONSTRUCTOR IS USED TO POPULATE THE LOBBY LIST!
    public Room(String host, String name, List<Player> players, String status, String subtext){
        this.host = host;
        this.name = name;
        this.status = status;
        this.subtext = subtext;
    }

    //THIS CONSTRUCTOR IS USED TO CREATE A ROOM PROGRAMATICALLY
    public Room(int i){

    }

    public String getName(){
        return name;
    }

    public void setName(String s){ name = s; }

    public String getHost(){
        return host;
    }

    public void setHost(String s) { host = s; }

    public String getStatus() { return status; }

    public void setStatus(String s) { status = s; }

    public String getSubtext() { return subtext; }

    public void setSubtext(String s) { subtext = s; }
/*
    public List<Player> getPlayers(){
        return players;
    }

    public void setPlayers(List<Player> p){
        players = p;
    }
*/
}
