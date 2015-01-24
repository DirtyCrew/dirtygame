package com.dirtycrew.dirtyame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class Dirty extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	
	private static Player player = new Player();
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

	@Override
	public void create () {
		DLog.debug("ScreenWidth: {} ScreenHeight:{}", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		DLog.error("ScreenWidth: {} ScreenHeight:{}", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();


		event.setState("Timer");
		timer = new Timer(100,handler,event);

		batch = new SpriteBatch();

		map1 = new Map(byteMapArray1, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		map2 = new Map(byteMapArray2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		gameState = new GameState(player, map1);

		tiledMap = new TmxMapLoader().load("example_map.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);


		img = new Texture("badlogic.jpg");
		handler.suscribe(event, new Listener());
	}

	@Override
	public void render () {
		timer.update();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
			gameState.setMap(map1);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
			gameState.setMap(map2);
		}

		// get the delta time
		float deltaTime = Gdx.graphics.getDeltaTime();
		// update the player (process input, collision detection, position update)
		player.update(deltaTime, gameState.map);

		// let the camera follow the player, x-axis only
		camera.position.x = player.getPosition().x + player.PLAYER_WIDTH / 2;
		if (camera.position.x < 640 / 2){
			camera.position.x = 640 / 2;
		}
		if (camera.position.x > gameState.map.getWidth() - 640/2){
			camera.position.x = gameState.map.getWidth() - 640/2;
		}
		camera.update();

		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);
		batch.begin();
		batch.draw(img, player.getPosition().x, player.getPosition().y, player.PLAYER_WIDTH, player.PLAYER_HEIGHT);


//		gameState.map.drawMap(batch);

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.end();
	}

	public class Listener implements EventHandler.EventListener{

		@Override
		public void onEvent(EventHandler.Event e) {
			if(e.getState().equals("Timer"))
			{
				DLog.debug("Game Over");
			}
		}
	};

}
