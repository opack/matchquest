package com.slamdunk.matchquest;

import java.util.HashMap;
import java.util.Map;

import com.slamdunk.matchquest.actions.HeroAction;
import com.slamdunk.matchquest.actions.attackdist.ActionSimpleAttackDist;
import com.slamdunk.matchquest.actions.attackmelee.ActionSimpleAttackMelee;
import com.slamdunk.matchquest.actions.defend.ActionSimpleDefense;
import com.slamdunk.matchquest.actions.heal.ActionSimpleHeal;
import com.slamdunk.matchquest.actions.loot.ActionSimpleLoot;
import com.slamdunk.matchquest.actions.special.ActionSimpleSpecial;
import com.slamdunk.matchquest.dungeon.puzzle.AttributeTypes;
import com.slamdunk.matchquest.dungeon.puzzle.Puzzle;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleAttributes;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleMatchData;

/**
 * Représentation logique du joueur, utilisable partout dans le jeu
 */
public class Player {
	private int hp;
	private int attack;
	private int defense;
	private int defenseMax;
	private int coins;
	private boolean turnOver;
	private Map<PuzzleAttributes, Class<? extends HeroAction>> actions;

	public Player(int hp, int att, int def, int defenseMax) {
		this.hp = hp;
		this.attack = att;
		this.defense = def;
		this.defenseMax = defenseMax;
		
		// Ajout des actions par défaut
		actions = new HashMap<PuzzleAttributes, Class<? extends HeroAction>>();
		actions.put(PuzzleAttributes.ATTACK_MELEE, ActionSimpleAttackMelee.class);
		actions.put(PuzzleAttributes.ATTACK_DIST, ActionSimpleAttackDist.class);
		actions.put(PuzzleAttributes.DEFEND, ActionSimpleDefense.class);
		actions.put(PuzzleAttributes.HEAL, ActionSimpleHeal.class);
		actions.put(PuzzleAttributes.LOOT, ActionSimpleLoot.class);
		actions.put(PuzzleAttributes.SPECIAL, ActionSimpleSpecial.class);
	}
	
	public void addAttack(int attack) {
		this.attack += attack;
	}

	public void addCoins(int coins) {
		this.coins += coins;
	}


	/**
	 * Cette méthode s'assure que defense ne dépasse pas defenseMax
	 * @param value
	 */
	public void addDefense(int value) {
		this.defense += value;
		if (defense > defenseMax) {
			defense = defenseMax;
		} else if (defense < 0) {
			defense = 0;
		}
	}

	public void addHp(int hp) {
		this.hp += hp;
	}

	public HeroAction getAction(Puzzle puzzle, PuzzleMatchData matchData) throws InstantiationException, IllegalAccessException {
		PuzzleAttributes sourceAttribute = matchData.getSource().attribute;
		if (matchData.getOrientation() != null && sourceAttribute != null) {
			Class<? extends HeroAction> actionClass;
			if (sourceAttribute.getType() == AttributeTypes.BASE) {
				actionClass = actions.get(sourceAttribute);
			} else {
				actionClass = actions.get(sourceAttribute.getBaseAttribute());
			}
			if (actionClass != null) {
				HeroAction action = actionClass.newInstance();
				action.setPuzzle(puzzle);
				action.setMatchData(matchData);
				return action;
			}
		}
		return null;
	}

	public int getAttack() {
		return attack;
	}

	public int getCoins() {
		return coins;
	}

	public int getDefense() {
		return defense;
	}

	public int getDefenseMax() {
		return defenseMax;
	}

	public int getHp() {
		return hp;
	}

	public boolean isDead() {
		return hp <= 0;
	}

	public boolean isTurnOver() {
		return turnOver;
	}
	
	public void receiveHit(int amount) {
		// Si la défense peut tout supporter, alors elle encaisse
		if (defense >= amount) {
			defense -= amount;
		}
		// Sinon, elle chute à 0 et la santé prend le reste
		else {
			hp -= (amount - defense);
			defense = 0;
		}
	}

	public void setAction(PuzzleAttributes attribute, Class<? extends HeroAction> actionClass) {
		actions.put(attribute, actionClass);
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}
	

	/**
	 * Cette méthode permet de définir une valeur de défense supérieure à
	 * defenseMax
	 * @param defense
	 */
	public void setDefense(int defense) {
		this.defense = defense;
		if (this.defense < 0) {
			this.defense = 0;
		}
	}

	public void setDefenseMax(int defenseMax) {
		this.defenseMax = defenseMax;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}

	public void setTurnOver(boolean turnOver) {
		this.turnOver = turnOver;
	}
}
