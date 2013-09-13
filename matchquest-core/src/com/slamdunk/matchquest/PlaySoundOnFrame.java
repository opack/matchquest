package com.slamdunk.matchquest;

import com.badlogic.gdx.audio.Sound;
import com.slamdunk.utils.PropertiesEx;
import com.slamdunk.utils.animatedsprite.FrameRunnable;

public class PlaySoundOnFrame implements FrameRunnable {
	private Sound sound;

	@Override
	public void load(PropertiesEx properties, String runnableKey) {
		String file = properties.getStringProperty(runnableKey + ".sound", null);
		sound = Assets.getSound(file);
	}
	
	@Override
	public void run() {
		Assets.playSound(sound);
	}
}
