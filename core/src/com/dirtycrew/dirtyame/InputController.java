package com.dirtycrew.dirtyame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by z084254 on 1/24/15.
 */
public class InputController {
    List<InputSet> inputSets;

    public InputController(){
        inputSets = new ArrayList<InputSet>();
    }

    public boolean isRightPressed(){
        for (InputSet set:inputSets){
            if(set.isRightActive() && !set.isRightPressed()) {
                return false;
            }

            if(!set.isRightActive() && set.isRightPressed()) {
                return false;
            }
        }
        DLog.debug("Right pressed");
        return true;
    }

    public boolean isLeftPressed(){
        for (InputSet set:inputSets){
            if(set.isLeftActive() && !set.isLeftPressed()) {
                return false;
            }

            if(!set.isLeftActive() && set.isLeftPressed()) {
                return false;
            }
        }
        return true;
    }

    public boolean isJumpPressed(){
        for (InputSet set:inputSets){
            if(set.isJumpActive() && !set.isJumpPressed()) {
                return false;
            }

            if(!set.isJumpActive() && set.isJumpPressed()) {
                return false;
            }
        }
        return true;
    }

    public boolean isAttackPressed(){
        for (InputSet set:inputSets){
            if(set.isAttackActive() && !set.isAttackPressed()) {
                return false;
            }

            if(!set.isAttackActive() && set.isAttackPressed()) {
                return false;
            }
        }
        return true;
    }

    public boolean isVolumePressed(){
        for (InputSet set:inputSets){
            if(!set.isVolumePressed()) {
                return false;
            }

        }
        return true;
    }

    public void randomizeControls(){
        for (InputSet set:inputSets){
            set.setAllInactive();
        }

        Random randomizer = new Random();
        inputSets.get(randomizer.nextInt(inputSets.size())).setRightActive(true);
        inputSets.get(randomizer.nextInt(inputSets.size())).setLeftActive(true);
        inputSets.get(randomizer.nextInt(inputSets.size())).setJumpActive(true);
        inputSets.get(randomizer.nextInt(inputSets.size())).setAttackActive(true);
    }
}
