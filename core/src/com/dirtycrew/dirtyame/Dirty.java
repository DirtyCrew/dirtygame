package com.dirtycrew.dirtyame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Dirty extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	EventHandler handler = new EventHandler();
	EventHandler.Event event = new EventHandler.Event();
	Timer timer;



	@Override
	public void create () {
		DLog.debug("ScreenWidth: {} ScreenHeight:{}", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		DLog.error("ScreenWidth: {} ScreenHeight:{}", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		event.setState("Timer");
		timer = new Timer(100,handler,event);
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		handler.suscribe(event, new Listener());
	}

	@Override
	public void render () {
		timer.update();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
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
