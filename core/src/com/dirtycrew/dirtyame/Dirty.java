package com.dirtycrew.dirtyame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Dirty extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	private static Player player;
	private OrthographicCamera camera;
	Map map;
	World world;
	Body playerBody;
	Body blockBody;
	Box2DDebugRenderer debugRenderer;

	@Override
	public void create () {
		DLog.debug("ScreenWidth: {} ScreenHeight:{}", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		DLog.error("ScreenWidth: {} ScreenHeight:{}", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);
		camera.update();
		world = new World(new Vector2(0, 0), true);
		// First we create a body definition
		BodyDef playerBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		playerBodyDef.position.set(0, 0);
		playerBody = world.createBody(playerBodyDef);
		PolygonShape playerBox = new PolygonShape();
		playerBox.setAsBox(25, 25);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = playerBox;
		fixtureDef.density = 0.0f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 1f; // Make it bounce a little bit

		playerBody.createFixture(fixtureDef);

		player = new Player(playerBody);

		BodyDef boxBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.StaticBody;
		boxBodyDef.position.set(200, 0);
		blockBody = world.createBody(boxBodyDef);
		PolygonShape blockBox = new PolygonShape();
		blockBox.setAsBox(25, 25);
		blockBody.createFixture(blockBox, 0.0f);
		blockBox.dispose();
		playerBox.dispose();

		batch = new SpriteBatch();

		byte[][] byteMapArray = {
				{0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
				{0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
		};

		map = new Map(byteMapArray, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// get the delta time
		float deltaTime = Gdx.graphics.getDeltaTime();
		// update the player (process input, collision detection, position update)
		player.update(deltaTime, map);

		// let the camera follow the player, x-axis only
		camera.position.x = player.getPosition().x + player.PLAYER_WIDTH / 2;
		if (camera.position.x < 640 / 2){
			camera.position.x = 640 / 2;
		}
		if (camera.position.x > map.getWidth() - 640/2){
			camera.position.x = map.getWidth() - 640/2;
		}
		camera.update();

		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);
		debugRenderer.render(world, camera.combined);
		world.step(1/60f, 6, 2);

		batch.begin();
		batch.draw(img, player.getPosition().x, player.getPosition().y, player.PLAYER_WIDTH, player.PLAYER_HEIGHT);


		map.drawMap(batch);

		batch.end();
	}

}
