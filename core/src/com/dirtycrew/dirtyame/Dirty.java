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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class Dirty extends ApplicationAdapter {
	SpriteBatch batch;
	World world;
	Box2DDebugRenderer debugRenderer;

	IGameState currentState;

	@Override
	public void create () {
		DLog.debug("ScreenWidth: {} ScreenHeight:{}", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		world = new World(Constants.GRAVITY, false);
		debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();

		Gdx.graphics.setDisplayMode(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);
		currentState = new PlayState();
		currentState.init(this);
	}

	private void doUpdate() {
		currentState.update(this, Gdx.graphics.getDeltaTime());
		world.step(1/60f, 6, 2);
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
