package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.files.FileHandleStream;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.FileHandler;

/**
 * Created by sturm on 1/24/15.
 */
public class PlayState implements IGameState {
    IMap map;
    Player player;
    List<Entity> entityList = new ArrayList<Entity>();
    List<Entity> toRemove = new ArrayList<Entity>();
    List<Sprite> renderList = new ArrayList<Sprite>();
    OrthographicCamera camera;
    OrthographicCamera hudCamera;
    SpriteBatch hudBatch;
    //Map map;
    World world;
    Box2DDebugRenderer debugRenderer;

    BetterThanBrandonsTimer timer;
    private Music music;
    EventHandler eventHandler;

    BitmapFont deathTimerFont;
    BitmapFont playerControlFont;
    Timer deathTimer;
    long timeForLevel;
    GameManager gameManager;
    int mapNumber;
    InputController inputController;
    private boolean volume;
    private boolean volumePressed;
    int numPlayers;

    private final static int hudCameraZoom = 32;
    private final static String left = "Left";
    private final static String right = "Right";
    private final static String jump = "Jump";
    private final static String attack = "Attack";

    public PlayState(GameManager gameManager, long time, int mapNumber, int numPlayers){
        this.numPlayers = numPlayers;
        this.gameManager = gameManager;
        timeForLevel = time;
        inputController = new InputController();
        inputController.inputSets.add(new InputSet(Input.Keys.SPACE));
        switchMusic();
        this.mapNumber = mapNumber;
    }

    private void switchMusic()
    {
        int song = (new Random()).nextInt(7);
        
        switch (song){
            case 0:{
                music = Gdx.audio.newMusic(Gdx.files.getFileHandle("01 A Night Of Dizzy Spells.mp3", Files.FileType.Internal));
                break;
            }
            case 1:{
                music = Gdx.audio.newMusic(Gdx.files.getFileHandle("02 Underclocked (underunderclocked mix).mp3", Files.FileType.Internal));
                break;
            }
            case 2:{
                music = Gdx.audio.newMusic(Gdx.files.getFileHandle("03 Chibi Ninja.mp3", Files.FileType.Internal));
                break;
            }
            case 3:{
                music = Gdx.audio.newMusic(Gdx.files.getFileHandle("06 Searching.mp3", Files.FileType.Internal));
                break;
            }
            case 4:{
                music = Gdx.audio.newMusic(Gdx.files.getFileHandle("10 Arpanauts.mp3", Files.FileType.Internal));
                break;
            }
            case 5:{
                music = Gdx.audio.newMusic(Gdx.files.getFileHandle("Jumpshot.mp3", Files.FileType.Internal));
                break;
            }
            case 6:{
                music = Gdx.audio.newMusic(Gdx.files.getFileHandle("Prologue.mp3", Files.FileType.Internal));
                break;
            }
            default:{
                music = Gdx.audio.newMusic(Gdx.files.getFileHandle("01 A Night Of Dizzy Spells.mp3", Files.FileType.Internal));
                break;
            }
        }
    }

    @Override
    public void update(Dirty game, float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            game.gameManager.changeState(GameManager.GameState.Start);
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
        }

        timer.update(delta);

        if(inputController.isVolumePressed())
        {
            if(!volumePressed)
            {
                if (volume) {
                    music.pause();
                    volume = false;
                } else {
                    music.play();
                    volume = true;
                }
            }
            volumePressed = true;
        }
        else
        {
            volumePressed = false;
        }

        cleanUpOrphans();

//        deathTimer.update();

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

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
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
        

        hudCamera.update();
        hudBatch.setProjectionMatrix(hudCamera.projection);
        hudBatch.begin();



        String deathCounterString = "Deaths: " + game.gameManager.numDeaths;
        BitmapFont.TextBounds deathCounterBounds = deathTimerFont.getBounds(deathCounterString);
        deathTimerFont.draw(hudBatch,
                deathCounterString,
                (Constants.SCREEN_WIDTH / 2) - deathCounterBounds.width,
                (Constants.SCREEN_HEIGHT / 2) - deathCounterBounds.height);

        BitmapFont.TextBounds playerControlBoundsJump = playerControlFont.getBounds(jump);
        BitmapFont.TextBounds playerControlBoundsLeft = playerControlFont.getBounds(left);
        BitmapFont.TextBounds playerControlBoundsRight = playerControlFont.getBounds(right);
        BitmapFont.TextBounds playerControlBoundsAttack = playerControlFont.getBounds(attack);

        float playerControlHeight = (-(Constants.VIEWPORT_HEIGHT / 2.0f) * hudCameraZoom) + playerControlBoundsJump.height;

//        Texture controlBorder = new Texture("white_square.png");
//        hudBatch.draw(controlBorder,
//                -(Constants.SCREEN_WIDTH / 2.0f) - 10,
//                -(Constants.SCREEN_HEIGHT / 2) - 10,
//                Constants.SCREEN_WIDTH + 20,
//                playerControlBoundsJump.height + 20);
//
//        controlBorder = new Texture("blackbox.jpeg");
//        hudBatch.draw(controlBorder,
//                (-(Constants.VIEWPORT_WIDTH / 2.0f) * hudCameraZoom),
//                -(Constants.SCREEN_HEIGHT / 2),
//                Constants.SCREEN_WIDTH,
//                playerControlBoundsJump.height);

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
                k.musicCleanup();
                renderList.remove(k.sprite);
                world.destroyBody(k.body);
                entityList.remove(e);
                timer.clearAll();
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
            } else if(e instanceof MovingPlatform) {
                MovingPlatform platform = (MovingPlatform) e;
                renderList.remove(platform.sprite);
                world.destroyBody(platform.body);
                entityList.remove(e);
            }else {
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
        world = new World(Constants.GRAVITY, false);
        debugRenderer = new Box2DDebugRenderer();
        timer = new BetterThanBrandonsTimer();

        eventHandler = new EventHandler();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2.f, camera.viewportHeight / 2.f, 0);
        camera.update();

        boolean jumpyKoopas = false;
        //Setting the correct level
        DLog.debug("MpaNumber: {}", mapNumber);
        switch (this.mapNumber){
            case 1:{
                map = new Map("Lonely_Trees.tmx", world);
                break;
            }
            case 2:{
                map = new EugenesAmazingBetterThanBrandonsMap("better-that-lonely-tree.tmx", world);
                break;
            }
            case 3:{
                map = new EugenesAmazingBetterThanBrandonsMap("koopa-funtime.tmx", world);
                jumpyKoopas = true;
                break;
            }
            case 4:{
                map = new EugenesAmazingBetterThanBrandonsMap("Platform_Map.tmx", world);
                break;
            }
        }

        DLog.debug("NumPlayers: {}", numPlayers);
        player = EntityFactory.createPlayer(world, map.getPlayerSpawnLocation(), timer, numPlayers);
        entityList.add(player);
        renderList.add(player.sprite);
        player.body.setUserData(player);

        //End Creating Enemy
        for(Vector2 pos : map.getMonsterSpawnLocations()) {
            KoopaKoopa koopaKoopa = EntityFactory.createKoopaKoopa(world, pos, eventHandler, timer, jumpyKoopas);
            renderList.add(koopaKoopa.sprite);
            entityList.add(koopaKoopa);
            koopaKoopa.body.setUserData(koopaKoopa);
        }
        //End Creating Enemy
        for(Vector2 pos : map.getBeeSpawnLocations()) {
            Bee bee = EntityFactory.createBee(world, pos, timer);
            renderList.add(bee.sprite);
            entityList.add(bee);
            bee.body.setUserData(bee);
        }

        for(MovingPlatformData data : map.getMovingPlatformSpawns()){
            MovingPlatform platform = EntityFactory.createMovingPlatform(world, data, timer);
            renderList.add(platform.sprite);
            entityList.add(platform);
            platform.body.setUserData(platform);
        }

        hudCamera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        hudCamera.zoom += hudCameraZoom;//16

        deathTimerFont = new BitmapFont(Gdx.files.internal("font.fnt"));
        deathTimerFont.setColor(Color.RED);

        playerControlFont = new BitmapFont(Gdx.files.internal("font.fnt"));
        playerControlFont.setColor(Color.RED);

        hudBatch = new SpriteBatch();

//        EventHandler.Event event = new deathTimer();
//        event.setState("Death Timer");
//        Listener listener = new Listener();
//        eventHandler.subscribe(event, listener);
//        deathTimer = new Timer(timeForLevel, eventHandler, event);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if(contact.isTouching() == false) {
                    return;
                }

                Entity e1 = (Entity)contact.getFixtureA().getBody().getUserData();
                Entity e2 = (Entity)contact.getFixtureB().getBody().getUserData();
                if(e1 != player && e2 != player) {
                    if(e1 instanceof Attack || e2 instanceof Attack){
                        if (e1 instanceof KoopaKoopa || e1 instanceof Bee){
                            if(e1 instanceof KoopaKoopa) {
                                ((KoopaKoopa) e1).decrementHitPoints();
                                if(((KoopaKoopa) e1).isDead()) {
                                    killEntity(e1);
                                    if(player.dieSound.isPlaying()){
                                        player.dieSound.stop();
                                        player.dieSound.play();
                                    }
                                    else {
                                        player.dieSound.play();
                                    }
                                }
                            } else {
                                killEntity(e1);
                                if(player.dieSound.isPlaying()){
                                    player.dieSound.stop();
                                    player.dieSound.play();
                                }
                                else {
                                    player.dieSound.play();
                                }
                            }
                        }
                        if (e2 instanceof KoopaKoopa || e2 instanceof Bee){
                            if(e2 instanceof KoopaKoopa) {
                                ((KoopaKoopa) e2).decrementHitPoints();
                                if(((KoopaKoopa) e2).isDead()) {
                                    killEntity(e2);
                                    if(player.dieSound.isPlaying()){
                                        player.dieSound.stop();
                                        player.dieSound.play();
                                    }
                                    else {
                                        player.dieSound.play();
                                    }
                                }
                            } else {
                                killEntity(e2);
                                if(player.dieSound.isPlaying()){
                                    player.dieSound.stop();
                                    player.dieSound.play();
                                }
                                else {
                                    player.dieSound.play();
                                }
                            }
                        }
                    }
                    // other things colliding
                } else {
                    Player p = e1 == player ? (Player)e1 : (Player)e2;
                    Entity e = p == e1 ? e2 : e1;

                    if(e instanceof Map.Tile) {
                        if (((Map.Tile) e).isDeath) {
                            gameManager.changeState(GameManager.GameState.Fail);
                        }
                        else if (((Map.Tile) e).isWin) {
                            gameManager.changeState(GameManager.GameState.Finish);
                        }
                        else if (((Map.Tile) e).isBouncy){
                            if(player.jumpSound.isPlaying()){
                                player.jumpSound.stop();
                                player.jumpSound.play();
                            }
                            else{
                                player.jumpSound.play();
                            }

                        }

                    } else if(e instanceof KoopaKoopa ||e instanceof Bee) {
                        Vector2 enemyPos = new Vector2(0, 0);
                        if(e instanceof KoopaKoopa) {
                            enemyPos = ((KoopaKoopa) e).body.getPosition();
                        } else {
                            enemyPos = ((Bee) e).body.getPosition();
                        }
                        Vector2 playerPos = p.body.getPosition();
                        Vector2 l = enemyPos.sub(playerPos).nor();

                        Vector2 down = new Vector2(0, -1);

                        float d =  l.dot(down);
                        DLog.debug("{}", d);
                        if(d >= 0.75f) {
                            killEntity(e);
                            player.dieSound.play();

                            player.body.applyLinearImpulse(0f, player.JUMP_IMPULSE * 1.5f, player.body.getLocalCenter().x, player.body.getLocalCenter().y, true);

                        } else {
                            gameManager.changeState(GameManager.GameState.Fail);

                        }

//                        Vector2 contactNormal = contact.getWorldManifold().getNormal();
//                        if(up.dot(contactNormal) > 0) { // entity landed ontop of player
//                            gameManager.changeState(GameManager.GameState.Fail);
//                        } else if (down.dot(contactNormal) > 0) { // player on entity
//                            killEntity(e);
//                            player.body.applyLinearImpulse(0f, player.JUMP_IMPULSE * 2, player.body.getLocalCenter().x, player.body.getLocalCenter().y, true);
//                        } else if(right.dot(contactNormal) > 0) { // entity hit player on right
//                            gameManager.changeState(GameManager.GameState.Fail);
//                        } else if(left.dot(contactNormal) > 0) { // entity hit player on left
//                            gameManager.changeState(GameManager.GameState.Fail);
//                        }

                    } else if (e instanceof MovingPlatform){
                        MovingPlatform plat = (MovingPlatform)e;
                        player.body.setLinearVelocity(player.body.getLinearVelocity().x + plat.body.getLinearVelocity().x*.8f, player.body.getLinearVelocity().y);
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
                if(contact.isTouching() == false) {
                    return;
                }

                Entity e1 = (Entity)contact.getFixtureA().getBody().getUserData();
                Entity e2 = (Entity)contact.getFixtureB().getBody().getUserData();
                if(e1 == player || e2 == player) {
                    Player p = e1 == player ? (Player) e1 : (Player) e2;
                    Entity e = p == e1 ? e2 : e1;
                    if (e1 == player || e2 == player) {
                        if (e1 instanceof MovingPlatform || e2 instanceof MovingPlatform) {
                            MovingPlatform plat = (MovingPlatform) e2;
//                            if (Math.abs(player.body.getLinearVelocity().x) < Math.abs(plat.body.getLinearVelocity().x)*.8f) {
//                                player.body.applyForceToCenter(plat.body.getLinearVelocity().x, 0, false);
//                                //player.body.setLinearVelocity(player.body.getLinearVelocity().x + plat.body.getLinearVelocity().x * .8f, player.body.getLinearVelocity().y);
//                            }
                        }
                    }
                }

            }
        });

        //Begin Music
        music.setLooping(true);
        music.setVolume(35);
        volume = true;
        volumePressed = false;
        music.play();
        player.emergingSound.play();
    }

    @Override
    public void shutdown() {
        for(Entity e : entityList) {
            killEntity(e);
        }
        cleanUpOrphans();

        //Destroy Music
        music.stop();
        music.dispose();

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

    private class onFinishMusicListener implements Music.OnCompletionListener{

        @Override
        public void onCompletion(Music music) {
        }
    }
}