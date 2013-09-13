package com.slamdunk.matchquest.dungeon;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.slamdunk.matchquest.MatchQuest;
import com.slamdunk.matchquest.actions.HeroAction;
import com.slamdunk.matchquest.dungeon.objects.Background;
import com.slamdunk.matchquest.dungeon.objects.DungeonObject;
import com.slamdunk.matchquest.dungeon.objects.Hero;
import com.slamdunk.matchquest.dungeon.objects.Mob;
import com.slamdunk.matchquest.dungeon.objects.ObjectType;
import com.slamdunk.matchquest.dungeon.objects.Stance;
import com.slamdunk.matchquest.dungeon.puzzle.Puzzle;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleListener;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleMatchData;

/**
 * Représentation logique du donjon
 */
public class DungeonWorld implements PuzzleListener {
	private float length;
	private List<Background> grounds;
	private List<Mob> mobs;
	private Hero hero;
	/**
	 * Contenu du donjon avec lequel peut interagir le joueur, donc tout
	 * sauf le background (les objets, les personnages, les obstacles...)
	 */
	private List<DungeonObject> objects;
	private List<DungeonObject> visualEffects;
	/**
	 * Liste de travail utilisée pour permettre la suppression d'objets pendant update()
	 * en évitant les concurrentmodificationexception
	 */
	private List<DungeonObject> tmpObjects;
	/**
	 * Indice dans le tableau d'objets du personnage qui joue actuellement
	 */
	private int curPlayingObjectIdx;
	
	public DungeonWorld(float approximativePreferedLength) {
		grounds = new ArrayList<Background>();
		mobs = new ArrayList<Mob>();
		objects = new ArrayList<DungeonObject>();
		tmpObjects = new ArrayList<DungeonObject>();
		visualEffects = new ArrayList<DungeonObject>();
		
		// Génère un donjon aléatoirement
		generateRandom(approximativePreferedLength);
	}

	/**
	 * Indique si un déplacement est possible vers la position indiquée.
	 * L'algo dit que c'est possible si la position est valide et si
	 * aucun ennemi ne se trouve à cette position.
	 * @param position
	 * @param width l'espace libre qui doit être disponible
	 * @return
	 */
	public boolean canMoveTo(DungeonObject object, float position) {
		if (!isValidPosition(position, object.getWidth())) {
			return false;
		}
		for (Mob mob : mobs) {
			if (mob.overlaps(object)) {
				return false;
			}
		}
		return !hero.overlaps(object);
	}

	private void generateRandom(float approximativePreferedLength) {
		// Génération du héros
		hero = new Hero(0);
		hero.setWorld(this);
		objects.add(hero);
		
		// Génération du sol
		length = 0;
		while (length < approximativePreferedLength) {
			// TODO DBG Changement de l'image pour 1 sol sur 2, histoire de voir qu'on avance
			Background ground;
			if (MathUtils.randomBoolean()) {
				ground = new Background(ObjectType.BACKGROUND_DUNGEON_ROW1, length);
			} else {
				ground = new Background(ObjectType.BACKGROUND_DUNGEON_ROW2, length);
			}
			grounds.add(ground);
			length += ground.getWidth();
		}
		
		// Génération des mobs
		final int nbMobs = MathUtils.random(4, 10);
		float lastMobEnd = hero.getWidth();
		for (int mobCount = 0; mobCount < nbMobs; mobCount++) {
			Mob mob = new Mob(
				MathUtils.random(lastMobEnd, Math.min(lastMobEnd, length)), 
				MathUtils.random(2, 6), 
				MathUtils.random(1, 2));
			mob.setWorld(this);
			mobs.add(mob);
			objects.add(mob);
			
			lastMobEnd = mob.getRight();
			// Dès qu'un mob est généré hors du donjon on s'arrête
			if (lastMobEnd >= length) {
				break;
			}
		}
		
		// Fait jouer le héros
		curPlayingObjectIdx = 0;
	}
	
	public List<Background> getGrounds() {
		return grounds;
	}
	
	public Hero getHero() {
		return hero;
	}
	
	public float getLength() {
		return length;
	}

	public List<Mob> getMobs() {
		return mobs;
	}

	public List<DungeonObject> getObjects() {
		return objects;
	}
	
	public List<DungeonObject> getVisualEffects() {
		return visualEffects;
	}
	
	/**
	 * Une position est valide si l'objet pourra tenir en entier dans le donjon
	 * @param position
	 * @param width
	 * @return
	 */
	public boolean isValidPosition(float position, float width) {
		return position >= 0 && position + width < length;
	}
	
	/**
	 * Retourne la plus grande distance qui peut être parcourue avant de rencontrer un obstacle.
	 * @param movingObject
	 * @param wishedDistance La distance que l'on souhaiterait parcourir. < 0 pour aller vers la 
	 * gauche, > 0 pour aller vers la droite.
	 * @return Distance maximale qui peut être parcourue. Correspond à un offset. Comme pour
	 * wishedDistance, retourne une valeur < 0 pour aller vers la gauche et > 0 pour la droite.
	 * La distance peut donc directement être ajoutée à une position, comme un décalage.
	 */
	public float maxMoveDistance(final DungeonObject movingObject, final float wishedDistance) {
		if (wishedDistance == 0) {
			return 0;
		}
		// Détermine un rectangle indiquant la zone qui souhaite être parcourue
		boolean goesLeft = wishedDistance < 0;
		Rectangle path = new Rectangle(movingObject.getBounds());
		if (goesLeft) {
			// On souhaite aller vers la gauche
			path.x += wishedDistance;
			path.width = -wishedDistance;
		} else {
			// On souhaite aller vers la droite
			path.x += path.width;
			path.width = wishedDistance;
		}
		
		// Pour chaque objet, s'il entre dans cette zone alors c'est que la zone est trop
		// grande. On l'adapte donc suivant le sens de parcours.
		final int movingId = movingObject.getId();
		Rectangle objectBounds;
		for (DungeonObject object : objects) {
			if (object.isBlocking()
			&& object.getId() != movingId
			&& object.getStance() != Stance.DYING) {
				objectBounds = object.getBounds();
				if (objectBounds.overlaps(path)) {
					if (goesLeft) {
						// On va vers la gauche et la course doit être tronquée
						float right = path.x + path.width;
						path.x = objectBounds.x + objectBounds.width;
						path.width = right - path.x;
					} else {
						// On va vers la droite et la course doit être tronquée
						path.width = objectBounds.x - path.x;
					}
				}
			}
		}
		if (goesLeft) {
			return Math.min(0, - path.width);
		} else {
			return Math.max(0, path.width);
		}
	}

	@Override
	public void puzzleSteady() {
		hero.setPuzzleSteady(true);
	}
	
	public void removeMob(Mob mob) {
		mobs.remove(mob);
		objects.remove(mob);
	}
	
	public void addVisualEffect(DungeonObject visualEffect) {
		//objects.add(curPlayingObjectIdx, visualEffect);
		visualEffects.add(visualEffect);
	}
	
	public void removeVisualEffect(DungeonObject visualEffect) {
		//objects.remove(visualEffect);
		visualEffects.remove(visualEffect);
	}

	@Override
	public void switchDone(Puzzle puzzle, PuzzleMatchData matchData) {
		// Choix de l'action à effectuer suivant l'alignement
		HeroAction action = null;
		try {
			action = MatchQuest.getInstance().getPlayer().getAction(puzzle, matchData);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (action == null) {
			return;
		}
		hero.addAction(action);
		
		// Il faut finir le tour du joueur car il a fait un échange
		hero.setPlayerPlayed(true);
		
		// Puisqu'un échange vient d'être fait, le puzzle n'est pas stable
		hero.setPuzzleSteady(false);
	}

	/**
	 * Mise à jour de l'état du monde
	 * @param delta
	 */
	public void update(float delta) {
		// Met à jour les objets en fonction du temps écoulé (attentes, animations...)
		tmpObjects.clear();
		tmpObjects.addAll(grounds);
		tmpObjects.addAll(visualEffects);
		tmpObjects.addAll(objects);
		for (DungeonObject object : tmpObjects) {
			if (object != null) {
				object.act(delta);
			}
		}
		
		// Fait jouer l'acteur (héros ou mob) suivant
		DungeonObject curPlayingCharacter = objects.get(curPlayingObjectIdx);
		if (curPlayingCharacter.isTurnOver()) {
			curPlayingObjectIdx++;
			if (curPlayingObjectIdx >= objects.size() - 1) {
				curPlayingObjectIdx = 0;
			}
			curPlayingCharacter = objects.get(curPlayingObjectIdx);
			curPlayingCharacter.setStance(Stance.THINKING);
		}
	}
}
