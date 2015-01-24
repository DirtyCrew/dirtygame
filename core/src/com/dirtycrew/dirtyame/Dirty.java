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
	Texture img;

	
	private Player player;
	private OrthographicCamera camera;
	Map map1;
	Map map2;
	GameState gameState;

	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;

	private static byte[][] byteMapArray1 = {
			{0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
	};

	private static byte[][] byteMapArray2 = {
			{0,1,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,1,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
	};


	EventHandler handler = new EventHandler();
	EventHandler.Event event = new EventHandler.Event();
	Timer timer;

	interface IGameState {
		void init();
		void shutdown();
		void update(Dirty game, float delta);
		void render(Dirty game);

	}


	public abstract class Entity {
		public abstract void update();
	}

	private static class PlayState implements IGameState {

		Player player;
		OrthographicCamera camera;
		Map map;

		@Override
		public void update(Dirty game, float delta) {

			camera.update();

		}

		@Override
		public void render(Dirty game) {
			map.drawMap(camera);

			game.batch.setProjectionMatrix(camera.combined);

			game.batch.begin();
			player.sprite.draw(game.batch);
			game.batch.end();
		}

		@Override
		public void init() {
			// create player
			Texture playerTexture = new Texture("badlogic.jpg");
			player = new Player();
			player.sprite = new Sprite(playerTexture);
			player.sprite.setPosition(0, 0);
			player.sprite.setSize(50, 50);

			float viewportWidth = 800;
			float viewportHeight = 600;
			camera = new OrthographicCamera(viewportWidth, viewportHeight);
			camera.position.set(camera.viewportWidth / 2.f, camera.viewportHeight / 2.f, 0);
			camera.update();

			map = new Map("example_map.tmx");
		}

		@Override
		public void shutdown() {

		}
	}

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

	@Override
	public void render () {
		currentState.update(this, Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		currentState.render(this);

//		if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
//			gameState.setMap(map1);
//		}
//
//		if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
//			gameState.setMap(map2);
//		}
//
//		// get the delta time
//		float deltaTime = Gdx.graphics.getDeltaTime();
//		// update the player (process input, collision detection, position update)
//		player.update(deltaTime, gameState.map);
//
//		// let the camera follow the player, x-axis only
////		camera.position.x = player.getPosition().x + player.PLAYER_WIDTH / 2;
////		if (camera.position.x < 640 / 2){
////			camera.position.x = 640 / 2;
////		}
////		if (camera.position.x > gameState.map.getWidth() - 640/2){
////			camera.position.x = gameState.map.getWidth() - 640/2;
////		}
//		camera.update();
//
//		batch.setProjectionMatrix(camera.projection);
//		batch.setTransformMatrix(camera.view);
//		batch.begin();
//		batch.draw(player.sprite, 0, 0, 25, 25);
//
//
////		gameState.map.drawMap(batch);
//
//		tiledMapRenderer.setView(camera);
//		tiledMapRenderer.render();

//		batch.end();
	}

//	public class Listener implements EventHandler.EventListener{
//
//		@Override
//		public void onEvent(EventHandler.Event e) {
//			if(e.getState().equals("Timer"))
//			{
//				DLog.debug("Game Over");
//			}
//		}
//	};

}
