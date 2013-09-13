package com.slamdunk.matchquest.dungeon.objects.weapons;

import com.slamdunk.matchquest.MatchQuest;
import com.slamdunk.matchquest.actions.StandardActions;
import com.slamdunk.matchquest.dungeon.objects.DungeonObject;
import com.slamdunk.matchquest.dungeon.objects.ObjectType;
import com.slamdunk.matchquest.dungeon.objects.Stance;

/**
 * Un objet qui cause des dégâts à tous les ennemis à portée une fois l'animation
 * terminée.
 */
public class SimpleSword extends DungeonObject {
	public static final float RANGE = 1f;
	
	public SimpleSword() {
		super(ObjectType.ATTACK_MELEE);
		
		// Ajoute les animations possibles de cet objet
	    loadAnimation(Stance.ATTACKING.name(), "clips/simpleSwordAttack.clip");
		
		// Définit les propriétés de l'objet
		setStance(Stance.IDLE);
	}
	
	@Override
	public void attack() {
		if (isAnimationFinished()) {
			// Fait une simple attaque correspondant à l'ATT du joueur, 
			// avec un bonus pour chaque item aligné dépassant 3
			StandardActions.attack(
				MatchQuest.getInstance().getPlayer().getAttack(),
				getX(), getX() + RANGE,
				null);
			
			// L'action a été effectuée
			setActionDone(true);
			setStance(Stance.IDLE);
			
			if (getParentLinkSprite() != null) {
				getParentLinkSprite().unlinkSprite("hand", this);
			}
			getWorld().removeVisualEffect(this);
		}
	}
}
