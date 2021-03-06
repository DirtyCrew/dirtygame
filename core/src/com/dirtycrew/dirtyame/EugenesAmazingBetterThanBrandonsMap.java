package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jared on 1/23/15.
 */
public class EugenesAmazingBetterThanBrandonsMap implements IMap {

    //Attributes
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    Vector2 tileMeterDims;
    Vector2 mapDims;

    private List<Vector2> monsterSpawnLocations = new ArrayList<Vector2>();
    private List<MovingPlatformData> movingPlatformSpawns = new ArrayList<MovingPlatformData>();
    private List<Vector2> beeSpawnLocations = new ArrayList<Vector2>();
    private Vector2 playerSpawnLocation = new Vector2(0, 0);


    //Methods
    public EugenesAmazingBetterThanBrandonsMap(String s, World world) {
        tiledMap = new TmxMapLoader().load(s);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Constants.FROM_PIXELS_TO_METER);

        TiledMapTileSet tileset =  tiledMap.getTileSets().getTileSet("Block Tileset");
        MapProperties prop = tiledMap.getProperties();
        int w = prop.get("width", Integer.class);
        int h =prop.get("height", Integer.class);
        mapDims = new Vector2(w, h);

        tileMeterDims = new Vector2(Conversions.convertToMeters(prop.get("tilewidth", Integer.class)),
                Conversions.convertToMeters(prop.get("tileheight", Integer.class)));



        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
        for(int x = 0; x < layer.getWidth();x++){
            for(int y = 0; y < layer.getHeight();y++){
                TiledMapTileLayer.Cell cell = layer.getCell(x,y);
                if(cell == null) continue;
                TiledMapTile mapTile = cell.getTile();
                int tileId = mapTile.getId();
                MapProperties properties = mapTile.getProperties();
                Object collidableCell = properties.get("collidable");
                String sc = properties.get("spawn", String.class);
                Object deathTile = properties.get("death");
                boolean isBouncy = properties.get("bouncy") != null ? true : false;
                boolean isWin = properties.get("win") != null ? true : false;
                String movingString = properties.get("moving", String.class);
                Integer moving = movingString != null ? Integer.valueOf(movingString) : null ;
                String movementDurationString = properties.get("movementDuration", String.class);
                Integer movementDuration = movementDurationString != null ? Integer.valueOf(movementDurationString) : 2000 ;
                boolean oppositeStart = properties.get("oppositeStart") != null ? true : false ;

                Integer spawnCell = sc != null ? Integer.valueOf(sc) : null ;

                Vector2 tilePos = new Vector2(x * tileMeterDims.x, y * tileMeterDims.y);
                if(spawnCell != null) {
                    if(spawnCell == 1) {
                        this.playerSpawnLocation = tilePos;
                    } else if(spawnCell == 2) {
                        this.monsterSpawnLocations.add(tilePos);
                    } else if (spawnCell == 3) {
                        this.beeSpawnLocations.add(tilePos);
                    }
                }
                if (moving != null){
                    MovingPlatformData data = new MovingPlatformData(tilePos, movementDuration, moving==1, oppositeStart);
                    this.getMovingPlatformSpawns().add(data);
                }
                if(collidableCell != null){
                    Map.Tile tile = new Map.Tile();
                    if(deathTile != null) {
                        tile.isDeath = true;
                    }
                    if(isWin) {
                        tile.isWin = true;
                    }
                    if(isBouncy) {
                        tile.isBouncy = true;
                    }
                    tile.id = tileId;
                    Vector2 tileBodyDims = Conversions.convertToBox2DSize(tileMeterDims);

                    BodyDef playerBodyDef = new BodyDef();
                    playerBodyDef.type = BodyDef.BodyType.StaticBody;

                    // cant control tilemap sprite positions, so need to center body def so so thats its bottom left corner is the prigin
                    playerBodyDef.position.set(tilePos.add(new Vector2(tileMeterDims.x / 2.f, tileMeterDims.y / 2.f)));
                    Body groundBody = world.createBody(playerBodyDef);
                    //PolygonShape groundBox = new PolygonShape();
                    Vector2 ll = new Vector2(0 - tileBodyDims.x,0 - tileBodyDims.y);
                    Vector2 lr = new Vector2(tileBodyDims.x,0 - tileBodyDims.y);
                    Vector2 ur = new Vector2(tileBodyDims.x, tileBodyDims.y);
                    Vector2 ul = new Vector2(0 - tileBodyDims.x, tileBodyDims.y);
                   // groundBox.setAsBox(tileBodyDims.x, tileBodyDims.y);
                    //Bottom
                    EdgeShape es = new EdgeShape();
                    es.set(ll,lr);

                    FixtureDef fixtureDef = new FixtureDef();
                    fixtureDef.shape = es;
                    fixtureDef.density = 0.0f;
                    fixtureDef.friction = 0.0f;
                    if (isBouncy){
                        fixtureDef.restitution = 1.5f;
                    }else {
                        fixtureDef.restitution = 0f;
                    }
                    groundBody.createFixture(fixtureDef);
                    groundBody.setUserData(tile);

                    //Top
                    es.set(ur,ul);

                    fixtureDef.shape = es;
                    groundBody.createFixture(fixtureDef);
                    groundBody.setUserData(tile);

                    //Left
                    es.set(ul,ll);

                    fixtureDef.shape = es;
                    groundBody.createFixture(fixtureDef);
                    groundBody.setUserData(tile);

                    //Right
                    es.set(lr,ur);

                    fixtureDef.shape = es;
                    groundBody.createFixture(fixtureDef);
                    groundBody.setUserData(tile);

                    es.dispose();

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


    public float getWidth(){
        return mapDims.x * tileMeterDims.x;
    }

    public float getHeight(){
        return mapDims.y * tileMeterDims.y;
    }

    public void drawMap(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public List<Vector2> getMonsterSpawnLocations(){
        return monsterSpawnLocations;
    }

    public List<Vector2> getBeeSpawnLocations(){
        return beeSpawnLocations;
    }

    public List<MovingPlatformData> getMovingPlatformSpawns() { return movingPlatformSpawns; }

    public Vector2 getPlayerSpawnLocation(){
        return playerSpawnLocation;
    }
}