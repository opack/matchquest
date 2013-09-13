package com.slamdunk.matchquest.dungeon.objects.weapons;

import com.slamdunk.matchquest.MatchQuest;
import com.slamdunk.matchquest.actions.StandardActions;
import com.slamdunk.matchquest.dungeon.objects.DungeonObject;
import com.slamdunk.matchquest.dungeon.objects.Hero;
import com.slamdunk.matchquest.dungeon.objects.ObjectType;
import com.slamdunk.matchquest.dungeon.objects.Stance;

/**
 * Un objet qui cause des d�g�ts � tous les ennemis � port�e une fois l'animation
 * termin�e.
 */
public class ProjectileArrow extends DungeonObject {
	public static final float SPEED = 400f; // Vitesse de d�placement : 4m/sec
	
	private float minEffectArea;
	private float maxEffectArea;
	
	/**
	 * 
	 * @param hero
	 * @param minEffectArea Position absolue � partir de laquelle la fl�che peut toucher un ennemi
	 * @param maxEffectArea Position absolue jusqu'� laquelle la fl�che peut toucher un ennemi
	 */
	public ProjectileArrow(Hero hero, float minEffectArea, float maxEffectArea) {
		super(ObjectType.PROJECTILE_ARROW);
		
		// Ajoute les animations possibles de cet objet
	    loadAnimation(Stance.IDLE.name(), "clips/arrow.clip");
	    loadAnimation(Stance.MOVING.name(), "clips/arrow.clip");
		
		// D�finit les propri�t�s de l'objet
		this.minEffectArea = minEffectArea;
		this.maxEffectArea = maxEffectArea;
		setSpeed(SPEED);
		setStance(Stance.IDLE);
	}
	
	@Override
	protected void move(float delta) {
		// D�place la fl�che
		super.move(delta);
		
		// Si on est repass� en idle, c'est que la fl�che n'a trouv� aucune cible
		// On peut la supprimer du monde.
		if (getStance() == Stance.IDLE) {
			getWorld().removeVisualEffect(this);
		}
		
		// Teste si la fl�che touche un ennemi dans la zone
		if (getRight() >= minEffectArea && getRight() <= maxEffectArea
		&& StandardActions.attack(
			MatchQuest.getInstance().getPlayer().getAttack() / 2, 
			getRight(), getRight(), null)) {
			
			// L'action a �t� effectu�e
			setActionDone(true);
			
			// Retire la fl�che du monde
			getWorld().removeVisualEffect(this);
		}
	}
	
	@Override
	public void onAnimationFinished() {
		// Pas d'animation ici
	}
}
