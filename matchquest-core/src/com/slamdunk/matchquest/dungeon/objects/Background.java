package com.slamdunk.matchquest.dungeon.objects;

import com.slamdunk.matchquest.dungeon.DungeonRenderer;

public class Background extends DungeonObject {
	public static final float WIDTH = 6f;
	public static final float HEIGHT = DungeonRenderer.DISPLAY_HEIGHT; // Taille en unités du monde : max affichable
	
	public Background(ObjectType type, float position) {
		super(type);
		
	    // Ajoute les animations possibles de cet objet
	    if (type == ObjectType.BACKGROUND_DUNGEON_ROW1) {
	    	loadAnimation(Stance.IDLE.name(), "clips/background_dungeonrow1-idle.clip");
	    } else {
	    	loadAnimation(Stance.IDLE.name(), "clips/background_dungeonrow2-idle.clip");
	    }
	    
	    // Définit les propriétés de l'objet
	 	setPosition(position, 0);
	 	//DBGsetSize(WIDTH, HEIGHT);
	    setStance(Stance.IDLE);
	}
}
