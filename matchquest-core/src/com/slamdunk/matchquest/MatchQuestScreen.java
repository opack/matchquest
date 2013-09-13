package com.slamdunk.matchquest;

import com.badlogic.gdx.Screen;
import com.slamdunk.matchquest.dungeon.DungeonWorld;
import com.slamdunk.matchquest.dungeon.puzzle.Puzzle;

/**
 * Un écran du jeu
 */
public interface MatchQuestScreen extends Screen {
	/**
	 * Retourne le puzzle actuel
	 */
	public Puzzle getPuzzle();
	
	/**
	 * Retourne le monde actuel
	 */
	public DungeonWorld getWorld();
	
	/**
	 * Affiche un message
	 * @return
	 */
	public void showMessage(String message);
}
