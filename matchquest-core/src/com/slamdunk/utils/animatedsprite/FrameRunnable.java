package com.slamdunk.utils.animatedsprite;

import com.slamdunk.utils.PropertiesEx;

/**
 * Code pouvant �tre ex�cut� lorsqu'une Frame d'un AnimatedSprite se pr�sente
 */
public interface FrameRunnable extends Runnable {
	/**
	 * Charge les informations n�cessaires � l'ex�cution de ce runnable
	 * � partir du fichier de propri�t� indiqu�, et sous la cl� indiqu�e
	 * @param properties
	 * @param runnableKey Cl� de la racine de ce runnable, SANS point
	 * final. Ex : frame1.runnable0
	 */
	void load(PropertiesEx properties, String runnableKey);
}
