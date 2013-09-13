package com.slamdunk.matchquest.dungeon;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.slamdunk.matchquest.MatchQuest;

/**
 * Affiche le contenu d'un donjon et gère ce qui le compose (ennemis, coffres...).
 * Etend Actor pour pouvoir être positionné simplement dans un Stage.
 */
public class Dungeon extends Actor {
	private DungeonWorld world;
	private DungeonRenderer renderer;
	private boolean isRenderAreaValid;
	private boolean isGameOver;
	
	public Dungeon(int length) {
		world = new DungeonWorld(length);
		renderer = new DungeonRenderer(world);
	}
	
	public DungeonWorld getWorld() {
		return world;
	}

	public void render(float delta) {
		// Met à jour les acteurs : déplace le héros, les ennemis, le background
		world.update(delta);
		
		// Fin du donjon ?
		if (!isGameOver && world.getHero().getRight() > world.getLength()) {
			MatchQuest.getInstance().getScreen().showMessage("Fin du donjon !");
			isGameOver = true;
		}
		
		// Dessine le donjon
		if (!isRenderAreaValid) {
			renderer.setRenderArea(getX(), getY(), getWidth(), getHeight());
			isRenderAreaValid = true;
		}
		renderer.render(delta);
	}

	@Override
	public void setHeight(float height) {
		super.setHeight(height);
		isRenderAreaValid = false;
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		isRenderAreaValid = false;
	}
	
	public void setRenderArea(float x, float y, float width, float height) {
		setPosition(x, y);
		setSize(width, height);
		isRenderAreaValid = false;
	}
	
	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		isRenderAreaValid = false;
	}
	
	@Override
	public void setWidth(float width) {
		super.setWidth(width);
		isRenderAreaValid = false;
	}
	
	@Override
	public void setX(float x) {
		super.setX(x);
		isRenderAreaValid = false;
	}

	@Override
	public void setY(float y) {
		super.setY(y);
		isRenderAreaValid = false;
	}
}
