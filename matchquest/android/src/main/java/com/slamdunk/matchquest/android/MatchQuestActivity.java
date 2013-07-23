package com.slamdunk.matchquest.android;

import com.slamdunk.matchquest.core.MatchQuest;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MatchQuestActivity extends AndroidApplication {

	@Override
	public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
			config.useGL20 = true;
			initialize(new MatchQuest(), config);
	}
}
