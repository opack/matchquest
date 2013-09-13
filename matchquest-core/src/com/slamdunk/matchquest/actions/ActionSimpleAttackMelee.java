package com.slamdunk.matchquest.actions;

import com.slamdunk.matchquest.MatchQuest;
import com.slamdunk.matchquest.dungeon.DungeonWorld;
import com.slamdunk.matchquest.dungeon.objects.Hero;
import com.slamdunk.matchquest.dungeon.objects.Stance;
import com.slamdunk.matchquest.dungeon.objects.weapons.SimpleSword;
import com.slamdunk.matchquest.dungeon.puzzle.AlignmentOrientation;
import com.slamdunk.matchquest.dungeon.puzzle.Puzzle;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleAttributes;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleLogic.AttributeData;
import com.slamdunk.utils.Point;

public class ActionSimpleAttackMelee extends HeroAction {
	public PuzzleAttributes getAttribute() {
		return PuzzleAttributes.ATTACK_MELEE;
	}
	
	public boolean hasHyperAction() {
		return true;
	}

	public boolean hasSuperAction() {
		return true;
	}

	@Override
	protected void performComboAction(Puzzle puzzle, AttributeData thisSuper, AttributeData otherSuper, AlignmentOrientation orientation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void performHyperAction(Puzzle puzzle, Point position, AttributeData otherItem, AlignmentOrientation orientation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void performStandardAction(Puzzle puzzle, int nbAlignedItems) {
		DungeonWorld world = MatchQuest.getInstance().getScreen().getWorld();
		Hero hero = world.getHero();
		
		// Création de l'arme
		SimpleSword meleeAttack = new SimpleSword();
	    meleeAttack.setLayer(1);
	    meleeAttack.setWorld(world);
	    world.addVisualEffect(meleeAttack);
	    
		// Liaison du héros et de l'objet
		hero.setWeapon(meleeAttack);
		hero.linkSprite("hand", meleeAttack);
		
		// Le héros attaque
		meleeAttack.setStance(Stance.ATTACKING);
		hero.setStance(Stance.ATTACKING);
	}

	@Override
	protected void performSuperAction(Puzzle puzzle, Point position, AlignmentOrientation orientation) {
		// TODO Auto-generated method stub
		
	}
}
