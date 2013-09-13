package com.slamdunk.matchquest.dungeon.objects.potions;

import com.slamdunk.matchquest.actions.StandardActions;
import com.slamdunk.matchquest.dungeon.objects.DungeonObject;
import com.slamdunk.matchquest.dungeon.objects.ObjectType;
import com.slamdunk.matchquest.dungeon.objects.Stance;

public class SimpleHealPotion extends DungeonObject {

	private int healValue;
	
	public SimpleHealPotion(int healValue) {
		super(ObjectType.POTION_HEAL);
		
		// Ajoute les animations possibles de cet objet
	    loadAnimation(Stance.HEALING.name(), "clips/simpleHealingPotion.clip");
	    
	    // D�finit les propri�t�s de l'objet
		this.healValue = healValue;
		setStance(Stance.IDLE);
	}
	
	@Override
	protected void heal() {
		if (isAnimationFinished()) {
			// Quand l'animation est finie, on donne les PV au joueur et on supprime l'effet
			StandardActions.heal(healValue, null);//DBG, Assets.actionSimpleHealSound);
			
			// L'action a �t� effectu�e
			setActionDone(true);
			setStance(Stance.IDLE);
			
			// Retire la fl�che du monde
			if (getParentLinkSprite() != null) {
				getParentLinkSprite().unlinkSprite("body", this);
			}
			getWorld().removeVisualEffect(this);
		}
	}
	

	@Override
	public void onAnimationFinished() {
		setAnimationFinished(true);
	}
}
