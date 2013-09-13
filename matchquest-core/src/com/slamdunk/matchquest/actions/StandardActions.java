package com.slamdunk.matchquest.actions;

import com.badlogic.gdx.audio.Sound;
import com.slamdunk.matchquest.Assets;
import com.slamdunk.matchquest.MatchQuest;
import com.slamdunk.matchquest.dungeon.DungeonWorld;
import com.slamdunk.matchquest.dungeon.objects.Hero;
import com.slamdunk.matchquest.dungeon.objects.Mob;
import com.slamdunk.matchquest.dungeon.objects.Stance;

/**
 * Met des comportements standards à disposition des classes Action
 */
public class StandardActions {
	/**
	 * Action d'attaque simple qui endommage 1 ennemi (le premier rencontré) et joue éventuellement un son.
	 */
	public static boolean attack(int damage, float minEffectArea, float maxEffectArea, Sound sound) {
		DungeonWorld world = MatchQuest.getInstance().getScreen().getWorld();
		
		for (Mob mob : world.getMobs()) {
			if (mob != null
			&& !mob.isDead()
			&& mob.getX() <= maxEffectArea
			&& minEffectArea <= mob.getRight()) {
				// Bim !
				mob.receiveHit(damage);
				
				// Mouru ?
				if (mob.isDead()) {
					mob.setStance(Stance.DYING);
				}
				// On ne frappe que le premier ennemi
				return true;
			}
		}
		
		// Petit son
		if (sound != null) {
			Assets.playSound(sound);
		}
		
		// Personne n'a été touché
		return false;
	}
	
	/**
	 * Action d'attaque simple qui endommage TOUS les ennemis dans la zone
	 * et joue éventuellement un son.
	 */
	public static boolean attackAll(int damage, float minRange, float maxRange, Sound sound) {
		DungeonWorld world = MatchQuest.getInstance().getScreen().getWorld();
		Hero hero = world.getHero();
		final float heroPos = hero.getX();
		final float minAttackArea = heroPos + minRange;
		final float maxAttackArea = heroPos + maxRange;

		boolean hit = false;
		for (Mob mob : world.getMobs()) {
			if (mob != null
			&& !mob.isDead()
			&& mob.getX() >= minAttackArea
			&& mob.getX() <= maxAttackArea) {
				// Petit son
				if (sound != null) {
					Assets.playSound(sound);
				}
				
				// Bim !
				hero.setStance(Stance.ATTACKING);
				mob.receiveHit(damage);
				
				// Mouru ?
				if (mob.isDead()) {
					mob.setStance(Stance.DYING);
				}
				hit = true;
			}
		}
		return hit;
	}
	
	/**
	 * Action de collecte de pièces qui ajoute des pièces et joue éventuellement un son.
	 */
	public static void collectCoins(int coins, Sound sound) {
		// Ajout de pièces
		MatchQuest.getInstance().getPlayer().addCoins(coins);
		
		// Petit son
		if (sound != null) {
			Assets.playSound(sound);
		}
	}
	
	/**
	 * Action de défense simple qui ajoute des points d'armure et joue éventuellement un son.
	 */
	public static void defend(int armor, Sound sound) {
		// Ajout de points d'armure
		MatchQuest.getInstance().getPlayer().addDefense(armor);
		
		// Petit son
		if (sound != null) {
			Assets.playSound(sound);
		}
	}
	
	/**
	 * Action de guérison  simple qui ajoute des points de santé et joue éventuellement un son.
	 */
	public static void heal(int amount, Sound sound) {
		// Ajout de points de santé
		MatchQuest.getInstance().getPlayer().addHp(amount);
		
		// Petit son
		if (sound != null) {
			Assets.playSound(sound);
		}
	}
}
