package com.slamdunk.matchquest.dungeon.puzzle;

import java.util.ArrayList;
import java.util.List;

import com.slamdunk.matchquest.dungeon.puzzle.PuzzleLogic.AttributeData;
import com.slamdunk.utils.Point;

/**
 * Contient les informations sur un match, c�d les items align�s,
 * l'orientation de l'alignement et la source (item d�plac� pour
 * cr�er l'alignement).
 * Cette classe s'assure que la source sera toujours le premier
 * attribut de la liste retourn�e par getAttributes() car aucun
 * �l�ment ne peut �tre ajout� avant un setSource().
 */
public class PuzzleMatchData {
	private AttributeData source;
	private AlignmentOrientation orientation;
	private List<AttributeData> attributes;
	
	public PuzzleMatchData() {
		attributes = new ArrayList<AttributeData>();
	}
	
	public boolean add(AttributeData data) {
		if (source == null) {
			System.err.println("No source attribute defined. Call setSource() before add().");
			return false;
		}
		return add(data, true);
	}

	private boolean add(AttributeData data, boolean updateOrientation) {
		if (data == null || data.position == null || data.attribute == null || attributes.contains(data)) {
			return false;
		}
		
		// Ajout de l'attribut
		attributes.add(data);
		
		// Mise � jour de l'orientation
		if (updateOrientation) {
			AlignmentOrientation thisOrientation = AlignmentOrientation.WHOLE;
			Point thisPosition = data.position;
			if (thisPosition.getX() == source.position.getX()) {
				thisOrientation = AlignmentOrientation.VERTICAL;
			} else if (thisPosition.getY() == source.position.getY()) {
				thisOrientation = AlignmentOrientation.HORIZONTAL;
			}
			if (orientation == null) {
				// L'orientation globale est celle entre cet �l�ment et la source
				orientation = thisOrientation;
			} else if ((orientation == AlignmentOrientation.VERTICAL && thisOrientation == AlignmentOrientation.HORIZONTAL)
					|| (orientation == AlignmentOrientation.HORIZONTAL && thisOrientation == AlignmentOrientation.VERTICAL)) {
				// Si on �tait d�j� vertical ou horizontal et que l� on est dans l'autre cas
				// alors finalement on est en corner
				orientation = AlignmentOrientation.CROSS;
			}
		}
		return true;
	}
	
	public void add(List<AttributeData> attributes) {
		for (AttributeData data : attributes) {
			add(data);
		}
	}
	
	public int count() {
		return attributes.size();
	}

	public List<AttributeData> getAttributes() {
		return attributes;
	}

	public AlignmentOrientation getOrientation() {
		return orientation;
	}

	public AttributeData getSource() {
		return source;
	}

	public boolean setSource(AttributeData data) {
		if (add(data, false)) {
			source = data;
			return true;
		}
		return false;
	}
}