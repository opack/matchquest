package com.slamdunk.matchquest.dungeon.objects;

import java.util.Set;
import java.util.TreeSet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.slamdunk.matchquest.dungeon.DungeonWorld;
import com.slamdunk.utils.animatedsprite.AnimatedSprite;

/**
 * Un item du monde
 */
public class DungeonObject extends AnimatedSprite implements Comparable<DungeonObject>{
	private static int count = 0;
	
	/**
	 * Identifiant unique de l'objet dans le donjon
	 */
	private final int id = count++;
	/**
	 * Donjon dans lequel est l'objet
	 */
	private DungeonWorld world;
	/**
	 * Type de l'objet
	 */
	private ObjectType type;
	/**
	 * Détermine la pose actuelle de l'objet (ce qu'il fait)
	 */
	private Stance stance;
	/**
	 * Le temps qu'il a passé à cet état
	 */
	private float stateTime;
	/**
	 * Temps à patienter avant d'effectuer l'action associée à l'état
	 */
	private float waitTime;
	/**
	 * Indique si l'action associée à la Stance a déjà été
	 * effectuée ou non.
	 */
	private boolean actionDone;
	/**
	 * Indique si l'animation de la stance a été réalisée complètement
	 * au moins une fois
	 */
	private boolean animationFinished;
	/**
	 * Position où souhaite se rendre l'objet
	 */
	private float destination;
	/**
	 * Vitesse de déplacement, en m/s
	 */
	private float speed;
	/**
	 * Indique si l'objet est un obstacle ou s'il peut être traversé
	 */
	private boolean blocking;
	/**
	 * Parent de cet objet, qui le contient donc
	 */
	private DungeonObject parent;
	/**
	 * Liste d'objets contenus dans celui-ci
	 */
	private Set<DungeonObject> objects;
	/**
	 * Indique sur quelle couche se trouve l'objet. 0 identifie la
	 * couche la plus basse.
	 */
	private int layer;
	
	public DungeonObject(ObjectType type) {
		this.type = type;
		setStance(Stance.IDLE);
	}
	
	/**
	 * Met à jour l'objet en fonction de l'action courante et du temps écoulé
	 * @param delta
	 */
	public void act(float delta) {
		updateStateTime(delta);
		
		// Si l'action n'a pas encore été effectuée
		// et qu'on a finit la tempo, on agit
		if (!isActionDone() && getStateTime() >= waitTime) {
			// Choix de l'action
			switch (stance) {
			case ATTACKING:
				attack();
				break;
			case DYING:
				die();
				break;
			case HEALING:
				heal();
				break;
			case IDLE:
				idle();
				break;
			case THINKING:
				think();
				break;
			case MOVING:
				move(delta);
				break;
			default:
				break;
			}
		}
		
		// Fait jouer les objets contenus
		if (objects != null) {
			for (DungeonObject object : objects) {
				object.act(delta);
			}
		}
	}

	public void addObject(DungeonObject object) {
		if (objects == null) {
			objects = new TreeSet<DungeonObject>();
		}
		objects.add(object);
		object.parent = this;
	}
	
	protected void attack() {
		setActionDone(true);
	}

	@Override
	public int compareTo(DungeonObject o) {
		return layer - o.layer;
	}

	protected void die() {
		setActionDone(true);
	}
	
	@Override
	public void draw(SpriteBatch batch, float offsetX, float offsetY) {
		super.draw(batch, offsetX, offsetY);
		
		// Si l'animation est finie, on fait des trucs
		if (isVisible() && getCurrentAnimation().isFinished()) {
			onAnimationFinished();
		}
	}

	public float getDestination() {
		return destination;
	}

	public int getId() {
		return id;
	}

	public int getLayer() {
		return layer;
	}

	public Set<DungeonObject> getObjects() {
		return objects;
	}

	public float getSpeed() {
		return speed;
	}

	public Stance getStance() {
		return stance;
	}

	public float getStateTime() {
		return stateTime;
	}

	public ObjectType getType() {
		return type;
	}
	
	public float getWaitTime() {
		return waitTime;
	}

	public DungeonWorld getWorld() {
		return world;
	}
	
	protected void heal() {
		setActionDone(true);
	}
	
	protected void idle() {
		setActionDone(true);
	}

	public boolean isActionDone() {
		return actionDone;
	}

	public boolean isAnimationFinished() {
		return animationFinished;
	}
	
	public boolean isBlocking() {
		return blocking;
	}

	public boolean isIdle() {
		return stance == Stance.IDLE;
	}

	/**
	 * Indique si le tour de l'objet est fini. Par défaut, le tour
	 * est considéré comme finit si l'objet est oisif (isIdle()== true).
	 * @return
	 */
	public boolean isTurnOver() {
		return isIdle();
	}
	
	protected void move(float delta) {
		float position = getX();
		if (position == destination) {
			setStance(Stance.IDLE);
		} else {
			// Calcule la nouvelle position à occuper
			float newPosition = position;
			if (destination > getX()) {
				// On va vers la droite
				float distance = getWorld().maxMoveDistance(this, speed * delta);
				newPosition = Math.min(position + distance, destination);
			} else {
				// On va vers la gauche
				float distance = getWorld().maxMoveDistance(this, - speed * delta);
				newPosition = Math.max(position + distance, destination);
				
			}
			// Déplace l'objet
			setX(newPosition);
		}
	}
	
	/**
	 * Méthode appelée quand l'animation de la stance actuelle est finie
	 */
	public void onAnimationFinished() {
		setAnimationFinished(true);
		// Si l'animation est finie et que l'action associée
		// est achevée, alors l'objet devient IDLE
		if (isActionDone()) {
			setStance(Stance.IDLE);
		}
	}

	/**
	 * Indique si 2 objets se chevauchent. Retourne false si otherObject
	 * est l'objet courant.
	 * @param otherObject
	 * @return
	 */
	public boolean overlaps(DungeonObject otherObject) {
		return id != otherObject.id 
		&& getBounds().overlaps(otherObject.getBounds());
	}

	public void removeObject(DungeonObject object) {
		if (objects == null) {
			return;
		}
		objects.remove(object);
		object.parent = null;
	}
	
	public void setActionDone(boolean actionDone) {
		this.actionDone = actionDone;
	}

	/**
	 * Si animationFinished==true, l'animation est achevée et on repasse en idle
	 * @param animationFinished
	 */
	public void setAnimationFinished(boolean animationFinished) {
		this.animationFinished = animationFinished;
	}

	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}

	public void setDestination(float destination) {
		this.destination = destination;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setStance(Stance stance) {
		// Chargement d'une nouvelle animation si on change de pose
		// ou s'il n'y a pas d'animation actuellement
		if (stance != this.stance || getCurrentAnimation() == null) {
			// RAZ du statetime si on change de pose
			stateTime = 0;
			// L'action choisie n'a pas encore été effectuée
			actionDone = false;
			// L'animation n'a pas encore été réalisée
			animationFinished = false;
			// Changement du clip actif
			setCurrentAnimation(stance.name());
		}
		this.stance = stance;
		waitTime = 0;
	}

	@Override
	public void setStateTime(float stateTime) {
		super.setStateTime(stateTime);
		this.stateTime = stateTime;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}

	public void setWaitTime(float waitTime) {
		this.waitTime = waitTime;
	}

	public void setWorld(DungeonWorld world) {
		this.world = world;
	}

	protected void think() {
		setActionDone(true);
	}

	@Override
	public void updateStateTime(float delta) {
		super.updateStateTime(delta);
		this.stateTime += delta;
		
		// Met à jour le statetime des sous-objets
		if (objects != null) {
			for (DungeonObject object : objects) {
				object.updateStateTime(delta);
			}
		}
	}
}
