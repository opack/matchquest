package com.slamdunk.matchquest.core;

import com.badlogic.gdx.Game;
import com.slamdunk.matchquest.core.puzzle.PuzzleScreen;
import com.slamdunk.utils.Config;

public class MatchQuest extends Game {
	/**
	 * Taille de l'affichage en pixels
	 */
	public static int screenWidth;
	public static int screenHeight;
	
	private PuzzleScreen puzzleScreen;
	
	@Override
	public void create () {
		// Chargement de la taille de l'écran
		screenWidth = Config.asInt("screen.width", 480);
		screenHeight = Config.asInt("screen.height", 800);
		
		// Chargement des assets
		Assets.load();
		
		// Arrivée sur la carte du monde
		puzzleScreen = new PuzzleScreen();
		setScreen(puzzleScreen);
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
		// Rechargement des assets
		Assets.load();
	}

	@Override
	public void dispose () {
		disposePuzzleScreen();
		Assets.dispose();
	}

	private void disposePuzzleScreen() {
		if (puzzleScreen != null) {
			puzzleScreen.dispose();
			puzzleScreen = null;
		}
	}
}
