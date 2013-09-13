package com.slamdunk.matchquest.messagebox;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.slamdunk.matchquest.Assets;

public class MessageBoxNinePatch extends NinePatch {
	private static MessageBoxNinePatch instance;

	public static MessageBoxNinePatch getInstance() {
		if (instance == null) {
			instance = new MessageBoxNinePatch();
		}
		return instance;
	}

	private MessageBoxNinePatch() {
		super(Assets.ui_msgBox, 8, 8, 8, 8);
	}
}