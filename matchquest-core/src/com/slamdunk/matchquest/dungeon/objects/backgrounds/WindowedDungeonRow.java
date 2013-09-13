package com.slamdunk.matchquest.dungeon.objects.backgrounds;

import com.slamdunk.matchquest.dungeon.DungeonRenderer;
import com.slamdunk.matchquest.dungeon.objects.DungeonObject;
import com.slamdunk.matchquest.dungeon.objects.ObjectType;
import com.slamdunk.matchquest.dungeon.objects.Stance;

public class WindowedDungeonRow extends DungeonObject {
	public static final float WIDTH = 6f;
	public static final float HEIGHT = DungeonRenderer.DISPLAY_HEIGHT; // Taille en unit�s du monde : max affichable
	
	public WindowedDungeonRow(float position) {
		super(ObjectType.BACKGROUND_DUNGEON_ROW2);
		
	    // Ajoute les animations possibles de cet objet
    	loadAnimation(Stance.IDLE.name(), "clips/background_dungeonrow2-idle.clip");
	    
	    // D�finit les propri�t�s de l'objet
	 	setPosition(position, 0);
	    setStance(Stance.IDLE);
	}
}
