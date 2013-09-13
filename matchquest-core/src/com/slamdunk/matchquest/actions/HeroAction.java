package com.slamdunk.matchquest.actions;

import com.slamdunk.matchquest.dungeon.puzzle.AlignmentOrientation;
import com.slamdunk.matchquest.dungeon.puzzle.AttributeTypes;
import com.slamdunk.matchquest.dungeon.puzzle.Puzzle;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleAttributes;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleAttributesHelper;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleLogic.AttributeData;
import com.slamdunk.matchquest.dungeon.puzzle.PuzzleMatchData;
import com.slamdunk.utils.Point;

/**
 * Action exécutée lorsque le joueur aligne des items.
 * Cette action abstraite gère le découpage de l'alignement et appelle
 * les méthodes perform*Action adéquates suivant l'alignement effectué.
 */
public abstract class HeroAction {
	
	public interface HeroActionFactory {
		/**
		 * Crée une nouvelle instance de l'action pour ce puzzle et ces données matchées
		 * @param puzzle
		 * @param matchData
		 * @return
		 */
		public abstract HeroAction createAction(Puzzle puzzle, PuzzleMatchData matchData);
	}

	private Puzzle puzzle;
	private PuzzleMatchData matchData;

	/**
	 * Crée un Hyper de type source à l'emplacement source
	 * @param puzzle
	 * @param source
	 */
	private void createHyper(Puzzle puzzle, AttributeData source) {
		if (hasHyperAction()) {
			PuzzleAttributes hyperAttribute = PuzzleAttributesHelper.getHyperAttribute(source.attribute);
			if (hyperAttribute != null) {
				puzzle.createAttribute(source.position.getX(), source.position.getY(), hyperAttribute);
			}
		}
	}
	
	/**
	 * Crée un Super de type source  à l'emplacement source
	 * @param puzzle
	 * @param source
	 */
	private void createSuper(Puzzle puzzle, AttributeData source) {
		if (hasSuperAction()) {
			PuzzleAttributes superAttribute = PuzzleAttributesHelper.getSuperAttribute(source.attribute);
			if (superAttribute != null) {
				puzzle.createAttribute(source.position.getX(), source.position.getY(), superAttribute);
			}
		}
	}

	/**
	 * Retourne l'attribut associé à cette action
	 */
	public abstract PuzzleAttributes getAttribute();

	/**
	 * Indique si l'action crée un super lors de l'alignement de 4 items
	 * @return
	 */
	public abstract boolean hasHyperAction();

	/**
	 * Indique si l'action crée un super lors de l'alignement de 4 items
	 */
	public abstract boolean hasSuperAction();
	
	/**
	 * Utilise le puzzle et le matchData définis avec setPuzzle() et setMatchData().
	 * Pratique pour mettre une action de côté et l'exécuter plus tard.
	 */
	public void perform() {
		perform(puzzle, matchData);
	}
	
	/**
	 * Effectue l'action sur le puzzle indiqué en tenant compte des attributs
	 * alignés. Par défaut le comportement est :
	 *   - Supprimer les attributs de l'alignement
	 *   - Créer (si nécessaire) un attribut super ou hyper
	 *   - Demander la chute des attributs
	 * @param puzzleStage Puzzle
	 * @param matchData Informations sur l'alignement
	 */
	public void perform(Puzzle puzzle, PuzzleMatchData matchData) {
		// Supprimer les attributs de l'alignement
		for (AttributeData data : matchData.getAttributes()) {
			puzzle.eatAttribute(data.position.getX(), data.position.getY());
		}
		
		// Effectue les actions adéquates
		int count = matchData.count();
		switch (count) {
		// Echange de 2 supers ou d'un Hyper avec autre chose
		case 2:
			resolveNonStandardAlignment(puzzle, matchData);
			break;
		// Alignement standard
		case 3:
			resolveAlignment(puzzle, matchData);
			break;
		// Alignement standard + création d'un super
		case 4:
			resolveAlignment(puzzle, matchData);
			createSuper(puzzle, matchData.getSource());
			break;
		// Alignement standard + création d'un hyper
		case 5:
			resolveAlignment(puzzle, matchData);
			createHyper(puzzle, matchData.getSource());	
			break;
		}
		
		// Chute des attributs restants
		if (count > 0) {
			puzzle.fall();
		}
	}
	
	/**
	 * Appelée lorsque le Super de l'action courante est échangé avec le super indiqué et qu'un
	 * effet combo entre 2 Supers doit être déclenché.
	 * Y tester si otherSuper participe bien à un combo et effectuer l'effet adéquat.
	 * @param puzzle
	 * @param otherSuper
	 */
	protected abstract void performComboAction(Puzzle puzzle, AttributeData thisSuper, AttributeData otherSuper, AlignmentOrientation orientation);
	
	/**
	 * Appelée lorsque l'Hyper de l'action courante est échangé avec l'item indiqué et que l'effet
	 * Hyper doit être déclenché.
	 * ATTENTION ! otherItem peut être null si l'Hyper est déclenché dans un alignement.
	 * @param puzzle
	 * @param otherItem
	 */
	protected abstract void performHyperAction(Puzzle puzzle, Point position, AttributeData otherItem, AlignmentOrientation orientation);

	/**
	 * Appelée lorsque des items standards de l'action courante sont dans un alignement
	 * et que l'effet standard doit être effectué.
	 * @param puzzle
	 * @param i 
	 * @param otherSuper
	 */
	protected abstract void performStandardAction(Puzzle puzzle, int nbAlignedItems);

	/**
	 * Appelée lorsque le Super de l'action courante est déclenchée dans un alignement
	 * et que l'effet du Super doit être effectué.
	 * @param puzzle
	 * @param matchData
	 */
	protected abstract void performSuperAction(Puzzle puzzle, Point position, AlignmentOrientation orientation);

	/**
	 * Effectue l'effet standard et déclenche les éventuels effets Super
	 * contenus dans l'alignement
	 * @param puzzle
	 * @param matchData
	 */
	private void resolveAlignment(Puzzle puzzle, PuzzleMatchData matchData) {
		// Effectue l'effet standard
		performStandardAction(puzzle, matchData.count());
		
		// Déclenche les effets Super et Hyper de l'alignement	
		for (AttributeData attribute : matchData.getAttributes()) {
			if (attribute.attribute.getType() == AttributeTypes.SUPER) {
				performSuperAction(puzzle, attribute.position, matchData.getOrientation());
			} else if (attribute.attribute.getType() == AttributeTypes.HYPER) {
				performHyperAction(puzzle, attribute.position, null, matchData.getOrientation());
			}
		}
	}
	
	/**
	 * Appelée lorsque 2 items non-standards sont alignés : super+super
	 * ou hyper+autre
	 * @param puzzle
	 * @param matchData
	 */
	private void resolveNonStandardAlignment(Puzzle puzzle, PuzzleMatchData matchData) {
		// Récupération des 2 items
		AttributeData source = matchData.getSource();
		AttributeData other = matchData.getAttributes().get(1);
		
		// Si l'un des deux est un hyper, alors on effectue l'hyper correspondant
		if (source.attribute.getType() == AttributeTypes.HYPER || other.attribute.getType() == AttributeTypes.HYPER) {
			// Si source est un hyper, alors son effet est prioritaire
			if (source.attribute.getType() == AttributeTypes.HYPER) {
				performHyperAction(puzzle, source.position, other, matchData.getOrientation());
			}
			// Sinon, on effectue l'hyper de l'autre attribut
			else {
				performHyperAction(puzzle, other.position, source, matchData.getOrientation());
			}
		}
		// Si la source et l'autre item sont des supers, alors on tente un combo
		if (source.attribute.getType() == AttributeTypes.SUPER
		&& other.attribute.getType() == AttributeTypes.SUPER) {
			performComboAction(puzzle, source, other, matchData.getOrientation());
		}
	}

	public void setPuzzle(Puzzle puzzle) {
		this.puzzle = puzzle;
	}

	public void setMatchData(PuzzleMatchData matchData) {
		this.matchData = matchData;
	}
}
