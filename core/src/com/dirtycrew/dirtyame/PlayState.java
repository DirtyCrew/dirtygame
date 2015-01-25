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

import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

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
    BetterThanBrandonsTimer timer;

    EventHandler eventHandler;

    BitmapFont deathTimerFont;
    BitmapFont playerControlFont;
    Timer deathTimer;
    long timeForLevel;
    GameManager gameManager;

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
        timer.update(delta);

        cleanUpOrphans();

        deathTimer.update();

        for (Entity e : entityList) {
            e.update(delta);
            if (e instanceof Attack && ((Attack) e).destroy) {
                killEntity(e);
            }
        }
        if (player.isAttacking()) {
            createAttack(world, player);
            player.lastAttackTime = System.currentTimeMillis();
            player.setAttacking(false);
        }

        camera.position.set(player.body.getPosition().x, player.body.getPosition().y, camera.position.z);

        if (camera.position.x < Constants.VIEWPORT_WIDTH / 2) {
            camera.position.set(Constants.VIEWPORT_WIDTH / 2, camera.position.y, camera.position.z);
        }
        if (camera.position.x > map.getWidth() - Constants.VIEWPORT_WIDTH / 2) {
            camera.position.set(map.getWidth() - Constants.VIEWPORT_WIDTH / 2, camera.position.y, camera.position.z);
        }
        if (camera.position.y < Constants.VIEWPORT_HEIGHT / 2){
            camera.position.y = Constants.VIEWPORT_HEIGHT / 2;
        }
        if (camera.position.y > map.getHeight() - Constants.VIEWPORT_HEIGHT / 2) {
            camera.position.set(camera.position.x, map.getHeight() - Constants.VIEWPORT_HEIGHT / 2, camera.position.z);
        }
        camera.update();
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
        InputSet first = player.inputController.inputSets.get(0);
        InputSet second = player.inputController.inputSets.get(1);
        playerControlFont.setColor(first.isJumpActive() ? Color.GREEN : Color.RED);
        playerControlFont.draw(hudBatch, jump,
                (-(Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom),
                playerControlHeight);
        playerControlFont.setColor(first.isLeftActive() ? Color.GREEN : Color.RED);
        playerControlFont.draw(hudBatch, left,
                (-(Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) + playerControlBoundsJump.width + 20,
                playerControlHeight);
        playerControlFont.setColor(first.isRightActive() ? Color.GREEN : Color.RED);
        playerControlFont.draw(hudBatch, right,
                (-(Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) + playerControlBoundsJump.width + playerControlBoundsLeft.width + 80,
                playerControlHeight);
        playerControlFont.setColor(first.isAttackActive() ? Color.GREEN : Color.RED);
        playerControlFont.draw(hudBatch, attack,
                (-(Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) + playerControlBoundsJump.width + playerControlBoundsLeft.width + playerControlBoundsRight.width + 40,
                playerControlHeight);

        playerControlFont.setColor(second.isJumpActive() ? Color.GREEN : Color.RED);
        playerControlFont.draw(hudBatch, jump,
                ((Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) - playerControlBoundsAttack.width - playerControlBoundsJump.width - playerControlBoundsLeft.width - playerControlBoundsRight.width,
                playerControlHeight);
        playerControlFont.setColor(second.isLeftActive() ? Color.GREEN : Color.RED);
        playerControlFont.draw(hudBatch, left,
                ((Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) - playerControlBoundsAttack.width - playerControlBoundsJump.width - playerControlBoundsLeft.width - 40,
                playerControlHeight);
        playerControlFont.setColor(second.isRightActive() ? Color.GREEN : Color.RED);
        playerControlFont.draw(hudBatch, right,
                ((Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) - playerControlBoundsAttack.width - playerControlBoundsJump.width - 80,
                playerControlHeight);
        playerControlFont.setColor(second.isAttackActive() ? Color.GREEN : Color.RED);
        playerControlFont.draw(hudBatch, attack,
                ((Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom) - playerControlBoundsAttack.width,
                playerControlHeight);

        hudBatch.end();
    }

    Attack createAttack(World world, Player player) {
        //Creating Enemy

        BodyDef attackBodyDef = new BodyDef();
        attackBodyDef.fixedRotation = true;
        attackBodyDef.type = BodyDef.BodyType.DynamicBody;
        if (player.facingRight) {
            attackBodyDef.position.set(player.body.getPosition().x + 1f, player.body.getPosition().y);
        }else{
            attackBodyDef.position.set(player.body.getPosition().x - 1f, player.body.getPosition().y);
        }
        Body attackBody = world.createBody(attackBodyDef);
        attackBody.setGravityScale(0);
        PolygonShape attackBox = new PolygonShape();
        attackBox.setAsBox(Conversions.convertToMeters(32) / 10.f, Conversions.convertToMeters(32) / 10.f);

        FixtureDef attackfixtureDef = new FixtureDef();
        attackfixtureDef.shape = attackBox;
        attackfixtureDef.density = 0.0f;
        attackfixtureDef.friction = 0.001f;
        attackfixtureDef.restitution = 0; // Make it bounce a little bit
        attackBody.createFixture(attackfixtureDef);

        Texture attackTexture = new Texture("badlogic.jpg");
        Attack attack = new Attack(attackBody, timer, player.facingRight);
        attack.sprite = new Sprite(attackTexture);
        attack.sprite.setPosition(attackBody.getPosition().x, attackBody.getPosition().y);
        attack.sprite.setSize(.2f, .2f);

        entityList.add(attack);
        renderList.add(attack.sprite);

        attackBody.setUserData(attack);
        return attack;
    }

    private void cleanUpOrphans() {
        for(Entity e : toRemove) {
            if (e instanceof KoopaKoopa) {
                KoopaKoopa k = (KoopaKoopa) e;
                renderList.remove(k.sprite);
                world.destroyBody(k.body);
                entityList.remove(e);
            } else if (e instanceof Player) {
                Player k = (Player) e;
                renderList.remove(k.sprite);
                world.destroyBody(k.body);
                entityList.remove(e);
            } else if(e instanceof Attack) {
                Attack at = (Attack) e;
                renderList.remove(at.sprite);
                world.destroyBody(at.getBody());
                entityList.remove(e);
            } else if(e instanceof Bee) {
                Bee bee = (Bee) e;
                renderList.remove(bee.sprite);
                world.destroyBody(bee.body);
                entityList.remove(e);
            } else {
                continue;
            }
        }
        toRemove.clear();;
    }
    public void killEntity(Entity e) {
        toRemove.add(e);

    }

    @Override
    public void init(final Dirty game) {
        timer = new BetterThanBrandonsTimer();
        world = game.world;
        eventHandler = new EventHandler();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2.f, camera.viewportHeight / 2.f, 0);
        camera.update();
        map = new Map("Lonely_Trees.tmx", game.world);


        player = EntityFactory.createPlayer(game.world, map.playerSpawnLocation);
        entityList.add(player);
        renderList.add(player.sprite);
        player.body.setUserData(player);

        //End Creating Enemy
        for(Vector2 pos : map.monsterSpawnLocations) {
            KoopaKoopa koopaKoopa = EntityFactory.createKoopaKoopa(game.world, pos, eventHandler);
            renderList.add(koopaKoopa.sprite);
            entityList.add(koopaKoopa);
            koopaKoopa.body.setUserData(koopaKoopa);
        }
        //End Creating Enemy
        for(Vector2 pos : map.beeSpawnLocations) {
            Bee bee = EntityFactory.createBee(game.world, pos, eventHandler);
            renderList.add(bee.sprite);
            entityList.add(bee);
            bee.body.setUserData(bee);
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
                    if(e1 instanceof Attack || e2 instanceof Attack){
                        if (e1 instanceof KoopaKoopa){
                            killEntity(e1);
                        }
                        if (e2 instanceof KoopaKoopa){
                            killEntity(e2);
                        }
                    }
                    // other things colliding
                } else {
                    Player p = e1 == player ? (Player)e1 : (Player)e2;
                    Entity e = p == e1 ? e2 : e1;

                    if(e instanceof Map.Tile) {
                        if (((Map.Tile) e).isDeath) {
                            gameManager.changeState(GameManager.GameState.Finish);
                        }

                    } else if(e instanceof KoopaKoopa) {
                        Vector2 up = new Vector2(0, 1);
                        Vector2 down = new Vector2(0, -1);
                        Vector2 right = new Vector2(1,0);
                        Vector2 left = new Vector2(-1,0);

                        Vector2 contactNormal = contact.getWorldManifold().getNormal();
                        if(up.dot(contactNormal) > 0) { // entity landed ontop of player
                            gameManager.changeState(GameManager.GameState.Finish);
                        } else if (down.dot(contactNormal) > 0) { // player on entity
                            killEntity(e);
                        } else if(right.dot(contactNormal) > 0) { // entity hit player on right
                            gameManager.changeState(GameManager.GameState.Finish);
                        } else if(left.dot(contactNormal) > 0) { // entity hit player on left
                            gameManager.changeState(GameManager.GameState.Finish);
                        }

                    } else if(e instanceof Bee) {
                        gameManager.changeState(GameManager.GameState.Finish);
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
        for(Entity e : entityList) {
            killEntity(e);
        }
        cleanUpOrphans();
        entityList.clear();
    }

    private class deathTimer extends EventHandler.Event{

    }

    private class Listener implements EventHandler.EventListener{

        @Override
        public void onEvent(EventHandler.Event e)
        {
            //failed the stage
            System.out.println("Failed");
            gameManager.changeState(GameManager.GameState.Finish);
        }
    };
}