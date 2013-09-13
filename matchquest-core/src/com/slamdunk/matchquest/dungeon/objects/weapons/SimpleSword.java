package com.slamdunk.matchquest.dungeon.objects.weapons;

import com.slamdunk.matchquest.MatchQuest;
import com.slamdunk.matchquest.actions.StandardActions;
import com.slamdunk.matchquest.dungeon.objects.DungeonObject;
import com.slamdunk.matchquest.dungeon.objects.ObjectType;
import com.slamdunk.matchquest.dungeon.objects.Stance;

/**
 * Un objet qui cause des d�g�ts � tous les ennemis � port�e une fois l'animation
 * termin�e.
 */
public class SimpleSword extends DungeonObject {
	public static final float RANGE = 1f;
	
	public SimpleSword() {
		super(ObjectType.ATTACK_MELEE);
		
		// Ajoute les animations possibles de cet objet
	    loadAnimation(Stance.ATTACKING.name(), "clips/simpleSwordAttack.clip");
		
		// D�finit les propri�t�s de l'objet
		setStance(Stance.IDLE);
	}
	
	@Override
	public void attack() {
		if (isAnimationFinished()) {
			// Fait une simple attaque correspondant � l'ATT du joueur, 
			// avec un bonus pour chaque item align� d�passant 3
			StandardActions.attack(
				MatchQuest.getInstance().getPlayer().getAttack(),
				getX(), getX() + RANGE,
				null);
			
			// L'action a �t� effectu�e
			setActionDone(true);
			setStance(Stance.IDLE);
			
			if (getParentLinkSprite() != null) {
				getParentLinkSprite().unlinkSprite("hand", this);
			}
			getWorld().removeVisualEffect(this);
		}
	}
}
