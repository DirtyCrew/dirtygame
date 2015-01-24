package com.dirtycrew.dirtyame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.xml.internal.ws.api.client.SelectOptimalEncodingFeature;

/**
 * Created by Jared on 1/23/15.
 */
public class Map {

    private static final int MAP_TILE_WIDTH = 32;
    private static final int MAP_TILE_HEIGHT = 32;

    //Attributes
    byte[][] map;
    int screenWidth;
    int screenHeight;
    Sprite[][] spriteMap;
    int rows;
    int cols;

    //Methods
    public Map(byte[][] map, int screenWidth, int screenHeight){
        this.map = map;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.rows = this.map.length;
        this.cols = this.map[0].length;

        this.spriteMap = new Sprite[rows][];
        for (int y = 0; y < rows; y++){
            this.spriteMap[y] = new Sprite[cols];
        }

        Texture texture = new Texture("badlogic.jpg");

        //Creating sprite map
        for (int y = 0; y < rows; y++){
            for (int x = 0; x < cols; x++){
                switch (this.map[y][x]){
                    case 0:{
                        break;
                    }
                    case 1:{
                        this.spriteMap[(rows - 1 - y)][x] = new Sprite(texture);
                        this.spriteMap[(rows - 1 - y)][x].setSize(MAP_TILE_WIDTH, MAP_TILE_HEIGHT);
//                        this.spriteMap[y][x].setColor(0.0f, 1.0f, 0.0f, 1.0f);
//                        this.spriteMap[(rows - 1 - y)][x].setTexture(texture);
                        this.spriteMap[(rows - 1 - y)][x].setPosition(x * MAP_TILE_WIDTH, (rows - 1 - y) * MAP_TILE_HEIGHT);
                        break;
                    }
                    case 2:{
                        break;
                    }
                    case 3:{
                        break;
                    }
                }
            }
        }
    }

    public void drawMap(SpriteBatch batch){
        for (int y = 0; y < this.rows; y++) {
            for (int x = 0; x < this.cols; x++) {
                if (this.spriteMap[y][x] != null) {
                    this.spriteMap[y][x].draw(batch);
                }
            }
        }
    }

    public int getWidth(){
        return MAP_TILE_WIDTH * cols;
    }
}
