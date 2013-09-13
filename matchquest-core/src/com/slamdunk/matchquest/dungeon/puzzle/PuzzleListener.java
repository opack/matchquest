package com.slamdunk.matchquest.dungeon.puzzle;

public interface PuzzleListener {

	/**
	 * M�thode appel�e lorsque le puzzle est stable
	 */
	void puzzleSteady();

	/**
	 * M�thode appel�e lorsqu'un �change d'items a �t� valid�
	 */
	void switchDone(Puzzle puzzle, PuzzleMatchData matchData);

}
