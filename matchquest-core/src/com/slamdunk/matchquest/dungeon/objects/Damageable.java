package com.slamdunk.matchquest.dungeon.objects;

/**
 * Comportement d'un objet qui peut recevoir des dégâts
 */
public interface Damageable {
	/**
	 * Appelée lorsque l'objet doit mourir. C'est dans cette
	 * méthode que l'objet doit sortir éventuellement du
	 * Stage.
	 */
	void die();
	
	/**
	 * Retourne le nombre de PV restants
	 */
	int getHp();
	
	/**
	 * Indique si l'objet est "mort"
	 */
	boolean isDead();
	
	/**
	 * Appelée lorsque l'objet reçoit des dégâts
	 */
	void receiveHit(int hit);
}
