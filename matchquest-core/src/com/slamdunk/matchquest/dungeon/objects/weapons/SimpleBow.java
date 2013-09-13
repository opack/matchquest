package com.slamdunk.matchquest.dungeon.objects.weapons;

import com.slamdunk.matchquest.dungeon.objects.DungeonObject;
import com.slamdunk.matchquest.dungeon.objects.Hero;
import com.slamdunk.matchquest.dungeon.objects.ObjectType;
import com.slamdunk.matchquest.dungeon.objects.Stance;


/**
 * Un objet qui cause des d�g�ts � tous les ennemis � port�e une fois l'animation
 * termin�e.
 */
public class SimpleBow extends DungeonObject {
	private final static int MIN_RANGE = 0;
	private final static int MAX_RANGE = 400;
	
	private Hero hero;
	private ProjectileArrow arrow;
	
	/**
	 * 
	 * @param hero2 
	 * @param hero
	 * @param damage
	 * @param minRange Relatif � la droite du h�ros
	 * @param maxRange Relatif � la droite du h�ros
	 */
	public SimpleBow(Hero hero) {
		super(ObjectType.ATTACK_DIST);
		
		// Ajoute les animations possibles de cet objet
	    loadAnimation(Stance.ATTACKING.name(), "clips/simpleBowAttack.clip");
	    
	    // D�finit les propri�t�s de l'objet
	    this.hero = hero;
 		setStance(Stance.IDLE);
	}
	
	public void setHero(Hero hero) {
		this.hero = hero;
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
	}
	
	@Override
	public void attack() {
		// Cr�e la fl�che
		if (arrow == null) {
			arrow = new ProjectileArrow(hero, hero.getRight() + MIN_RANGE, hero.getRight() + MAX_RANGE);
			linkSprite("bow-string", arrow);
			arrow.setWorld(hero.getWorld());
			hero.getWorld().addVisualEffect(arrow);
		}
		
		// Une fois l'animation de l'arc achev�e, on envoie la fl�che
		if (isAnimationFinished()) {
			// D�tache la fl�che de la corde
			unlinkSprite("bow-string", arrow);
			// Passe le projectile en MOVING pour qu'il se dirige vers le maxRange
			arrow.setStance(Stance.MOVING);
			arrow.setDestination(hero.getRight() + MAX_RANGE - arrow.getWidth());
			// Cette fl�che n'appartient plus � l'arc
			arrow = null;
			
			// L'action a �t� effectu�e
			setActionDone(true);
			setStance(Stance.IDLE);
			
			if (getParentLinkSprite() != null) {
				getParentLinkSprite().unlinkSprite("hand", this);
			}
			getWorld().removeVisualEffect(this);
		}
	}
	
	@Override
	public void onAnimationFinished() {
		setAnimationFinished(true);
	}
}
