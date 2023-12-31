package com.mygdx.game3;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import org.w3c.dom.css.Rect;

import java.sql.Time;
import java.util.Iterator;

public class SpaceShip extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, tNave, tMissile, tEnemy;
	private Sprite nave, missile;
	private float posX;
	private float posY;
	private float velocity;
	private float xMissile, yMissile;
	private boolean attack;
	private Array<Rectangle> enemys;
	private long lastEnemyTime;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("bg.png");
		tNave = new Texture("spaceship.png");
		nave = new Sprite(tNave);
		posX = 0;
		posX = 0;
		velocity = 10;


		tMissile = new Texture("missile.png");
		missile = new Sprite(tMissile);
		xMissile = posX;
		yMissile = posY;
		attack = false;

		tEnemy = new Texture("enemy.png");
		enemys = new Array<Rectangle>();
		lastEnemyTime = 0;

	}

	@Override
	public void render () {
		this.moveEnemies();
		this.moveMissiles();
		this.moveNave();
		ScreenUtils.clear(51, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		if(attack){
			batch.draw(missile, xMissile + nave.getHeight() / 2,  yMissile + nave.getHeight() / 2 - 12);
		}
		batch.draw(tNave, posX,  posY);
		for(Rectangle enemy : enemys){
			batch.draw(tEnemy, enemy.x,  enemy.y);
		}
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		tNave.dispose();
	}

	public void moveNave() {
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			if( posX < Gdx.graphics.getWidth() - nave.getWidth()){
				posX += velocity;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			if( posX > 0){
				posX -= velocity;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			if( posY < Gdx.graphics.getHeight() - nave.getHeight()){
				posY += velocity;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			if( posY > 0){
				posY -= velocity;
			}
		}
	}

	public void moveMissiles(){
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && !attack){
			attack = true;
			yMissile = posY;
		}
		if(attack){
			if(xMissile < Gdx.graphics.getWidth()){
				xMissile += 40;
			}else {
				xMissile = posX;
				attack = false;
			}
		}else{
			xMissile = posX;
			yMissile = posY;
		}

	}

	private void spawnEnemies(){

		Rectangle enemy = new Rectangle(Gdx.graphics.getWidth(), MathUtils.random(0, Gdx.graphics.getWidth() - tEnemy.getHeight()), tEnemy.getWidth(), tEnemy.getHeight());
		enemys.add(enemy);
		lastEnemyTime = TimeUtils.nanoTime();
	}

	private void moveEnemies(){
		if(TimeUtils.nanoTime() - lastEnemyTime > 1000000000){
			this.spawnEnemies();
		}

		for ( Iterator<Rectangle> iter = enemys.iterator(); iter.hasNext(); ){
			Rectangle enemy = iter.next();
			enemy.x -= 400 * Gdx.graphics.getDeltaTime();
			if(enemy.x + tEnemy.getWidth() < 0){
				iter.remove();
			}
		}
	}
}
