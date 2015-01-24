package com.dirtycrew.dirtyame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class Dirty extends ApplicationAdapter {
	SpriteBatch batch;

	public abstract class Entity {
		public abstract void update();
	}

	Map map1;
	
	IGameState currentState;

	@Override
	public void create () {
		DLog.debug("ScreenWidth: {} ScreenHeight:{}", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.graphics.setDisplayMode(1024, 768, false);
		currentState = new PlayState();
		currentState.init();

		map1 = new Map("example_map.tmx");

		batch = new SpriteBatch();
	}

	private void doUpdate() {
		currentState.update(this, Gdx.graphics.getDeltaTime());
	}

	private void doRender() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		currentState.render(this);
	}

	@Override
	public void render () {
		doUpdate();
		doRender();
	}

}
