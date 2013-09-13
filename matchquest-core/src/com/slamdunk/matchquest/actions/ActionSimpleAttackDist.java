package com.slamdunk.matchquest.actions;

import com.slamdunk.matchquest.MatchQuest;
import com.slamdunk.matchquest.dungeon.DungeonWorld;
import com.slamdunk.matchquest.dungeon.objects.Hero;
import com.slamdunk.matchquest.dungeon.objects.SimpleBowAttack;
import com.slamdunk.matchquest.dungeon.objects.Stance;
import com.slamdunk.matchquest.dungeon.puzzle.AlignmentOrientation;
import com.slamdunk.matchquest.dungeon.puzzle.Puzzle;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleAttributes;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleLogic.AttributeData;
import com.slamdunk.utils.Point;

public class ActionSimpleAttackDist extends HeroAction {
	public PuzzleAttributes getAttribute() {
		return PuzzleAttributes.LOOT;
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
		SimpleBowAttack distAttack = new SimpleBowAttack(hero);
	    distAttack.setLayer(2);
	    distAttack.setWorld(world);
	    world.addVisualEffect(distAttack);
	    
		// Liaison du héros et de l'objet
		hero.setWeapon(distAttack);
		hero.linkSprite("hand", distAttack);
		
		// Le héros attaque
		distAttack.setStance(Stance.ATTACKING);
		hero.setStance(Stance.ATTACKING);
	}

	@Override
	protected void performSuperAction(Puzzle puzzle, Point position, AlignmentOrientation orientation) {
		// TODO Auto-generated method stub
		
	}
}
