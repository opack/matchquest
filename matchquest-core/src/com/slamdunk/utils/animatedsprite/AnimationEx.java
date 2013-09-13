package com.slamdunk.utils.animatedsprite;

import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slamdunk.utils.PropertiesEx;

/**
 * Permet de g�rer une animation disposant des propri�t�s particuli�res de l'objet
 * Frame (runnables, rotation par frame...).
 */
public class AnimationEx extends Animation {
	/**
	 * Images utilis�es dans l'animation. Principalement utilis� par le contructeur
	 * de copie.
	 */
	private TextureRegion[] regions;
	/**
	 * Donn�es li�es � chaque trame � afficher 
	 */
	private Frame[] frames;
	/**
	 * Trame actuellement dessin�e
	 */
	private Frame currentFrame;
	/**
	 * Flag indiquant si la trame vient de changer
	 */
	private boolean newFrame;
	/**
	 * Force la mise � jour de la trame au prochain updateCurrentFrame()
	 */
	private boolean forceNewFrame;
	/**
	 * Temps �coul�
	 */
	private float stateTime;
	
	public AnimationEx(AnimationEx model) {
		super(model.frameDuration, model.regions);
		frames = new Frame[model.frames.length];
		for (int curFrame = 0; curFrame < frames.length; curFrame++) {
			frames[curFrame] = new Frame(model.frames[curFrame]);
		}
		newFrame = true;
		regions = model.regions;
		stateTime = model.stateTime;
	}

	public AnimationEx(TextureRegion[] regions, PropertiesEx properties) {
		super(properties.getFloatProperty("animation.frameDuration", 0.1f), regions);
		this.regions = regions;
		
		// Initialisation de la table de Frame
		frames = new Frame[regions.length];

		// Lecture des informations du clip
		for (int frame = 0; frame < frames.length; frame++) {
			frames[frame] = new Frame(frame, regions[frame]);
			frames[frame].load(properties);
		}
		
		newFrame = true;
	}
	
	public Frame getCurrentFrame() {
		return currentFrame;
	}
	
	public float getStateTime() {
		return stateTime;
	}

	public boolean isFinished() {
		return isAnimationFinished(stateTime);
	}

	/**
	 * Ex�cute les runnables associ�s � la trame courante s'ils n'ont pas
	 * encore �t� lanc�s.
	 */
	public void runFrameRunnables() {
		if (newFrame && currentFrame != null) {
			Collection<FrameRunnable> runnables = currentFrame.getRunnables();
			if (runnables != null) {
				for (FrameRunnable runnable : runnables) {
					runnable.run();
				}
			}
		}
	}

	public void setCurrentFrame(int frameIndex) {
		// R�cup�ration de la trame actuelle
		currentFrame = frames[frameIndex];
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	/**
	 * Choisit la trame courante en fonction du temps �coul�
	 */
	public Frame updateCurrentFrame() {
		// R�cup�re la trame courante
		int frameIndex = getKeyFrameIndex(stateTime);
		newFrame = forceNewFrame || currentFrame == null || frameIndex != currentFrame.getIndex();
		forceNewFrame = false;
		if (newFrame) {
			setCurrentFrame(frameIndex);
		}
		return currentFrame;
	}

	public boolean isNewFrame() {
		return newFrame;
	}

	public void forceNewFrame() {
		forceNewFrame = true;
	}
}
