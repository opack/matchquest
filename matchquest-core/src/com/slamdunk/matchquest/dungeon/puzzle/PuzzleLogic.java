package com.slamdunk.matchquest.dungeon.puzzle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.slamdunk.utils.Point;

/**
 * G�re la repr�sentation logique du puzzle
 */
public class PuzzleLogic {
	public class AlignmentData {
		public List<AttributeData> attributes;
		/**
		 * Indique si l'alignement est horizontal ou vertical
		 */
		public AlignmentOrientation orientation;
		/**
		 * Indice de l'attribut � la source de l'alignement
		 */
		public int alignSourceAttributeIndex;
		
		public AlignmentData() {
			attributes = new ArrayList<AttributeData>();
		}
		
		public void add(Point position, PuzzleAttributes attribute) {
			attributes.add(new AttributeData(position, attribute));
		}
		
		public void clear() {
			attributes.clear();
			alignSourceAttributeIndex = -1;
		}
		
		public int size() {
			return attributes.size();
		}

		public void updateSourceIndex(int x, int y) {
			alignSourceAttributeIndex = -1;
			Point pos;
			final int count = attributes.size();
			for (int cur = 0; cur < count; cur ++) {
				pos = attributes.get(cur).position;
				if (pos.getX() == x && pos.getY() == y) {
					alignSourceAttributeIndex = cur;
					break;
				}
			}
		}
	}
	public class AttributeData {
		public PuzzleAttributes attribute;
		public Point position;
		public AttributeData(Point position, PuzzleAttributes attribute) {
			if (position == null || attribute == null) {
				throw new IllegalStateException("Tried to create an AttributeData with no position or no attribute : pos=" + position + ", attribute=" + attribute);
			}
			this.attribute = attribute;
			this.position = position;
		}
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof AttributeData)) {
				return false;
			}
			AttributeData other = (AttributeData)obj;
			return other.attribute.equals(attribute)
				&& other.position.equals(position);
		}
		
		@Override
		public int hashCode() {
			return position.hashCode() ^ attribute.hashCode();
		}
	}
	
	private int width;
	private int height;
	private PuzzleImage[][] puzzleImages;
	private Puzzle puzzleStage;
	
	private AlignmentData hAlignData;
	private AlignmentData vAlignData;
	private List<Point> lastFallen;
	private Set<Point> alreadyMatched;
	
	private PuzzleListener listener;
	
	public PuzzleLogic(Puzzle puzzle) {
		this.width = puzzle.getPuzzleWidth();
		this.height = puzzle.getPuzzleHeight();
		
		hAlignData = new AlignmentData();
		hAlignData.orientation = AlignmentOrientation.HORIZONTAL;
		vAlignData = new AlignmentData();
		vAlignData.orientation = AlignmentOrientation.VERTICAL;
		lastFallen = new ArrayList<Point>();
		alreadyMatched = new HashSet<Point>();
		
		// Cr�ation du puzzle
		this.puzzleStage = puzzle;
		puzzleImages = puzzle.getPuzzleImages();
	}
	
	public boolean fall() {
		lastFallen.clear();
		int yEmpty;
		int yFall = 0;
		int nbOutOfGrid;
		
		for (int x = 0; x < width; x ++) {
			// Le prochain �l�ment � �ventuellement appara�tre hors du tableau commencera juste
			// au-dessus du tableau : hauteur - 1 + 1 = hauteur
			nbOutOfGrid = 0;
			for (int y = 0; y < height; y ++) {
				if (puzzleImages[x][y].getAttribute() == PuzzleAttributes.EMPTY) {
					yEmpty = y;
					// D�termine l'emplacement du d�but de la chute en cherchant le prochain
					// attribut non vide, qui va donc chuter.
					if (nbOutOfGrid > 0) {
						// S'il y a au moins un attribut � cr�er hors de la grille, alors tous
						// les prochains seront forc�ment aussi � cr�er hors de de la grille
						yFall = height + nbOutOfGrid;
						nbOutOfGrid ++;
					} else {
						// Si on ne sait pas encore si des attributs peuvent chuter, on cherche
						// le prochain attribut non vide qui peut chuter
						int curRow;
						for (curRow = yEmpty + 1; curRow < height; curRow ++) {
							if (puzzleImages[x][curRow].getAttribute() != PuzzleAttributes.EMPTY
							&& puzzleImages[x][curRow].isCanFall()) {
								yFall = curRow;
								break;
							}
						}
						// Si on n'a pas trouv� d'�l�ment non vide avant la fin du tableau, alors
						// un nouvel �l�ment devra �tre cr�� hors du tableau, juste au-dessus du dernier
						// �l�ment cr��
						if (curRow == height) {
							yFall = height + nbOutOfGrid;
							nbOutOfGrid ++;
						}
					}
					
					// Cr�ation d'un PuzzleImage pour faire une belle animation.
					// A la fin de l'animation, l'attribut tomb� sera affect� � l'image actuellement vide.
					puzzleStage.createFallAnimation(x, yFall, yEmpty);
					
					// M�morise le point de chute de l'attribut pour tester ensuite s'il participe �
					// une combinaison une fois le tableau stabilis�.
					lastFallen.add(new Point(x, yEmpty));
				}
			}
		}
		return !lastFallen.isEmpty();
	}

	public PuzzleAttributes get(int x, int y) {
		if (!isValidPos(x, y)) {
			return null;
		}
		return puzzleImages[x][y].getAttribute();
	}

	public PuzzleListener getListener() {
		return listener;
	}

	/**
	 * G�n�re un attribut en s'assurant qu'il ne va pas
	 * provoquer l'alignement de 3 attributs.
	 */
	public PuzzleAttributes initAttribute(int x, int y) {
		PuzzleAttributes attribute;
		do {
			attribute = PuzzleAttributesHelper.getRandomBaseAttribute();
		}
		// Si les 2 attributs � gauche ou en haut sont identiques, on en choisit un autre
		while (isSameOnLeftOrTop(attribute, x, y, 2));
		return attribute;
	}

	private boolean isSameAttribute(PuzzleAttributes attribute, int x, int y) {
		if (isValidPos(x, y)) {
			PuzzleImage image = puzzleImages[x][y];
			return image != null && attribute == image.getAttribute();
		}
		return false;
	}
	
	/**
	 * Test si l'attribut indiqu� se trouve �galement aux 2 positions voisines
	 * dans la ligne � gauche ou en haut de la position indiqu�e.
	 */
	private boolean isSameOnLeftOrTop(PuzzleAttributes attribute, int x, int y, int depth) {
		// Teste vers la gauche
		int countLeft = 0;
		for (int cur = x - 1; cur >= x - depth; cur--) {
			if (isValidPos(cur, y) && puzzleImages[cur][y].getAttribute() == attribute) {
				countLeft ++;
			} else {
				break;
			}
		}
		if (countLeft == depth) {
			return true;
		}
		// Teste vers le haut
		int countTop = 0;
		for (int cur = y + 1; cur <= y + depth; cur++) {
			if (isValidPos(x, cur) && puzzleImages[x][cur].getAttribute() == attribute) {
				countTop ++;
			} else {
				break;
			}
		}
		if (countTop == depth) {
			return true;
		}
		// Au moins 1 des 2 � gauche et au-dessus n'est pas du m�me type
		return false;
	}

	/**
	 * V�rifie s'il reste au moins un switch possible
	 * @return
	 */
	public boolean isSwitchPossible() {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				if (isSwitchPossible(x, y)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isSwitchPossible(int x, int y) {
		if (!isValidPos(x, y)) {
			return false;
		}
		PuzzleImage curImage = puzzleImages[x][y];
		if (curImage == null) {
			return false;
		}
		PuzzleAttributes curAttribute = curImage.getAttribute();
		
		// Si c'est un hyper, alors on peut forc�ment le switcher
		if (curAttribute.getType() == AttributeTypes.HYPER) {
			return true;
		}
		
		// Teste vers le haut
		if (
		(
			// Combinaisons avec 2 align�s et...
			isSameAttribute(curAttribute, x, y + 1)
			// ... un �cart� (combinaison en "i")
			&& (isSameAttribute(curAttribute, x, y + 3)
			// ou un sur le c�t� (combinaison en "L")
				|| isSameAttribute(curAttribute, x - 1, y + 2)
				|| isSameAttribute(curAttribute, x + 1, y + 2))
		)
		||
		(
			// Combinaisons en "parenth�ses"
			isSameAttribute(curAttribute, x, y + 2)
			&& (isSameAttribute(curAttribute, x - 1, y + 1)
				|| isSameAttribute(curAttribute, x + 1, y + 1))
		)
		) {
			return true;
		}
		
		// Teste vers le bas
		if (
		(
			// Combinaisons avec 2 align�s et...
			isSameAttribute(curAttribute, x, y - 1)
			// ... un �cart� (combinaison en "i")
			&& (isSameAttribute(curAttribute, x, y - 3)
			// ou un sur le c�t� (combinaison en "L")
				|| isSameAttribute(curAttribute, x - 1, y - 2)
				|| isSameAttribute(curAttribute, x + 1, y - 2))
		)
		||
		(
			// Combinaisons en "parenth�ses"
			isSameAttribute(curAttribute, x, y - 2)
			&& (isSameAttribute(curAttribute, x - 1, y - 1)
				|| isSameAttribute(curAttribute, x + 1, y - 1))
		)
		) {
			return true;
		}
		
		// Teste vers la gauche
		if (
		(
			// Combinaisons avec 2 align�s et...
			isSameAttribute(curAttribute, x - 1, y)
			// ... un �cart� (combinaison en "i")
			&& (isSameAttribute(curAttribute, x - 3, y)
			// ou un sur le c�t� (combinaison en "L")
				|| isSameAttribute(curAttribute, x - 2, y - 1)
				|| isSameAttribute(curAttribute, x - 2, y + 1))
		)
		||
		(
			// Combinaisons en "parenth�ses"
			isSameAttribute(curAttribute, x - 2, y)
			&& (isSameAttribute(curAttribute, x - 1, y + 1)
				|| isSameAttribute(curAttribute, x - 1, y - 1))
		)
		) {
			return true;
		}
		
		// Teste vers la droite
		if (
		(
			// Combinaisons avec 2 align�s et...
			isSameAttribute(curAttribute, x + 1, y)
			// ... un �cart� (combinaison en "i")
			&& (isSameAttribute(curAttribute, x + 3, y)
			// ou un sur le c�t� (combinaison en "L")
				|| isSameAttribute(curAttribute, x + 2, y - 1)
				|| isSameAttribute(curAttribute, x + 2, y + 1))
		)
		||
		(
			// Combinaisons en "parenth�ses"
			isSameAttribute(curAttribute, x + 2, y)
			&& (isSameAttribute(curAttribute, x + 1, y + 1)
				|| isSameAttribute(curAttribute, x + 1, y - 1))
		)
		) {
			return true;
		}
		return false;
	}

	/**
	 * Retourne true si la position existe dans le puzzle
	 */
	public boolean isValidPos(int x, int y) {
		return x > -1 && x < width
		&& y > -1 && y < height;
	}
	
	public void match() {
		if (lastFallen.isEmpty()) {
			return;
		}
		alreadyMatched.clear();
		for (Point fallen : lastFallen) {
			if (!alreadyMatched.contains(fallen)) {
				resolveAlignments(fallen.getX(), fallen.getY());
			}
		}
		lastFallen.clear();
		alreadyMatched.clear();
	}

	/**
	 * Recherche si l'attribut � la position indiqu�e participe �
	 * un alignement.
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean match(int x, int y) {
		PuzzleAttributes element = puzzleImages[x][y].getAttribute();
		// Rien ne match avec EMPTY
		if (element == PuzzleAttributes.EMPTY) {
			return false;
		}
		
		// V�rifie si l'item participe � un alignement horizontal
		for (int curCol = x; curCol > -1; curCol--) {
			if (!match(curCol, y, element, hAlignData)) {
				break;
			}
		}
		// On commence � col+1 pour ne pas compter 2 fois la cellule � col;row
		for (int curCol = x + 1; curCol < width; curCol++) {
			if (!match(curCol, y, element, hAlignData)) {
				break;
			}
		}
		
		// V�rifie si l'item participe � un alignement vertical
		for (int curRow = y; curRow > -1; curRow--) {
			if (!match(x, curRow, element, vAlignData)) {
				break;
			}
		}
		// On commence � row+1 pour ne pas compter 2 fois la cellule � col;row
		for (int curRow = y + 1; curRow < height; curRow++) {
			if (!match(x, curRow, element, vAlignData)) {
				break;
			}
		}
		// Le match r�ussit s'il y a au moins 3 �l�ments align�s
		return hAlignData.size() > 2 || vAlignData.size() > 2;
	}
	
	/**
	 * Recherche si l'attribut � la position indiqu�e est matchable avec l'�l�ment
	 * sp�cifi�.
	 */
	private boolean match(int x, int y, PuzzleAttributes element, AlignmentData alignData) {
		PuzzleAttributes neighborElement = puzzleImages[x][y].getAttribute();
		if (PuzzleAttributesHelper.areMatchable(neighborElement, element)) {
			alignData.add(new Point(x, y), neighborElement);
			return true;
		}
		return false;
	}

	/**
	 * V�rifie si un switch avec un hyper a �t� fait, et dans ce cas, d�clenche l'effet
	 * ad�quat.
	 */
	private boolean matchHyper(int firstX, int firstY, int secondX, int secondY) {
		// On s'assure qu'au moins un des deux attributs est un HYPER
		PuzzleAttributes firstAttribute = puzzleImages[firstX][firstY].getAttribute();
		PuzzleAttributes secondAttribute = puzzleImages[secondX][secondY].getAttribute();
		if (firstAttribute.getType() != AttributeTypes.HYPER && secondAttribute.getType() != AttributeTypes.HYPER) {
			return false;
		}
		
		// D�clenche l'effet ad�quat
		PuzzleMatchData matchData = new PuzzleMatchData();
		matchData.setSource(new AttributeData(new Point(firstX, firstY), firstAttribute));
		matchData.add(new AttributeData(new Point(secondX, secondY), secondAttribute));
		listener.switchDone(puzzleStage, matchData);
		return true;
	}

	/**
	 * V�rifie si un switch entre 2 supers a �t� fait, et dans ce cas, d�clenche l'effet
	 * ad�quat.
	 */
	private boolean matchSupersCombo(int firstX, int firstY, int secondX, int secondY) {
		// On s'assure qu'au moins un des deux attributs est un HYPER
		PuzzleAttributes firstAttribute = puzzleImages[firstX][firstY].getAttribute();
		PuzzleAttributes secondAttribute = puzzleImages[secondX][secondY].getAttribute();
		if (firstAttribute.getType() != AttributeTypes.SUPER || secondAttribute.getType() != AttributeTypes.SUPER) {
			return false;
		}
		
		// D�clenche l'effet ad�quat
		PuzzleMatchData matchData = new PuzzleMatchData();
		matchData.setSource(new AttributeData(new Point(firstX, firstY), firstAttribute));
		matchData.add(new AttributeData(new Point(secondX, secondY), secondAttribute));
		listener.switchDone(puzzleStage, matchData);
		return true;
	}
	
	/**
	 * V�rifie la pr�sence d'alignements comprenant la position indiqu�e
	 */
	private boolean resolveAlignments(int x, int y) {
		if (!isValidPos(x, y)) {
			return false;
		}
		
		// Recherche des alignements
		hAlignData.clear();
		vAlignData.clear();
		if (!match(x, y)) {
			return false;
		}
		hAlignData.updateSourceIndex(x, y);
		vAlignData.updateSourceIndex(x, y);
		
		// On r�soud l'alignement en privil�giant le plus long.
		int hCount = hAlignData.size();
		int vCount = vAlignData.size();
		PuzzleMatchData matchData = new PuzzleMatchData();
		matchData.setSource(hAlignData.attributes.get(hAlignData.alignSourceAttributeIndex));
		if (hCount == vCount) {
			// Les deux lignes font la m�me taille
			matchData.add(hAlignData.attributes);
			matchData.add(vAlignData.attributes);
		} else if (hCount > vCount) {
			// La ligne horizontale est plus longue
			matchData.add(hAlignData.attributes);
		} else {
			// La ligne verticale est plus longue
			matchData.add(vAlignData.attributes);
		}
		
		// R�cup�ration de l'action � effectuer
		listener.switchDone(puzzleStage, matchData);
		
		// On marque les attributs comme ayant �t� match�s pour qu'on ne les match pas � nouveau
		for (AttributeData attributeData : matchData.getAttributes()) {
			alreadyMatched.add(attributeData.position);
		}
		return true;
	}
	
	private boolean resolveSupers(int firstX, int firstY, int secondX, int secondY) {
		// Teste si on �change un hyper avec autre chose
		if (matchHyper(firstX, firstY, secondX, secondY)
		// Teste si on �change entre eux 2 supers
		|| matchSupersCombo(firstX, firstY, secondX, secondY)) {
			return true;
		}
		return false;
	}
	

	public void setListener(PuzzleListener listener) {
		this.listener = listener;
	}

	/**
	 * Retourne true si le switch a �t� autoris� et effectu�.
	 */
	public boolean switchAttributes(int firstX, int firstY, int secondX, int secondY) {
		if (!isValidPos(firstX, firstY) || !isValidPos(secondX, secondY)) {
			return false;
		}
		
		// Recherche des �ventuelles combinaisons
		// Si le switch des 2 attributs d�clenche un super effet, on le traite
		if (resolveSupers(firstX, firstY, secondX, secondY)) {
			return true;
		}
		
		// Si on n'a pas invers� un hyper avec autre chose, alors on regarde
		// si l'inversion a provoqu� l'apparition de combinaisons
		boolean isFirstAligned = resolveAlignments(firstX, firstY);
		boolean isSecondAligned = resolveAlignments(secondX, secondY);

		if (!isFirstAligned && !isSecondAligned) {
			return false;
		}
		return true;
	}
	
	public void updatePuzzle() {
		if (!fall()) {
			// Si rien n'est tomb�, alors on est dans une situation stable.
			// On va donc rechercher d'�ventuelles combinaisons.
			match();
		}
	}
}
