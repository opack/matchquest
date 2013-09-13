package com.slamdunk.matchquest.dungeon.objects;

import java.util.LinkedList;
import java.util.Queue;

import com.slamdunk.matchquest.MatchQuest;
import com.slamdunk.matchquest.actions.HeroAction;
import com.slamdunk.utils.Config;

/**
 * Représentation du joueur dans un donjon. Les propriétés du héros (hp, actions...)
 * sont contenues dans l'objet Player car elles sont utilisables partout dans le jeu.
 */
public class Hero extends DungeonObject {
	public static final float SPEED = Config.asFloat("hero.speed", 2f);
	
	private boolean puzzleSteady;
	private boolean playerPlayed;
	
	private DungeonObject weapon;
	private DungeonObject potion;
	private DungeonObject waitForIdle;
	private Queue<HeroAction> actions;
	
	public Hero(float position) {
		super(ObjectType.HERO);
		
		 // Ajoute les animations possibles de cet objet
	    loadAnimation(Stance.ATTACKING.name(), "clips/hero-attack.clip");
	    loadAnimation(Stance.HEALING.name(), "clips/hero-heal.clip");
	    loadAnimation(Stance.IDLE.name(), "clips/hero-idle.clip");
	    loadAnimation(Stance.MOVING.name(), "clips/hero-move.clip");
	    loadAnimation(Stance.THINKING.name(), "clips/hero-idle.clip");
		
		// Définit les propriétés de l'objet
	    actions = new LinkedList<HeroAction>();
	    setPosition(position, 0);
	    setSpeed(SPEED);
	    setBlocking(true);
	    setStance(Stance.THINKING);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		// Avance le héros à la fin de son tour
		if (playerPlayed && isDependencyIdle()) {
			// On n'attend plus personne
			waitForIdle = null;
			if (puzzleSteady) {
				if (actions.isEmpty()) {
					// Le joueur a joué
					MatchQuest.getInstance().getPlayer().setTurnOver(true);
					// Avance le héros
					advance();
					// RAZ des flags
					playerPlayed = false;
					puzzleSteady = false;
				} else {
					actions.poll().perform();
				}
			}
		}
	}

	private boolean isDependencyIdle() {
		return waitForIdle == null || (waitForIdle != null && waitForIdle.isTurnOver());
	}

	@Override
	protected void attack() {
		super.attack();
		waitForIdle = weapon;
	}

	@Override
	protected void heal() {
		super.heal();
		waitForIdle = potion;
	}
	
	/**
	 * Avance le héros d'1m
	 */
	private void advance() {
		float distance = getWorld().maxMoveDistance(this, getWidth());
		if (distance != 0) {
			setDestination(getX() + distance);
			setStance(Stance.MOVING);
		}
	}
	
	public boolean isPlayerPlayed() {
		return playerPlayed;
	}

	public boolean isPuzzleSteady() {
		return puzzleSteady;
	}

	@Override
	public boolean isTurnOver() {
		// Le tour du héros est fini s'il ne fait plus rien et que le joueur a déjà joué
		return isIdle() && MatchQuest.getInstance().getPlayer().isTurnOver();
	}

	public void setPlayerPlayed(boolean playerPlayed) {
		this.playerPlayed = playerPlayed;
	}
	
	public void setPuzzleSteady(boolean puzzleSteady) {
		this.puzzleSteady = puzzleSteady;
	}
	
	@Override
	protected void idle() {
		// Lorsque le héros ne fait rien, il tente d'exécuter la prochaine action
		if (actions.isEmpty()) {
			setActionDone(true);
		} else if (isDependencyIdle()) {
			setStance(Stance.THINKING);
		}
	}
	
	@Override
	protected void think() {
		super.think();
		// Si le héros doit penser, alors le tour du joueur débute
		MatchQuest.getInstance().getPlayer().setTurnOver(false);
		// On regarde s'il y a une action à effectuer
		if (!actions.isEmpty()) {
			actions.poll().perform();
		} else {
			setStance(Stance.IDLE);
		}
	}

	public void setWeapon(DungeonObject weapon) {
		this.weapon = weapon;
	}
	
	public void setPotion(DungeonObject potion) {
		this.potion = potion;
	}

	public void addAction(HeroAction action) {
		actions.add(action);
	}
}
