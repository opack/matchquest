package com.slamdunk.matchquest.dungeon.puzzle;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Scaling;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleSwitchInputProcessor.SwitchListener;
import com.slamdunk.utils.Clip;
import com.slamdunk.utils.Config;
import com.slamdunk.utils.GroupEx;

/**
 * G�re l'UI (affichage, d�placement des items...) d'un puzzle
 */
public class Puzzle extends GroupEx implements SwitchListener {
	private static final float SWITCH_DURATION = Config.asFloat("puzzle.switchDuration", 0.2f);
	private static final float FALL_DURATION = Config.asFloat("puzzle.fallDuration", 0.2f);
	private static final float REMOVE_DURATION = Config.asFloat("puzzle.removeDuration", 0.2f);
	
	private int puzzleWidth;
	private int puzzleHeight;
	private PuzzleLogic puzzleLogic;
	private PuzzleImage[][]puzzleImages;
	
	/**
	 * Indique si le stage est dans un �tat stable. Si false, c'est qu'il y a une
	 * animation en cours (apparition d'un attribut, chute ou switch d'un attribut...)
	 */
	private boolean isFallRequested;
	private boolean isMatchRequested;
	/**
	 * Indique si le puzzle �tait d�j� stable. Cela permet de d�terminer s'il vient
	 * de se stabiliser pour n'avertir qu'une seule fois les listeners
	 */
	private boolean wasAlreadySteady;
	
	/**
	 * Indique si un switch � l'initiative de l'utilisateur est en cours
	 */
	private boolean isUserSwitching;
	private int[] switchingPos;

	private float puzzleRowHeight;
	private float puzzleColWidth;

	private final int puzzleItemWidth;
	private final int puzzleItemHeight;
	private final int puzzleItemPadding;
	
	/**
	 * Liste des effets visuels � dessiner
	 */
	private List<Clip> visualEffects;
	private List<Clip> finishedClips;
	
//	// DBG Test pour trajectoire
//	private Texture targetTexture;
//	private Controller controller;
//	private ControllerLogic controllerLogic;
	
	private PuzzleListener listener;
	
	public Puzzle(PuzzleListener listener, int puzzleWidth, int puzzleHeight) {
		// D�finition du puzzle
		this.puzzleWidth = puzzleWidth;
		this.puzzleHeight = puzzleHeight;
		puzzleImages = new PuzzleImage[puzzleWidth][puzzleHeight];
		puzzleLogic = new PuzzleLogic(this);
		puzzleLogic.setListener(listener);
		this.listener = listener;
		
		// Propri�t�s n�cessaires au dessin du puzzle
		puzzleItemWidth = Config.asInt("puzzle.item.width", 48);
		puzzleItemHeight = Config.asInt("puzzle.item.height", 48);
		puzzleItemPadding = Config.asInt("puzzle.item.padding", 48);
		
		// Cr�ation de la liste des clips en cours d'affichage
		visualEffects = new ArrayList<Clip>();
		finishedClips = new ArrayList<Clip>();
		
 		//
 		isUserSwitching = false;
 		switchingPos = new int[4];
	}
	
	@Override
	public void act(float delta) {
		// Met � jour les acteurs
		super.act(delta);
		
		// Met � jour le puzzle
		updatePuzzle(delta);
		
		// Met � jour les effets visuels
		updateVisualEffects(delta);
		
//		// DBG Test pour trajectoire
//		controllerLogic.update(delta);
	}

	/**
	 * V�rifie si tous les acteurs ont achev� leur action
	 */
	public boolean checkSteady() {
		// V�rifie si au moins un acteur du stage est en action
		for (Actor actor : getChildren()) {
			if (actor.getActions().size > 0) {
				return false;
			}
		}
		// V�rifie si au moins un acteur de la table est en action
		for (int x = 0; x < puzzleWidth; x ++) {
			for (int y = 0; y < puzzleHeight; y ++) {	
				if (puzzleImages[x][y].getActions().size > 0) {
					return false;
				}
			}
		}
		
		// V�rifie si au moins un effet visuel est en cours d'animation
		for (Clip clip : visualEffects) {
			if (!clip.isFinished()) {
				return false;
			}
		}
		return true;
	}

	public void createAttribute(int x, int y, final PuzzleAttributes attribute) {
		if (attribute == null) {
			return;
		}
		final PuzzleImage image = puzzleImages[x][y];
		
		// Affectation de l'attribut, et donc de l'image
		image.addAction(new Action() {
			@Override
			public boolean act(float delta) {
				// Affecte l'image
				image.setAttribute(attribute);
				
				// Cache l'image, pour la faire appara�tre avec un joli alpha
				image.getColor().a = 0;
				return true;
			}
		});
		
		// Jolie animation
		image.addAction(Actions.alpha(1, 0.2f, Interpolation.exp5));
	}

	/**
	 * Cr�e un PuzzleImage et le fait descendre vers sa destination. Une fois arriv�,
	 * il assigne son attribut
	 * @param x
	 * @param yFall
	 * @param yEmpty
	 */
	public void createFallAnimation(final int x, final int yFall, final int yEmpty) {
		final PuzzleImage fallingImage;
		if (yFall < puzzleHeight) {
			fallingImage = puzzleImages[x][yFall];
			// L'image �tant tomb�e, elle laisse un vide derri�re elle
			puzzleImages[x][yFall] = createPuzzleImage(fallingImage, false);
			puzzleImages[x][yFall].setAttribute(PuzzleAttributes.EMPTY);
		} else {
			// Cr�ation de l'image avec un attribut au hasard
			fallingImage = createPuzzleImage(x, yFall, null, true);
			fallingImage.setPosition(x * puzzleColWidth, yFall * puzzleRowHeight);
		}
		
		// Animation de l'image vers la position � combler
		final PuzzleImage emptyImage = puzzleImages[x][yEmpty];
		fallingImage.addAction(Actions.sequence(
			Actions.moveTo(emptyImage.getX(), emptyImage.getY(), FALL_DURATION * (yFall - yEmpty)),
			new Action() {
				@Override
				public boolean act(float arg0) {
					// Suppression de l'image vide
					emptyImage.remove();
					
					// Assignation de l'attribut tomb� � l'image destination
					puzzleImages[x][yEmpty] = fallingImage;
					fallingImage.setPuzzleXY(x, yEmpty);
					return true;
				}
			})
		);
		addActor(fallingImage);
	}
	
	/**
	 * Cr�e un nouveau PuzzleImage � partir d'un attribut.
	 * @param x
	 * @param y
	 * @param attribute Si null, un attribut de base est choisit al�atoirement
	 * @param shouldAnimateApperance Si true, l'image appara�t progressivement
	 * @return
	 */
	private PuzzleImage createPuzzleImage(int x, int y,	PuzzleAttributes attribute, boolean shouldAnimateApperance) {
		// Si aucun attribut n'a �t� sp�cifi�, on en choisit un al�atoirement
		if (attribute == null) {
			attribute = PuzzleAttributesHelper.getRandomBaseAttribute();
		}
		
		PuzzleImage image = new PuzzleImage(attribute);
		
		image.setPuzzleX(x);
		image.setPuzzleY(y);
		
		image.setScaling(Scaling.fit);
		image.setSize(puzzleItemWidth, puzzleItemHeight);
		
		// Animation d'apparition
		if (shouldAnimateApperance) {
			image.getColor().a = 0;
			image.addAction(Actions.alpha(1, 0.2f, Interpolation.exp5));
		}
		return image;
	}
	
	/**
	 * Cr�e un nouveau PuzzleImage en en copiant un autre. La position et la taille sont
	 * �galement copi�s.
	 * @param model
	 * @param shouldEmptyModel
	 * @return
	 */
	private PuzzleImage createPuzzleImage(PuzzleImage model, boolean shouldEmptyModel) {
		PuzzleImage image = new PuzzleImage(model.getAttribute());
		
		image.setPuzzleX(model.getPuzzleX());
		image.setPuzzleY(model.getPuzzleY());
		image.setX(model.getX());
		image.setY(model.getY());
		
		image.setWidth(model.getWidth());
		image.setHeight(model.getHeight());
		image.setScaling(Scaling.fit);
		
		if (shouldEmptyModel) {
			model.setAttribute(PuzzleAttributes.EMPTY);
		}
		return image;
	}

//	// DBG Test pour trajectoire
//	private void iniDbg() {
//		targetTexture = new Texture(Gdx.files.internal("dbg/white-circle.png"));
//		targetTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//		Sprite trajectorySprite = new Sprite(targetTexture);
//
//		controller = new Controller();
//		controllerLogic = new ControllerLogic(controller, new Vector2(128f, 650));
//
//		TrajectoryActor trajectoryActor = new TrajectoryActor(controller, -10f, trajectorySprite);
//
//		trajectoryActor.setX(128f);
//		trajectoryActor.setY(650);
//		trajectoryActor.setWidth(10f);
//		trajectoryActor.setHeight(10f);
//		
//		addActor(trajectoryActor);
//	}
	
	private void createRemoveAnimation(final PuzzleImage image) {
		image.addAction(Actions.sequence(
			Actions.alpha(0, REMOVE_DURATION),
			new Action() {
				@Override
				public boolean act(float delta) {
					image.setAttribute(PuzzleAttributes.EMPTY);
					
					// Reset alpha
					Color color = image.getColor();
					color.a = 1.0f;
					image.setColor(color);
					return true;
				}
			}
		));
	}

	private void createSwitchAnimation(final int firstX, final int firstY, final int secondX, final int secondY) {
		// R�cup�re les 2 images
		final PuzzleImage firstImage = puzzleImages[firstX][firstY];
		final PuzzleImage secondImage = puzzleImages[secondX][secondY];
		
		// Anime l'�change
		firstImage.addAction(Actions.sequence(
			Actions.moveTo(secondImage.getX(), secondImage.getY(), SWITCH_DURATION),
			new Action() {
				@Override
				public boolean act(float delta) {
					puzzleImages[secondX][secondY] = firstImage;
					firstImage.setPuzzleXY(secondX, secondY);
					return true;
				}
			}
		));

		secondImage.addAction(Actions.sequence(
			Actions.moveTo(firstImage.getX(), firstImage.getY(), SWITCH_DURATION),
			new Action() {
				@Override
				public boolean act(float delta) {
					puzzleImages[firstX][firstY] = secondImage;
					secondImage.setPuzzleXY(firstX, firstY);
					return true;
				}
			}
		));
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		drawVisualEffects(batch);
	}

	private void drawVisualEffects(SpriteBatch batch) {
		finishedClips.clear();
		
		for (Clip clip : visualEffects) {
			clip.playCurrentFrame(batch);
			if (clip.isFinished()) {
				finishedClips.add(clip);
			}
		}
		visualEffects.removeAll(finishedClips);
	}
	
	public boolean eatAttribute(int x, int y) {
		PuzzleImage eaten = puzzleImages[x][y];
		if (!eaten.isEatable()) {
			return false;
		}
		removeAttribute(x, y);
		return true;
	}

	/**
	 * V�rifie qu'il y a toujours au moins un mouvement possible, et 
	 * cr�e un autre puzzle le cas �ch�ant
	 */
	private void ensureSwitchPossible() {
		// V�rifie qu'il y a toujours au moins un mouvement possible
		if (!puzzleLogic.isSwitchPossible()) {
			// Aucun mouvement n'est possible : suppression de tous les attributs
			init(getStage());
		}
	}
	
	public void fall() {
		isFallRequested = true;
	}

	public PuzzleAttributes getAttribute(int x, int y) {
		return puzzleImages[x][y].getAttribute();
	}
	
	public int getPuzzleHeight() {
		return puzzleHeight;
	}
	
	public PuzzleImage[][] getPuzzleImages() {
		return puzzleImages;
	}
	
	public int getPuzzleWidth() {
		return puzzleWidth;
	}

	/**
	 * Cr�e les acteurs du stage repr�sentant le puzzle
	 */
	public void init(Stage stage) {
		// D�finit le stage actuel pour que les images cr��es dans ce group
		// le connaissent
		setStage(stage);
		
		// Cr�ation des images qui remplissent la table
		PuzzleImage cur;
		PuzzleAttributes attribute = PuzzleAttributes.EMPTY;
		PuzzleImage image;
//		String[] puzzleLine;
		for (int y = puzzleHeight - 1; y > -1; y --) {
			// Chargement �ventuel d'un puzzle dans le properties
//			puzzleLine = Config.asString("puzzle.line" + y, "EMPTY").split("\\s+");
			
			for (int x = 0; x < puzzleWidth; x ++) {
				cur = puzzleImages[x][y];
				// Cr�ation de l'attribut
				attribute = puzzleLogic.initAttribute(x, y);
//				attribute = PuzzleAttributes.valueOf(puzzleLine[x]);
				
				// Cr�ation d'une image
				image = createPuzzleImage(x, y, attribute, false);

				// Ajout de l'image au stage en supprimant l'ancienne
				if (cur != null) {
					removeAttribute(x, y);
				}
				image.setPosition(x * (puzzleItemWidth + puzzleItemPadding), y * (puzzleItemHeight + puzzleItemPadding));
				addActor(image);
				puzzleImages[x][y] = image;
			}
		}
		
		// Stockage des positions des images pour faciliter les animations
		puzzleColWidth = puzzleItemWidth + 2 * puzzleItemPadding;
		puzzleRowHeight = puzzleItemHeight + 2 * puzzleItemPadding;
		
		// On s'assure qu'au moins un switch est possible.
		ensureSwitchPossible();
	}

	public boolean isValidPos(int x, int y) {
		return x >= 0 && x < puzzleWidth
			&& y >= 0 && y < puzzleHeight;
	}

	@Override
	public void onPuzzleSwitch(int firstX, int firstY, int secondX, int secondY) {
		switchAttributes(firstX, firstY, secondX, secondY, true);
	}
	
	public void removeAttribute(int x, int y) {
		PuzzleImage removed = puzzleImages[x][y];
		createRemoveAnimation(removed);
		// L'image vient d'�tre retir�e : elle ne pourra pas �tre mang�e
		// jusqu'� ce qu'un nouvel attribut lui soit affect�
		removed.setEatable(false);
	}
	
	public void setPuzzleHeight(int puzzleHeight) {
		this.puzzleHeight = puzzleHeight;
	}
	
	public void setPuzzleWidth(int puzzleWidth) {
		this.puzzleWidth = puzzleWidth;
	}

	public void switchAttributes(int firstX, int firstY, int secondX, int secondY, boolean isUserSwitching) {
		// Switch requis par l'utilisateur
		this.isUserSwitching = isUserSwitching;
		// Animation et switch effectif
		createSwitchAnimation(firstX, firstY, secondX, secondY);
		// Les positions retenues sont invers�es car les items ont �t� �chang�s 
		switchingPos[0] = secondX;
		switchingPos[1] = secondY;
		switchingPos[2] = firstX;
		switchingPos[3] = firstY;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y=puzzleHeight-1; y > -1; y--) {
			for (int x = 0; x < puzzleWidth; x++) {
				sb.append(puzzleImages[x][y].getAttribute().toString().substring(0, 3)).append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	private void updatePuzzle(float delta) {
		// Tant que le tableau n'est pas stable, on ne le met pas � jour
		if (!checkSteady()) {
			wasAlreadySteady = false;
			return;
		}
		// Si le tableau est stable, alors on regarde s'il faut le mettre � jour
		if (isUserSwitching) {
			if (!puzzleLogic.switchAttributes(switchingPos[0], switchingPos[1], switchingPos[2], switchingPos[3])) {
				// Si le switch a �t� interdit, on replace les �l�ments dans leur ordre original
				createSwitchAnimation(switchingPos[0], switchingPos[1], switchingPos[2], switchingPos[3]);
			} else {
				// Switch de l'utilisateur effectu� !
				ensureSwitchPossible();
			}
			// Reset le flag APRES l'appel � puzzleLogic, car si un MatchEffect est d�clench�
			// il voudra peut-�tre savoir si l'utilisateur est � l'origine du match ou si
			// c'est � cause d'une chute
			isUserSwitching = false;
		} else if (isFallRequested) {
			isFallRequested = false;
			// Il faut faire un match si des attributs sont tomb�s ou qu'on avait d�j� demand� d'en faire un
			isMatchRequested |= puzzleLogic.fall();
		} else if (isMatchRequested) {
			isMatchRequested = false;
			puzzleLogic.match();
		} else if (!wasAlreadySteady){
			// Tableau de nouveau stable et rien d'autre � faire :)
			listener.puzzleSteady();
			wasAlreadySteady = true;
		}
	}

	private void updateVisualEffects(float delta) {
		for (Clip clip : visualEffects) {
			clip.update(delta);
		}
	}
}
