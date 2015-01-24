package com.dirtycrew.dirtyame;

public abstract class Entity {
    private static long key = 0;

    private final long id;

    public Entity() {
        id = key++;
    }

    public abstract void update();

    public long getId() {
        return id;
    }
}