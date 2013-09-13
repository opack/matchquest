package com.slamdunk.matchquest.dungeon;

import static com.slamdunk.matchquest.MatchQuest.screenHeight;
import static com.slamdunk.matchquest.MatchQuest.screenWidth;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.slamdunk.matchquest.Assets;
import com.slamdunk.matchquest.HUD;
import com.slamdunk.matchquest.MatchQuestScreen;
import com.slamdunk.matchquest.dungeon.puzzle.Puzzle;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleSwitchInputProcessor;
import com.slamdunk.matchquest.messagebox.MessageBox;
import com.slamdunk.matchquest.messagebox.MessageBoxFactory;
import com.slamdunk.utils.Config;

public class DungeonScreen implements MatchQuestScreen {
	private Dungeon dungeon;
	private Puzzle puzzle;
	private HUD hud;
	private Stage stage;
	private Table layout;
	
	public DungeonScreen() {
		layout = new Table();
		// Création du stage contenant tout les éléments
		initStage();
		// Donjon
		initDungeon();
		// Puzzle
		initPuzzle();
		// Statistiques et du sac à dos
		initHud();
		// Gestion des inputs
		initInput();
		// Layout de l'écran
		initLayout();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Puzzle getPuzzle() {
		return puzzle;
	}

	@Override
	public DungeonWorld getWorld() {
		return dungeon.getWorld();
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
	
	private void initDungeon() {
		dungeon = new Dungeon(800);
	}
	
	public void initHud() {
		hud = new HUD();
	}

	public void initInput() {
 		PuzzleSwitchInputProcessor switchProcessor = new PuzzleSwitchInputProcessor(puzzle);
 		InputMultiplexer inputMultiplexer = new InputMultiplexer(switchProcessor, stage);
 		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	private void initLayout() {
		stage.clear();
		layout.clear();
		
		// Espace pour le dungeon
		layout.add(dungeon).expand().colspan(3).fill();
		// Ajout du Hud
		layout.row();
		layout.add().expandX();
		layout.add(hud);
		layout.add().expandX();
		// Ajout du puzzle
		layout.row().padBottom(25);
		layout.add().expandX();
		layout.add(puzzle);
		layout.add().expandX();
		// Ajout au stage
		layout.setFillParent(true);
		layout.pack();
		stage.addActor(layout);
	}

	public void initPuzzle() {
		puzzle = new Puzzle(dungeon.getWorld(), Config.asInt("puzzle.width", 9), Config.asInt("puzzle.height", 9));
		puzzle.init(stage);
	}

	private void initStage() {
		// Création de la caméra
		OrthographicCamera camera = new OrthographicCamera();
 		camera.setToOrtho(false, screenWidth, screenHeight);
 		camera.update();
 		
 		// Création du stage contenant tous les acteurs
 		stage = new Stage();
 		stage.setCamera(camera);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// Efface l'écran
//		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// Dessine
		dungeon.render(delta);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// Lancement de la musique
		Assets.playMusic(Assets.dungeonMusics[MathUtils.random(Assets.dungeonMusics.length - 1)]);
	}

	@Override
	public void showMessage(String message) {
		MessageBox msg = MessageBoxFactory.createSimpleMessage(message, stage);
		msg.setVisible(true);
	}
}
