package com.slamdunk.matchquest.actions;

import com.slamdunk.matchquest.MatchQuest;
import com.slamdunk.matchquest.dungeon.DungeonWorld;
import com.slamdunk.matchquest.dungeon.objects.Hero;
import com.slamdunk.matchquest.dungeon.objects.SimpleHealPotion;
import com.slamdunk.matchquest.dungeon.objects.Stance;
import com.slamdunk.matchquest.dungeon.puzzle.AlignmentOrientation;
import com.slamdunk.matchquest.dungeon.puzzle.Puzzle;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleAttributes;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleLogic.AttributeData;
import com.slamdunk.utils.Point;

public class ActionSimpleHeal extends HeroAction {
	public PuzzleAttributes getAttribute() {
		return PuzzleAttributes.HEAL;
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
		
		// Création de la potion
		SimpleHealPotion potion = new SimpleHealPotion(2 + (nbAlignedItems - 3));
		potion.setStance(Stance.IDLE);
		potion.setWorld(world);
		world.addVisualEffect(potion);
		
		// Liaison du héros et de l'objet
		hero.setPotion(potion);
		hero.linkSprite("body", potion);
		
		// Le héros se soigne
		potion.setStance(Stance.HEALING);
		hero.setStance(Stance.HEALING);
	}

	@Override
	protected void performSuperAction(Puzzle puzzle, Point position, AlignmentOrientation orientation) {
		// TODO Auto-generated method stub
		
	}
}
