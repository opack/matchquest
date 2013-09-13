package com.slamdunk.matchquest.dungeon.objects;

import com.slamdunk.matchquest.MatchQuest;
import com.slamdunk.utils.Config;

public class Mob extends DungeonObject implements Damageable {
	public static final float SPEED = Config.asFloat("rabite.speed", 2f); // Vitesse de déplacement : 1m/sec
	
	private int hp;
	private int att;
	
	public Mob(float position, int hp, int att) {
		super(ObjectType.MOB_RABITE);
		
		// Ajoute les animations possibles de cet objet
	    loadAnimation(Stance.ATTACKING.name(), "clips/mob_rabite-attack.clip");
	    loadAnimation(Stance.DYING.name(), "clips/mob_rabite-death.clip");
	    loadAnimation(Stance.IDLE.name(), "clips/mob_rabite-idle.clip");
	    loadAnimation(Stance.MOVING.name(), "clips/mob_rabite-move.clip");
	    loadAnimation(Stance.THINKING.name(), "clips/mob_rabite-think.clip");
		
		// Définit les propriétés de l'objet
		this.hp = hp;
	    this.att = att;
	    
	    setPosition(position, 0);
	    setSpeed(SPEED);
	    setBlocking(true);
	    setStance(Stance.IDLE);
	}
	
	/**
	 * Méthode appelée lorsque le mob attaque
	 */
	@Override
	protected void attack() {
		MatchQuest.getInstance().getPlayer().receiveHit(att);
		setActionDone(true);
	}

	@Override
	public void die() {
		if (isAnimationFinished()) {
			// Suppression du mob de la liste des mobs
			getWorld().removeMob(this);
			// L'action a été effectuée
			setActionDone(true);
		}
	}

	public int getAtt() {
		return att;
	}

	public int getHp() {
		return hp;
	}
	
	@Override
	public boolean isDead() {
		return hp <= 0;
	}
	
	@Override
	public void receiveHit(int hit) {
		hp -= hit;
	}

	public void setAtt(int att) {
		this.att = att;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	@Override
	public void setStance(Stance stance) {
		// Une fois que le Mob est mort, il le reste.
		if (getStance() == Stance.DYING
		// Si un mob est mort, on ne peut lui affecter que la stance Dying
		|| (isDead() && stance != Stance.DYING)) {
			return;
		}
		
		// Changement de la pose
		super.setStance(stance);
		
		// Suivant la pose, on demande une petite tempo pour que le joueur
		// voit les choses se passer
		switch (stance) {
			case IDLE: 
				setWaitTime(0.15f); 
				break;
			case THINKING: 
				setWaitTime(0.3f); 
				break;
		}
	}

	/**
	 * Choisit la nouvelle pose du mob, càd son action
	 */
	@Override
	protected void think() {
		setActionDone(true);
		
		// Attaque si le héros est à portée
		Hero hero = getWorld().getHero();
		float distanceToHero = getX() - (hero.getRight());
		float maxMoveDistance = getWorld().maxMoveDistance(this, -1);
		if (distanceToHero > 0 && distanceToHero <= 1) {
			// Attaque si le héros est à côté
			setStance(Stance.ATTACKING);
		}
		// Se rapproche du héros s'il est en vue
		else if (distanceToHero < 6
		&& maxMoveDistance != 0) {
			setStance(Stance.MOVING);
			setDestination(getX() + maxMoveDistance);
		}
		// Fait des claquettes le reste du temps
		else {
			setStance(Stance.IDLE);
		}
	}
}
