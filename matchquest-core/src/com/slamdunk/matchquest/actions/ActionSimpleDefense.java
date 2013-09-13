package com.slamdunk.matchquest.actions;

import com.slamdunk.matchquest.Assets;
import com.slamdunk.matchquest.dungeon.puzzle.AlignmentOrientation;
import com.slamdunk.matchquest.dungeon.puzzle.Puzzle;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleAttributes;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleLogic.AttributeData;
import com.slamdunk.utils.Point;

public class ActionSimpleDefense extends HeroAction {
	
	public PuzzleAttributes getAttribute() {
		return PuzzleAttributes.DEFEND;
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
		// Déclencher l'action
		StandardActions.defend(2 + (nbAlignedItems - 3), Assets.actionSimpleDefenseSound);
	}

	@Override
	protected void performSuperAction(Puzzle puzzle, Point position, AlignmentOrientation orientation) {
		// TODO Auto-generated method stub
		
	}
}
