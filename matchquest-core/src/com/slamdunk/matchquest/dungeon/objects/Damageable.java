package com.slamdunk.matchquest.dungeon.objects;

/**
 * Comportement d'un objet qui peut recevoir des d�g�ts
 */
public interface Damageable {
	/**
	 * Appel�e lorsque l'objet doit mourir. C'est dans cette
	 * m�thode que l'objet doit sortir �ventuellement du
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
	 * Appel�e lorsque l'objet re�oit des d�g�ts
	 */
	void receiveHit(int hit);
}
