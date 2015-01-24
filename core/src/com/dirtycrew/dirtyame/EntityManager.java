package com.dirtycrew.dirtyame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sturm on 1/24/15.
 */
public class EntityManager {
    List<Entity> entityList = new ArrayList<Entity>();
    List<Entity> toRemove = new ArrayList<Entity>();
    List<Entity> toAdd = new ArrayList<Entity>();

    public void addEntity(Entity e) {
        if(!entityList.contains(e)) {
            toAdd.add(e);
        }

    }

    public void removeEntity(Entity e) {
        if(entityList.contains(e)) {
            toRemove.add(e);
        }
    }

    public void update(float dt) {
        for(Entity e : toRemove) {
            e.dispose();
        }

        toRemove.clear();

        entityList.addAll(toAdd);
        toAdd.clear();

        for(Entity e : entityList) {
            e.update(dt);
        }

    }
}
