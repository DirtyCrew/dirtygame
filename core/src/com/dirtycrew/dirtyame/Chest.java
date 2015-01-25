package com.dirtycrew.dirtyame;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Kern on 1/25/2015.
 */
public class Chest extends Entity {

    Body chestBody;

    public Chest(Body body)
    {
        chestBody = body;
    }

    @Override
    public void update(float delta) {

    }
}
