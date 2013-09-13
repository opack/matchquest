package com.slamdunk.matchquest.dungeon.puzzle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.slamdunk.matchquest.Assets;
import com.slamdunk.utils.Clip;

/**
 * Liste les différents attributs disponibles dans le puzzle, ainsi que
 * leurs propriétés
 */
public enum PuzzleAttributes {
	// Attribut vide
	EMPTY (Assets.attribute_empty, null, AttributeTypes.EMPTY),
	
	// Actions de base
	ATTACK_MELEE (Assets.action_attack_melee, null, AttributeTypes.BASE),
	ATTACK_DIST (Assets.action_attack_dist, null, AttributeTypes.BASE),
	DEFEND (Assets.action_defend, null, AttributeTypes.BASE),
	HEAL (Assets.action_heal, null, AttributeTypes.BASE),
	LOOT (Assets.action_loot, null, AttributeTypes.BASE),
	SPECIAL (Assets.action_special, null, AttributeTypes.BASE),
	
	// Attributs super
	SUPER_ATTACK_MELEE (Assets.action_super_attack_melee, Assets.action_super_attack_melee_clip, AttributeTypes.SUPER, ATTACK_MELEE),
	SUPER_ATTACK_DIST (Assets.action_super_attack_dist, Assets.action_super_attack_dist_clip, AttributeTypes.SUPER, ATTACK_DIST),
	SUPER_DEFEND (Assets.action_super_defend, Assets.action_super_defend_clip, AttributeTypes.SUPER, DEFEND),
	SUPER_HEAL (Assets.action_super_heal, Assets.action_super_heal_clip, AttributeTypes.SUPER, HEAL),
	SUPER_LOOT (Assets.action_super_loot, Assets.action_super_loot_clip, AttributeTypes.SUPER, LOOT),
	SUPER_SPECIAL (Assets.action_super_special, Assets.action_super_special_clip, AttributeTypes.SUPER, SPECIAL),
	
	// Attributs super
	HYPER_ATTACK_MELEE (Assets.action_hyper_attack_melee, Assets.action_hyper_attack_melee_clip, AttributeTypes.HYPER, ATTACK_MELEE),
	HYPER_ATTACK_DIST (Assets.action_hyper_attack_dist, Assets.action_hyper_attack_dist_clip, AttributeTypes.HYPER, ATTACK_DIST),
	HYPER_DEFEND (Assets.action_hyper_defend, Assets.action_hyper_defend_clip, AttributeTypes.HYPER, DEFEND),
	HYPER_HEAL (Assets.action_hyper_heal, Assets.action_hyper_heal_clip, AttributeTypes.HYPER, HEAL),
	HYPER_LOOT (Assets.action_hyper_loot, Assets.action_hyper_loot_clip, AttributeTypes.HYPER, LOOT),
	HYPER_SPECIAL (Assets.action_hyper_special, Assets.action_hyper_special_clip, AttributeTypes.HYPER, SPECIAL);

	private Clip clip;
	private Drawable drawable;
	private AttributeTypes type;
	private PuzzleAttributes baseAttribute;
	
	private PuzzleAttributes(TextureRegion texture, Clip clip, AttributeTypes type) {
		this(texture, clip, type, null);
	}
	private PuzzleAttributes(TextureRegion texture, Clip clip, AttributeTypes type, PuzzleAttributes baseAttribute) {
		this.clip = clip;
		this.drawable = new TextureRegionDrawable(texture);
		this.type = type;
		this.baseAttribute = baseAttribute;
	}
	public PuzzleAttributes getBaseAttribute() {
		return baseAttribute;
	}
	public Clip getClip() {
		return clip;
	}
	public Drawable getDrawable() {
		return drawable;
	}
	public AttributeTypes getType() {
		return type;
	}
}
