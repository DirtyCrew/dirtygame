package com.dirtycrew.dirtyame;

/**
 * Created by sturm on 1/24/15.
 */
interface IGameState {
    void init(Dirty game);
    void shutdown();
    void update(Dirty game, float delta);
    void render(Dirty game);

}