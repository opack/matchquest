package com.slamdunk.matchquest.html;

import com.slamdunk.matchquest.core.MatchQuest;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class MatchQuestHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new MatchQuest();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
