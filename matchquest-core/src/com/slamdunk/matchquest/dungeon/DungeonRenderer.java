package com.slamdunk.matchquest.dungeon;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.slamdunk.matchquest.Assets;
import com.slamdunk.matchquest.MatchQuest;
import com.slamdunk.matchquest.dungeon.objects.DungeonObject;
import com.slamdunk.matchquest.dungeon.objects.Mob;
import com.slamdunk.utils.Clip;

public class DungeonRenderer {
	public static final int DISPLAY_WIDTH = MatchQuest.screenWidth;
	public static final int DISPLAY_HEIGHT = 60;
	private static final float GROUND_OFFSET = 30f;
	private static final float STATS_IMG_SIZE = 16;
	private static final float STATS_PADDING = 2;

	private DungeonWorld world;
	/**
	 * Liste des effets visuels en cours
	 */
	private List<Clip> runningVisualEffects;
	/**
	 * Liste des effets visuels terminés
	 */
	private List<Clip> finishedVisualEffects;

	private Rectangle renderArea;
	private SpriteBatch spriteBatch;
	private float ppuX; // pixels per unit on the X axis
	private float ppuY; // pixels per unit on the Y axis
	/**
	 * Position du héros au début du render
	 */
	private float currentHeroX;
	/**
	 * Position minimale qui sera dessinée au cours de ce render
	 */
	private float minRenderX;
	/**
	 * Position maximale qui sera dessinée au cours de ce render
	 */
	private float maxRenderX;
	
	/**
	 * Label utilisé pour dessiner les textes des stats
	 */
	private Label statsLabel;
	
	public DungeonRenderer(DungeonWorld world) {
	    this.world = world;
	    
	    renderArea = new Rectangle();
	    spriteBatch = new SpriteBatch();
	    
	    runningVisualEffects = new ArrayList<Clip>();
	    finishedVisualEffects = new ArrayList<Clip>();
	    
	    LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Assets.characterFont;
		statsLabel = new Label("--", labelStyle);
	}

	/**
	 * Dessine les mobs en faisant un clipping : seuls les objets après le joueur
	 * et dans la partie visible par la caméra sont affichés
	 * @param mobs
	 */
	private void drawMobs(List<Mob> mobs) {
		// Zone où le texte sera affiché. La zone s'étend du bas de la zone de rendu
		// jusqu'au sol.
		Rectangle statsBounds = new Rectangle();
		statsBounds.y = renderArea.y;
		statsBounds.height = GROUND_OFFSET;
		
		Rectangle mobBounds = new Rectangle();
		float imgX;
	    for (Mob mob : mobs) {
	    	if (mob != null) {
	    		// Dessin du mob
	    		drawObject(mob);
	    		
	    		// Dessin de ses statistiques
	    		mobBounds.set(mob.getBounds());
	    		mobBounds.x *= ppuX;
	    		mobBounds.y *= ppuY;
	    		mobBounds.width *= ppuX;
	    		mobBounds.height *= ppuY;
	    		if (isVisible(mob)) {
		    		statsLabel.setText(String.valueOf(mob.getHp()));
		    		imgX = mobBounds.x + mobBounds.width / 2 - STATS_IMG_SIZE / 2 + STATS_PADDING - statsLabel.getWidth() / 2;
		    		spriteBatch.draw(
	    				Assets.hud_heart,
	    				imgX,
	    				renderArea.y + GROUND_OFFSET - STATS_IMG_SIZE - STATS_PADDING,
	    				STATS_IMG_SIZE,
	    				STATS_IMG_SIZE);
		    		statsLabel.setPosition(
	    				imgX + STATS_IMG_SIZE + STATS_PADDING,
		    			renderArea.y + GROUND_OFFSET - statsLabel.getHeight());
		    		statsLabel.draw(spriteBatch, 1.0f);
	    		}
	    	}
	    }
	}

	/**
	 * Dessine un objet du monde
	 * @param object
	 */
	private void drawObject(DungeonObject object) {
		if (object != null) {
    		// Mise à jour de la zone de dessin et rendu de la frame uniquement si l'objet est visible
    		if (isVisible(object)) {
	    		// Dessin de la frame
	    		object.draw(spriteBatch, renderArea.x - currentHeroX, renderArea.y + GROUND_OFFSET);
	    		
	    		// Dessin des sous-objets
	    		if (object.getObjects() != null) {
		    		for (DungeonObject subObject : object.getObjects()) {
		    			drawObject(subObject);
		    		}
	    		}
    		}
    	}
	}

	/**
	 * Dessine les objets en faisant un clipping : seuls les objets après le joueur
	 * et dans la partie visible par la caméra sont affichés
	 * @param objects
	 */
	private void drawObjects(List<? extends DungeonObject> objects) {
	    for (DungeonObject object : objects) {
	    	if (object != null && isVisible(object)) {
	    		drawObject(object);
	    	}
	    }
	}
	
	private void drawVisualEffects(float delta, List<Clip> worldVisualEffects) {
		// Supprime les clips terminés
		for (Clip clip : runningVisualEffects) {
			if (clip.isFinished()) {
				finishedVisualEffects.add(clip);
			}
		}
		runningVisualEffects.removeAll(finishedVisualEffects);
		finishedVisualEffects.clear();
		
		// Récupère les nouveaux clips en adaptant les coordonnées de dessin
		if (!worldVisualEffects.isEmpty()) {
			final float heroX = world.getHero().getX();
			for (Clip clip : worldVisualEffects) {
				// Adapte les coordonnées world-relative à des coordonnées renderArea-relative
				clip.drawArea.x = renderArea.x + (clip.drawArea.x - heroX) * ppuX;
				clip.drawArea.y = renderArea.y + GROUND_OFFSET + clip.drawArea.y * ppuY;
				clip.drawArea.width *= ppuX;
				clip.drawArea.height *= ppuY;
				runningVisualEffects.add(clip);
			}
			worldVisualEffects.clear();
		}
		
		// Dessine les clips
		for (Clip clip : runningVisualEffects) {
			clip.play(delta, spriteBatch);			
		}
	}
	
	/**
	 * Indique si l'objet indiqué est visible au moins en partie, et doit donc être dessiné.
	 * @param object
	 */
	public boolean isVisible(DungeonObject object) {
		return object.isVisible() && object.getRight() >= minRenderX && object.getX() <= maxRenderX;
	}
	
	public void render(float delta) {
		// Mise à jour des variables de travail utilisée pour le dessin
		currentHeroX = world.getHero().getX();
		minRenderX = currentHeroX;
		maxRenderX = currentHeroX + DISPLAY_WIDTH;
		
		// Dessin des objets du monde
	    spriteBatch.begin();
	    drawObjects(world.getGrounds());
	    drawMobs(world.getMobs());
	    drawObject(world.getHero());
	    //drawVisualEffects(delta, world.getVisualEffects());
	    drawObjects(world.getVisualEffects());
// TODO        drawItems();
	    drawFPS(spriteBatch);
	    spriteBatch.end();
	}

	private void drawFPS(SpriteBatch spriteBatch) {
		int fps = Gdx.graphics.getFramesPerSecond();
		String msg = String.valueOf(fps) + "FPS";
		Assets.characterFont.draw(spriteBatch, msg, 0, 790);
	}

	public void setRenderArea(float x, float y, float width, float height) {
		renderArea.set(x, y, width, height);
		
	    ppuX = (float)width / DISPLAY_WIDTH;
	    ppuY = (float)height / DISPLAY_HEIGHT;
	}
}
