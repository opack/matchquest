package com.slamdunk.matchquest.messagebox;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.slamdunk.matchquest.Assets;

public class CharacterStatsNinePatch extends NinePatch {
	private static CharacterStatsNinePatch instance;

	public static CharacterStatsNinePatch getInstance() {
		if (instance == null) {
			instance = new CharacterStatsNinePatch();
		}
		return instance;
	}

	private CharacterStatsNinePatch() {
		super(Assets.hud_menuskin, 8, 8, 8, 8);
	}
}