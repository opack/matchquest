package com.slamdunk.matchquest.dungeon.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.slamdunk.utils.Clip;

public class PuzzleImage extends Image {
	private PuzzleAttributes attribute;
	private int puzzleX;
	private int puzzleY;
	private boolean isEatable;
	private boolean canFall;
	private Clip clip;
	
	public PuzzleImage(PuzzleAttributes attribute) {
		super(attribute.getDrawable());
		setAttribute(attribute);
		canFall = true;
	}
	
	@Override
	public void addAction(Action action) {
		// Création d'une séquence action pour pouvoir ajouter d'éventuelles autres actions par la suite
		if (getActions().size == 0) {
			super.addAction(Actions.sequence(action));
		} else {
			// Si on arrive ici, c'est qu'on veut ajouter une action mais qu'il y en a déjà d'autres en cours.
			// La seule action est forcément une SequenceAction (on l'a créée juste au-dessus), donc on peut
			// ajouter celle-ci à la suite. Le but est de faire en sorte que les actions ne soient plus
			// exécutées en parallèle mais les unes à la suite des autres.
			SequenceAction sequence = (SequenceAction)getActions().get(0);
			sequence.addAction(action);
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (clip == null) {
			super.draw(batch, parentAlpha);
		} else {
			clip.drawArea.x = getX();
			clip.drawArea.y = getY();
			clip.drawArea.width = getWidth();//clip.getKeyFrame(0).getRegionWidth();
			clip.drawArea.height = getHeight();//clip.getKeyFrame(0).getRegionHeight();
			clip.play(Gdx.graphics.getDeltaTime(), batch);
		}
	}

	public PuzzleAttributes getAttribute() {
		return attribute;
	}

	public int getPuzzleX() {
		return puzzleX;
	}

	public int getPuzzleY() {
		return puzzleY;
	}

	public boolean isCanFall() {
		return canFall;
	}
	
	public boolean isEatable() {
		return isEatable;
	}
	
	public void setAttribute(PuzzleAttributes attribute) {
		this.attribute = attribute;
		isEatable = attribute != PuzzleAttributes.EMPTY;
		if (attribute.getClip() == null) {
			clip = null;
			setDrawable(attribute.getDrawable());
		} else {
			clip = new Clip(attribute.getClip());
			setDrawable(null);
		}
	}
	
	public void setCanFall(boolean canFall) {
		this.canFall = canFall;
	}
	
	public void setEatable(boolean isEatable) {
		this.isEatable = isEatable;
	}

	public void setPuzzleX(int puzzleX) {
		this.puzzleX = puzzleX;
	}

	public void setPuzzleXY(int puzzleX, int puzzleY) {
		this.puzzleX = puzzleX;
		this.puzzleY = puzzleY;
	}

	public void setPuzzleY(int puzzleY) {
		this.puzzleY = puzzleY;
	}
	
	@Override
	public String toString() {
		return getAttribute() + " at " + getPuzzleX() + ";" + getPuzzleY();
	}
}
