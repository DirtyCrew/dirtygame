package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.security.Key;

/**
 * Created by Jared on 1/24/15.
 */
public class StartState implements IGameState {

    OrthographicCamera titleCamera;
    OrthographicCamera mapTileCamera;
    BitmapFont titleFont;
    GameManager gameManager;
    SpriteBatch titleBatch;
    SpriteBatch mapTileBatch;

    private final static int titleCameraZoom = 16;
    private final static String gameTitle = "The Adventures of Dirtyame";
    private final static String gameStartInfo = "Select your Level.";

    public StartState(GameManager gameManager, Dirty game){
        this.gameManager = gameManager;

        this.init(game);
    }

    @Override
    public void init(Dirty game){
        titleCamera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        titleCamera.zoom += titleCameraZoom;//16

        mapTileCamera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        titleFont = new BitmapFont(Gdx.files.internal("font.fnt"));
        titleFont.setColor(Color.NAVY);

        titleBatch = new SpriteBatch();
        mapTileBatch = new SpriteBatch();
    }

    @Override
    public void shutdown(){

    }

    @Override
    public void update(Dirty game, float delta){
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            gameManager.changeState(GameManager.GameState.Play);
        }
    }

    @Override
    public void render(Dirty game){
        game.batch.begin();
        Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Texture texture = new Texture("generic_platformer_tiles.png");
        game.batch.draw(texture, 0, -285);
        game.batch.end();

        mapTileCamera.update();
        mapTileBatch.setProjectionMatrix(mapTileCamera.projection);
        mapTileBatch.begin();
        Texture map1Image = new Texture("white_square.png");
        Texture map2Image = new Texture("white_square.png");
        Texture map3Image = new Texture("white_square.png");
        Texture map4Image = new Texture("white_square.png");
        Sprite map1Sprite = new Sprite(map1Image);
        Sprite map2Sprite = new Sprite(map2Image);
        Sprite map3Sprite = new Sprite(map3Image);
        Sprite map4Sprite = new Sprite(map4Image);
        mapTileBatch.draw(map1Sprite, -7.5f, -5f, 5, 5);
        mapTileBatch.draw(map2Sprite, 4.25f, -5f, 5, 5);
        mapTileBatch.draw(map3Sprite, -7.5f, -11f, 5, 5);
        mapTileBatch.draw(map4Sprite, 4.25f, -11f, 5, 5);
        mapTileBatch.end();

        titleCamera.update();
        titleBatch.setProjectionMatrix(titleCamera.projection);
        titleBatch.begin();
        BitmapFont.TextBounds titleBounds = titleFont.getBounds(gameTitle);
        titleFont.draw(titleBatch, gameTitle,
                (0 - titleBounds.width / 2),
                ((Constants.SCREEN_HEIGHT / 2) - 250));
//                (0 + titleBounds.height / 2));
        BitmapFont.TextBounds infoBounds = titleFont.getBounds(gameStartInfo);
        titleFont.draw(titleBatch, gameStartInfo,
                (0 - infoBounds.width / 2),
                ((Constants.SCREEN_HEIGHT / 2) - 350));

        titleFont.draw(titleBatch, "1", -100, 0);
        titleFont.draw(titleBatch, "2", 100, 0);
        titleFont.draw(titleBatch, "3", -100, -100);
        titleFont.draw(titleBatch, "4", 100, -100);

        titleBatch.end();
    }
}
