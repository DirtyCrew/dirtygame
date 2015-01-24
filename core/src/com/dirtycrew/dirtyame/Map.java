package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.maps.tiled.*;


/**
 * Created by Jared on 1/23/15.
 */
public class Map {

    //Attributes
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    //Methods
    public Map(String tiledMapPath, World world) {
        tiledMap = new TmxMapLoader().load(tiledMapPath);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        TiledMapTileSet tileset =  tiledMap.getTileSets().getTileSet("Block Tileset");
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        for(int x = 0; x < layer.getWidth();x++){
            for(int y = 0; y < layer.getHeight();y++){
                TiledMapTileLayer.Cell cell = layer.getCell(x,y);
                if(cell == null) continue;
                TiledMapTile mapTile = cell.getTile();
                MapProperties properties = mapTile.getProperties();
                Object collidableCell = properties.get("collidable");

                System.out.printf("x: %d y: %d\n", x, y);

                if(collidableCell != null){

                    BodyDef playerBodyDef = new BodyDef();
                    playerBodyDef.type = BodyDef.BodyType.StaticBody;
                    playerBodyDef.position.set(x * 32, y * 32);
                    Body groundBody = world.createBody(playerBodyDef);
                    PolygonShape groundBox = new PolygonShape();
                    groundBox.setAsBox(16, 16);

                    FixtureDef fixtureDef = new FixtureDef();
                    fixtureDef.shape = groundBox;
                    fixtureDef.density = 0.0f;
                    fixtureDef.friction = 0.0f;
                    fixtureDef.restitution = 1f; // Make it bounce a little bit
                    groundBody.createFixture(fixtureDef);
                }
            }
        }


//        TiledMapTileSet tileset =  tiledMap.getTileSets().getTileSet("Block Tileset");
//        for(TiledMapTile tile:tileset){
//            Object property = tile.getProperties().get("collidable");
//            if(property != null){
//                tile.getProperties().
//
//                BodyDef playerBodyDef = new BodyDef();
//                playerBodyDef.type = BodyDef.BodyType.DynamicBody;
//                playerBodyDef.position.set(0, 0);
//                Body playerBody = game.world.createBody(playerBodyDef);
//                PolygonShape playerBox = new PolygonShape();
//                playerBox.setAsBox(25, 25);
//
//                FixtureDef fixtureDef = new FixtureDef();
//                fixtureDef.shape = playerBox;
//                fixtureDef.density = 0.0f;
//                fixtureDef.friction = 0.0f;
//                fixtureDef.restitution = 1f; // Make it bounce a little bit
//                playerBody.createFixture(fixtureDef);
//
//            }
//        }
    }

    public void drawMap(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }
}