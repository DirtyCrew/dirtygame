package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.Input.Keys;

/**
 * Created by sturm on 1/24/15.
 */
public class PlayState implements IGameState {

    Player player;
    OrthographicCamera camera;
    Map map;

    TiledMapRenderer tiledMapRenderer;
    InputController inputController;

    @Override
    public void update(Dirty game, float delta) {
        player.update(delta);
        camera.position.set(player.body.getPosition(), 0.f);
        camera.update();

    }

    @Override
    public void render(Dirty game) {

        map.drawMap(camera);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.sprite.draw(game.batch);
        game.batch.end();

        game.debugRenderer.render(game.world, camera.combined);
    }


    void createPlayer(World world) {
        Vector2 playerDims = new Vector2(1.f, 1.f);
        Vector2 playerCenter = new Vector2(map.spawnLocation);
        Vector2 playerPos = Conversions.createSpritePosition(playerCenter, playerDims);
        Vector2 playerBodyDims = Conversions.convertToBox2DSize(playerDims);

        // First we create a body definition
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(playerCenter);
        Body playerBody = world.createBody(playerBodyDef);
        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(playerBodyDims.x, playerBodyDims.y);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = playerBox;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.001f;
        fixtureDef.restitution = .001f; // Make it bounce a little bit
        playerBody.createFixture(fixtureDef);
        playerBody.setFixedRotation(true);

        inputController = new InputController();
        inputController.inputSets.add(new InputSet(Keys.RIGHT, Keys.LEFT, Keys.UP, Keys.DOWN));
        inputController.inputSets.add(new InputSet(Keys.D, Keys.A, Keys.W, Keys.S));

        // create player
        Texture playerTexture = new Texture("badlogic.jpg");
        player = new Player(playerBody, inputController);
        player.sprite = new Sprite(playerTexture);
        player.sprite.setPosition(playerPos.x, playerPos.y);
        player.sprite.setSize(playerDims.x, playerDims.y);

    }

    @Override
    public void init(Dirty game) {
        map = new Map("Lonely_Trees.tmx", game.world);

        createPlayer(game.world);

        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2.f, camera.viewportHeight / 2.f, 0);
        camera.update();


    }

    @Override
    public void shutdown() {

    }
}