package com.slamdunk.matchquest.dungeon.puzzle;

public interface PuzzleListener {

	/**
	 * Méthode appelée lorsque le puzzle est stable
	 */
	void puzzleSteady();

	/**
	 * Méthode appelée lorsqu'un échange d'items a été validé
	 */
	void switchDone(Puzzle puzzle, PuzzleMatchData matchData);

}
