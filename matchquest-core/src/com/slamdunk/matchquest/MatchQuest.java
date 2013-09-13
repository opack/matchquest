package com.slamdunk.matchquest;

import com.badlogic.gdx.Game;
import com.slamdunk.matchquest.dungeon.DungeonScreen;
import com.slamdunk.utils.Config;

public class MatchQuest extends Game {
	private static MatchQuest instance;
	
	/**
	 * Taille de l'affichage en pixels
	 */
	public static int screenWidth;
	public static int screenHeight;
	
	public static MatchQuest getInstance() {
		return instance;
	}
	private Player player;
	
	private DungeonScreen puzzleScreen;

	@Override
	public void create () {
		// Chargement de la taille de l'écran
		screenWidth = Config.asInt("screen.width", 480);
		screenHeight = Config.asInt("screen.height", 800);
		
		// Chargement des assets
		Assets.load();
		
		// Création du joueur
		player = new Player(30, 2, 0, 3);
		
		// Arrivée sur la carte du monde
		puzzleScreen = new DungeonScreen();
		setScreen(puzzleScreen);
		
		instance = this;
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

	public Player getPlayer() {
		return player;
	}
	
	@Override
	public MatchQuestScreen getScreen() {
		return (MatchQuestScreen)super.getScreen();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
