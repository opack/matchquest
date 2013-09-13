package com.slamdunk.utils.animatedsprite;

import com.slamdunk.utils.PropertiesEx;

/**
 * Code pouvant être exécuté lorsqu'une Frame d'un AnimatedSprite se présente
 */
public interface FrameRunnable extends Runnable {
	/**
	 * Charge les informations nécessaires à l'exécution de ce runnable
	 * à partir du fichier de propriété indiqué, et sous la clé indiquée
	 * @param properties
	 * @param runnableKey Clé de la racine de ce runnable, SANS point
	 * final. Ex : frame1.runnable0
	 */
	void load(PropertiesEx properties, String runnableKey);
}
