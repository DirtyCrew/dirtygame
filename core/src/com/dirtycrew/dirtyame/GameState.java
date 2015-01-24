package com.dirtycrew.dirtyame;

/**
 * Created by Jared on 1/24/15.
 */
public class GameState {

    //Attributes
    Player player;
    Map map;

    //Methods
    public GameState(Player player, Map map){
        this.player = player;
        this.map = map;
    }

    public void setMap(Map map){
        this.map = map;
    }

}
