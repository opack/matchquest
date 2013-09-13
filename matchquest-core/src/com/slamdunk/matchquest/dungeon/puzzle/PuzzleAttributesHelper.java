package com.slamdunk.matchquest.dungeon.puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.MathUtils;

public class PuzzleAttributesHelper {
	/**
	 * Liste les attributs de base
	 */
	public static final List<PuzzleAttributes> BASE_ATTRIBUTES;
	/**
	 * Table indiquant l'attribut Super pour un PuzzleAttributes
	 */
	public static final Map<PuzzleAttributes, PuzzleAttributes> SUPER_ATTRIBUTES;
	/**
	 * Table indiquant l'attribut Hyper pour un PuzzleAttributes
	 */
	public static final Map<PuzzleAttributes, PuzzleAttributes> HYPER_ATTRIBUTES;
	
	static {
		BASE_ATTRIBUTES = new ArrayList<PuzzleAttributes>();
		SUPER_ATTRIBUTES = new HashMap<PuzzleAttributes, PuzzleAttributes>();
		HYPER_ATTRIBUTES = new HashMap<PuzzleAttributes, PuzzleAttributes>();
		for (PuzzleAttributes attribute : PuzzleAttributes.values()) {
			switch (attribute.getType()) {
			case BASE:
				BASE_ATTRIBUTES.add(attribute);
				break;
			case SUPER:
				SUPER_ATTRIBUTES.put(attribute.getBaseAttribute(), attribute);
				break;
			case HYPER:
				HYPER_ATTRIBUTES.put(attribute.getBaseAttribute(), attribute);
				break;
			case EMPTY:
				// Rien à faire avec EMPTY
				continue;
			}
		}
	}
	
	/**
	 * Vérifie que 2 attributs sont matchables. Si ces attributs sont des supers, on tente
	 * de matcher leur type de base.
	 */
	public static boolean areMatchable(PuzzleAttributes attribute1, PuzzleAttributes attribute2) {
		// EMPTY n'est jamais matchable
		if (attribute1 == PuzzleAttributes.EMPTY || attribute2 == PuzzleAttributes.EMPTY) {
			return false;
		}
		return getBaseAttribute(attribute1) == getBaseAttribute(attribute2);
	}

	public static PuzzleAttributes getBaseAttribute(PuzzleAttributes attribute) {
		PuzzleAttributes baseAttribute = attribute.getBaseAttribute();
		if (baseAttribute == null) {
			return attribute;
		}
		return baseAttribute;
	}

	public static PuzzleAttributes getHyperAttribute(PuzzleAttributes attribute) {
		PuzzleAttributes baseAttribute = getBaseAttribute(attribute);
		return HYPER_ATTRIBUTES.get(baseAttribute);
	}
	
	public static PuzzleAttributes getRandomBaseAttribute() {
		int random = MathUtils.random(BASE_ATTRIBUTES.size() - 1);
		return BASE_ATTRIBUTES.get(random);
	}

	public static PuzzleAttributes getSuperAttribute(PuzzleAttributes attribute) {
		PuzzleAttributes baseAttribute = getBaseAttribute(attribute);
		return SUPER_ATTRIBUTES.get(baseAttribute);
	}
}
