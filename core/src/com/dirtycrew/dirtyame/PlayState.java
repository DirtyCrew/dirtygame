package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Created by sturm on 1/24/15.
 */
public class PlayState implements IGameState {

    Player player;
    OrthographicCamera camera;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    @Override
    public void update(Dirty game, float delta) {

        camera.update();

    }

    @Override
    public void render(Dirty game) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        player.sprite.draw(game.batch);
        game.batch.end();
    }



    @Override
    public void init() {
        // create player
        Texture playerTexture = new Texture("badlogic.jpg");
        player = new Player();
        player.sprite = new Sprite(playerTexture);
        player.sprite.setPosition(0, 0);
        player.sprite.setSize(50, 50);

        float viewportWidth = 800;
        float viewportHeight = 600;
        camera = new OrthographicCamera(viewportWidth, viewportHeight);
        camera.position.set(camera.viewportWidth / 2.f, camera.viewportHeight / 2.f, 0);
        camera.update();

        tiledMap = new TmxMapLoader().load("example_map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    @Override
    public void shutdown() {

    }
}