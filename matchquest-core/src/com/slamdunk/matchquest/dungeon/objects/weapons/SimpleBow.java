package com.slamdunk.matchquest.dungeon.objects.weapons;

import com.slamdunk.matchquest.dungeon.objects.DungeonObject;
import com.slamdunk.matchquest.dungeon.objects.Hero;
import com.slamdunk.matchquest.dungeon.objects.ObjectType;
import com.slamdunk.matchquest.dungeon.objects.Stance;


/**
 * Un objet qui cause des dégâts à tous les ennemis à portée une fois l'animation
 * terminée.
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
	 * @param minRange Relatif à la droite du héros
	 * @param maxRange Relatif à la droite du héros
	 */
	public SimpleBow(Hero hero) {
		super(ObjectType.ATTACK_DIST);
		
		// Ajoute les animations possibles de cet objet
	    loadAnimation(Stance.ATTACKING.name(), "clips/simpleBowAttack.clip");
	    
	    // Définit les propriétés de l'objet
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
		// Crée la flèche
		if (arrow == null) {
			arrow = new ProjectileArrow(hero, hero.getRight() + MIN_RANGE, hero.getRight() + MAX_RANGE);
			linkSprite("bow-string", arrow);
			arrow.setWorld(hero.getWorld());
			hero.getWorld().addVisualEffect(arrow);
		}
		
		// Une fois l'animation de l'arc achevée, on envoie la flèche
		if (isAnimationFinished()) {
			// Détache la flèche de la corde
			unlinkSprite("bow-string", arrow);
			// Passe le projectile en MOVING pour qu'il se dirige vers le maxRange
			arrow.setStance(Stance.MOVING);
			arrow.setDestination(hero.getRight() + MAX_RANGE - arrow.getWidth());
			// Cette flèche n'appartient plus à l'arc
			arrow = null;
			
			// L'action a été effectuée
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
