package com.slamdunk.matchquest.actions.loot;

import com.slamdunk.matchquest.Assets;
import com.slamdunk.matchquest.actions.HeroAction;
import com.slamdunk.matchquest.actions.StandardActions;
import com.slamdunk.matchquest.dungeon.puzzle.AlignmentOrientation;
import com.slamdunk.matchquest.dungeon.puzzle.Puzzle;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleAttributes;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleLogic.AttributeData;
import com.slamdunk.utils.Point;

public class ActionSimpleLoot extends HeroAction {
	
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
		StandardActions.collectCoins(1 + (nbAlignedItems - 3), Assets.actionSimpleLootSound);
	}

	@Override
	protected void performSuperAction(Puzzle puzzle, Point position, AlignmentOrientation orientation) {
		// TODO Auto-generated method stub
		
	}
}
