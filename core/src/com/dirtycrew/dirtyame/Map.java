package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.sun.xml.internal.ws.api.client.SelectOptimalEncodingFeature;

/**
 * Created by Jared on 1/23/15.
 */
public class Map {

    //Attributes
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    //Methods
    public Map(String tiledMapPath) {
        tiledMap = new TmxMapLoader().load(tiledMapPath);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void drawMap(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }
}