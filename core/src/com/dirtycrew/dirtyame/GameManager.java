package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Game;

/**
 * Created by sturm on 1/24/15.
 */
public class GameManager {

    public enum GameState {
        Tutorial,
        Config,
        Start,
        Play,
        Finish,
        Fail
    }
    //Attributes
    IGameState currentStateObj;
    GameState currentState;
    GameState nextState;
    Dirty game;
    int mapNumber;
    long startTime;
    long numDeaths;

    //Methods
    public GameManager(Dirty game, GameState startState){
        this.currentState = startState;
        this.nextState = startState;
        this.currentStateObj = getState(startState);
        currentStateObj.init(game);
        this.game = game;
        startTime = System.currentTimeMillis();
        numDeaths = 0;
    }

    public IGameState getState(){
        return this.currentStateObj;
    }

    public void changeState(GameState state, int mapNumber) {
        this.mapNumber = mapNumber;
        if(currentState == state) {
            return;
        }
        nextState = state;
    }

    public void changeState(GameState state) {
        this.mapNumber = -1;
        if(currentState == state) {
            return;
        }
        nextState = state;
    }
    private IGameState getState(GameState state) {

        switch(nextState) {
            case Tutorial:
                return new TutorialState();
            case Config:
                return new ConfigState();
            case Play:
                return new PlayState(this, 180000, mapNumber, game.numPlayers);
            case Start:
                return new StartState(this, game);
            case Finish:
                return new FinishState();
            case Fail:
                return new FailState();
        }
        return null;
    }
    public void update() {
        if(nextState != currentState) {
            currentStateObj.shutdown();
            currentStateObj = getState(nextState);
            currentStateObj.init(game);
            currentState = nextState;
        }
    }
}
