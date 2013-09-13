package com.slamdunk.matchquest.dungeon.objects.weapons;

import com.slamdunk.matchquest.MatchQuest;
import com.slamdunk.matchquest.actions.StandardActions;
import com.slamdunk.matchquest.dungeon.objects.DungeonObject;
import com.slamdunk.matchquest.dungeon.objects.Hero;
import com.slamdunk.matchquest.dungeon.objects.ObjectType;
import com.slamdunk.matchquest.dungeon.objects.Stance;

/**
 * Un objet qui cause des dégâts à tous les ennemis à portée une fois l'animation
 * terminée.
 */
public class ProjectileArrow extends DungeonObject {
	public static final float SPEED = 400f; // Vitesse de déplacement : 4m/sec
	
	private float minEffectArea;
	private float maxEffectArea;
	
	/**
	 * 
	 * @param hero
	 * @param minEffectArea Position absolue à partir de laquelle la flèche peut toucher un ennemi
	 * @param maxEffectArea Position absolue jusqu'à laquelle la flèche peut toucher un ennemi
	 */
	public ProjectileArrow(Hero hero, float minEffectArea, float maxEffectArea) {
		super(ObjectType.PROJECTILE_ARROW);
		
		// Ajoute les animations possibles de cet objet
	    loadAnimation(Stance.IDLE.name(), "clips/arrow.clip");
	    loadAnimation(Stance.MOVING.name(), "clips/arrow.clip");
		
		// Définit les propriétés de l'objet
		this.minEffectArea = minEffectArea;
		this.maxEffectArea = maxEffectArea;
		setSpeed(SPEED);
		setStance(Stance.IDLE);
	}
	
	@Override
	protected void move(float delta) {
		// Déplace la flèche
		super.move(delta);
		
		// Si on est repassé en idle, c'est que la flèche n'a trouvé aucune cible
		// On peut la supprimer du monde.
		if (getStance() == Stance.IDLE) {
			getWorld().removeVisualEffect(this);
		}
		
		// Teste si la flèche touche un ennemi dans la zone
		if (getRight() >= minEffectArea && getRight() <= maxEffectArea
		&& StandardActions.attack(
			MatchQuest.getInstance().getPlayer().getAttack() / 2, 
			getRight(), getRight(), null)) {
			
			// L'action a été effectuée
			setActionDone(true);
			
			// Retire la flèche du monde
			getWorld().removeVisualEffect(this);
		}
	}
	
	@Override
	public void onAnimationFinished() {
		// Pas d'animation ici
	}
}
