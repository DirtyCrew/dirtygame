package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class Dirty extends ApplicationAdapter {
	SpriteBatch batch;
	GameManager gameManager;
	int map = 0;

	int numPlayers = 1;


	public void setMap(int map) {
		this.map = map;
	}
	public void setNumPlayers(int players) {
		numPlayers = players;
	}


	@Override
	public void create () {
		batch = new SpriteBatch();
		if(Config.FULLSCREEN) {
			Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
		} else {
			Gdx.graphics.setDisplayMode(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);
		}
		gameManager = new GameManager(this, GameManager.GameState.Tutorial);
	}

	private void doUpdate() {
		gameManager.update();
		gameManager.getState().update(this, Gdx.graphics.getDeltaTime());


		//world.clearForces();
	}

	private void doRender() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameManager.getState().render(this);
	}

	@Override
	public void render () {
		doUpdate();
		doRender();

	}

}
