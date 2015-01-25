package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.Input.Keys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sturm on 1/24/15.
 */
public class PlayState implements IGameState {

    Player player;
    List<Entity> entityList = new ArrayList<Entity>();
    List<Entity> toRemove = new ArrayList<Entity>();
    List<Sprite> renderList = new ArrayList<Sprite>();
    OrthographicCamera camera;
    OrthographicCamera hudCamera;
    SpriteBatch hudBatch;
    Map map;
    World world;

    TiledMapRenderer tiledMapRenderer;
    InputController inputController;
    EventHandler eventHandler;

    BitmapFont deathTimerFont;
    BitmapFont playerControlFont;
    Timer deathTimer;
    long timeForLevel;
    GameManager gameManager;
    FinishState finishState;

    private final static int hudCameraZoom = 32;
    private final static String left = "Left";
    private final static String right = "Right";
    private final static String jump = "Jump";
    private final static String attack = "Attack";

    public PlayState(GameManager gameManager, long time){
        this.gameManager = gameManager;
        timeForLevel = time;
    }

    @Override
    public void update(Dirty game, float delta) {
        for(Entity entity : toRemove) {
            killEntity(entity);
        }
        toRemove.clear();

        player.update(delta);
        camera.position.set(player.body.getPosition().x, player.body.getPosition().y, camera.position.z);
        //DLog.debug("Pos: {} {} {}", camera.position.x, camera.position.y, map.getWidth());

        if (camera.position.x < Constants.VIEWPORT_WIDTH / 2){
            camera.position.set(Constants.VIEWPORT_WIDTH / 2, camera.position.y, camera.position.z);
        }
        if (camera.position.x > map.getWidth() - Constants.VIEWPORT_WIDTH / 2){
            camera.position.set(map.getWidth() - Constants.VIEWPORT_WIDTH / 2, camera.position.y, camera.position.z);
        }
        camera.update();

        deathTimer.update();

        for(Entity e : entityList) {
            e.update(delta);
        }
    }

    @Override
    public void render(Dirty game) {
        map.drawMap(camera);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        player.sprite.draw(game.batch);

        for(Sprite s : renderList) {
            s.draw(game.batch);
        }

        game.batch.end();

        game.debugRenderer.render(game.world, camera.combined);

        hudCamera.update();
        hudBatch.setProjectionMatrix(hudCamera.projection);
        hudBatch.begin();

        long minutes = deathTimer.timeRemainingInMilliseconds() / (60 * 1000);
        long seconds = (deathTimer.timeRemainingInMilliseconds() / 1000) % 60;
        String time = String.format("%d:%02d", minutes, seconds);
        String timerString = "Death Timer: " + time;
        BitmapFont.TextBounds deathTimerBounds = deathTimerFont.getBounds(timerString);
        deathTimerFont.draw(hudBatch,
                timerString,
                -(Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom, // - deathTimerBounds.width/2
                (Constants.VIEWPORT_HEIGHT / 2.0f) * hudCameraZoom); // + deathTimerBounds.height/2


        BitmapFont.TextBounds playerControlBoundsJump = playerControlFont.getBounds(jump);
        BitmapFont.TextBounds playerControlBoundsLeft = playerControlFont.getBounds(left);
        BitmapFont.TextBounds playerControlBoundsRight = playerControlFont.getBounds(right);
        BitmapFont.TextBounds playerControlBoundsAttack = playerControlFont.getBounds(attack);

        float playerControlHeight = (-(Constants.VIEWPORT_HEIGHT / 2.0f) * hudCameraZoom) + playerControlBoundsJump.height;

        playerControlFont.setColor(Color.GREEN);
        playerControlFont.draw(hudBatch, jump,
                (-(Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom),
                playerControlHeight);
        playerControlFont.setColor(Color.RED);
        playerControlFont.draw(hudBatch, left,
                (-(Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) + playerControlBoundsJump.width + 20,
                playerControlHeight);
        playerControlFont.setColor(Color.GREEN);
        playerControlFont.draw(hudBatch, right,
                (-(Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) + playerControlBoundsJump.width + playerControlBoundsLeft.width + 80,
                playerControlHeight);
        playerControlFont.setColor(Color.RED);
        playerControlFont.draw(hudBatch, attack,
                (-(Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) + playerControlBoundsJump.width + playerControlBoundsLeft.width + playerControlBoundsRight.width + 40,
                playerControlHeight);

        playerControlFont.draw(hudBatch, jump,
                ((Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) - playerControlBoundsAttack.width - playerControlBoundsJump.width - playerControlBoundsLeft.width - playerControlBoundsRight.width,
                playerControlHeight);
        playerControlFont.draw(hudBatch, left,
                ((Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) - playerControlBoundsAttack.width - playerControlBoundsJump.width - playerControlBoundsLeft.width - 40,
                playerControlHeight);
        playerControlFont.draw(hudBatch, right,
                ((Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) - playerControlBoundsAttack.width - playerControlBoundsJump.width - 80,
                playerControlHeight);
        playerControlFont.draw(hudBatch, attack,
                ((Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) - playerControlBoundsAttack.width,
                playerControlHeight);

        hudBatch.end();
    }

    KoopaKoopa createKoopaKoopa(World world, Vector2 pos) {
        //Creating Enemy
        BodyDef koopaBodyDef = new BodyDef();
        koopaBodyDef.fixedRotation = true;
        koopaBodyDef.type = BodyDef.BodyType.DynamicBody;
        koopaBodyDef.position.set(Constants.VIEWPORT_WIDTH / 2.f, Constants.VIEWPORT_HEIGHT / 2.f);
        Body koopaBody = world.createBody(koopaBodyDef);
        PolygonShape koopaBox = new PolygonShape();
        koopaBox.setAsBox(Conversions.convertToMeters(32) / 2.f, Conversions.convertToMeters(32) / 2.f);

        FixtureDef koopafixtureDef = new FixtureDef();
        koopafixtureDef.shape = koopaBox;
        koopafixtureDef.density = 0.5f;
        koopafixtureDef.friction = 0.001f;
        koopafixtureDef.restitution = 0; // Make it bounce a little bit
        koopaBody.createFixture(koopafixtureDef);

        Texture koopaTexture = new Texture("badlogic.jpg");
        KoopaKoopa koopa = new KoopaKoopa(koopaBody,eventHandler);
        koopa.sprite = new Sprite(koopaTexture);
        koopa.sprite.setPosition(pos.x, pos.y);
        koopa.sprite.setSize(1,1);

        entityList.add(koopa);
        renderList.add(koopa.sprite);

        koopaBody.setUserData(koopa);

        return koopa;
    }


    Player createPlayer(World world) {

        Vector2 playerDims = new Vector2(1.f, 1.f);
        Vector2 playerCenter = new Vector2(map.playerSpawnLocation);
        Vector2 playerPos = Conversions.createSpritePosition(playerCenter, playerDims);
        Vector2 playerBodyDims = Conversions.convertToBox2DSize(playerDims);

        // First we create a body definition
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(playerCenter);
        playerBodyDef.linearDamping = 0f;
        Body playerBody = world.createBody(playerBodyDef);
        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(playerBodyDims.x, playerBodyDims.y);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = playerBox;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = .001f; // Make it bounce a little bit
        Fixture fixture = playerBody.createFixture(fixtureDef);
        playerBody.setFixedRotation(true);

        inputController = new InputController();
        inputController.inputSets.add(new InputSet(Keys.RIGHT, Keys.LEFT, Keys.UP, Keys.DOWN));
        if(Controllers.getControllers().size == 0)
        {
            inputController.inputSets.add(new InputSet(Keys.D, Keys.A, Keys.W, Keys.S));
        }
        else
            inputController.inputSets.add(new InputSet(Controllers.getControllers().first(), XBox360Pad.DPAD_RIGHT, XBox360Pad.DPAD_LEFT,
        XBox360Pad.BUTTON_A, XBox360Pad.BUTTON_RB));


        // create player
        Texture playerTexture = new Texture("badlogic.jpg");
        player = new Player(playerBody, inputController, camera);
        player.sprite = new Sprite(playerTexture);
        player.sprite.setPosition(playerPos.x, playerPos.y);
        player.sprite.setSize(playerDims.x, playerDims.y);

        // Player is special..dont add to entity list


        renderList.add(player.sprite);

        playerBody.setUserData(player);

        return player;


    }

    public void killEntity(Entity e) {
        if(e instanceof KoopaKoopa) {
            KoopaKoopa k = (KoopaKoopa)e;
            renderList.remove(k.sprite);
            world.destroyBody(k.body);

            entityList.remove(e);

        }
    }

    @Override
    public void init(final Dirty game) {
        world = game.world;
        eventHandler = new EventHandler();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2.f, camera.viewportHeight / 2.f, 0);
        camera.update();
        map = new Map("Lonely_Trees.tmx", game.world);

        finishState = new FinishState();

        createPlayer(game.world);

        //End Creating Enemy
        for(Vector2 pos : map.monsterSpawnLocations) {
            createKoopaKoopa(game.world, pos);
        }


        hudCamera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        hudCamera.zoom += hudCameraZoom;//16

        deathTimerFont = new BitmapFont(Gdx.files.internal("font.fnt"));
        deathTimerFont.setColor(Color.RED);

        playerControlFont = new BitmapFont(Gdx.files.internal("font.fnt"));
        playerControlFont.setColor(Color.RED);

        hudBatch = new SpriteBatch();

        EventHandler.Event event = new deathTimer();
        event.setState("Death Timer");
        Listener listener = new Listener();
        eventHandler.subscribe(event, listener);
        deathTimer = new Timer(timeForLevel, eventHandler, event);

        game.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if(contact.isTouching() == false) {
                    return;
                }



                Entity e1 = (Entity)contact.getFixtureA().getBody().getUserData();
                Entity e2 = (Entity)contact.getFixtureB().getBody().getUserData();
                if(e1 != player && e2 != player) {
                    // other things colliding
                } else {
                    Player p = e1 == player ? (Player)e1 : (Player)e2;
                    Entity e = p == e1 ? e2 : e1;

                    if(e instanceof Map.Tile) {
                        if (((Map.Tile) e).isDeath) {
                            game.gameManager.transitionToState(game.finishState);
                        }

                    } else if(e instanceof KoopaKoopa) {
                        Vector2 up = new Vector2(0, 1);
                        Vector2 down = new Vector2(0, -1);
                        Vector2 right = new Vector2(1,0);
                        Vector2 left = new Vector2(-1,0);

                        Vector2 contactNormal = contact.getWorldManifold().getNormal();
                        if(up.dot(contactNormal) > 0) {
                            game.gameManager.transitionToState(game.finishState);
                        } else if (down.dot(contactNormal) > 0) {
                            toRemove.add(e);
                        } else if(right.dot(contactNormal) > 0) {
                            game.gameManager.transitionToState(game.finishState);
                        } else if(left.dot(contactNormal) > 0) {
                            game.gameManager.transitionToState(game.finishState);
                        }

                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    @Override
    public void shutdown() {
//        eventHandler.subscribe(event, listener);
    }

    private class deathTimer extends EventHandler.Event{

    }

    private class Listener implements EventHandler.EventListener{

        @Override
        public void onEvent(EventHandler.Event e)
        {
            //failed the stage
            System.out.println("Failed");
            gameManager.transitionToState(finishState);
        }
    };
}