package com.slamdunk.matchquest;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Affiche les informations sur le joueur (comme sa santé ou sa défense)
 * mais aussi les boutons généraux (menu...).
 */
public class HUD extends Table {
	private Label lblAttack;
	private Label lblDefense;
	private Label lblHp;
	private Label lblCoins;
	private Label lblCurrentTurn;
	
	public HUD() {
		// Création de la partie stats du joueur
		LabelStyle style = new LabelStyle();
		style.font = Assets.hudFont;
		lblAttack = new Label("", style);
		lblDefense = new Label("", style);
		lblHp = new Label("", style);
		lblCoins = new Label("", style);
		lblCurrentTurn = new Label("", style);
		
		Table stats = new Table();
		stats.add(new Image(Assets.hud_heart)).size(32, 32).padRight(5);
		stats.add(lblHp).width(50).padRight(10).top();
		stats.add(new Image(Assets.hud_sword)).size(32, 32).padRight(5);
		stats.add(lblAttack).width(50).padRight(10).top();
		stats.add(new Image(Assets.hud_shield)).size(32, 32).padRight(5);
		stats.add(lblDefense).width(50).padRight(10).top();
		stats.add(new Image(Assets.hud_coin)).size(32, 32).padRight(5);
		stats.add(lblCoins).width(50).padRight(10).top();
		stats.pack();
		
		// Ajout au hud
		add().expandX();
		add(stats).center();
		add().expandX();
		row().pad(10.0f);
		add().expandX();
		add(lblCurrentTurn);
		add().expandX();
		pack();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		update();
	}
	
	public void update() {
		Player player = MatchQuest.getInstance().getPlayer();
		lblAttack.setText(String.valueOf(player.getAttack()));
		lblDefense.setText(player.getDefense() + "/" + player.getDefenseMax());
		lblHp.setText(String.valueOf(player.getHp()));
		lblCoins.setText(String.valueOf(player.getCoins()));
		if (player.isTurnOver()) {
			lblCurrentTurn.setText("TOUR DES MONSTRES");
		} else {
			lblCurrentTurn.setText("TOUR DU HEROS");
		}
	}

}
