package com.slamdunk.utils.animatedsprite;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.matchquest.Assets;
import com.slamdunk.utils.Point;
import com.slamdunk.utils.PropertiesEx;

public class AnimatedSprite {
	/**
	 * Object chargé des manipulations et du dessin de la trame
	 */
	private Sprite sprite;
	/**
	 * Gestion de l'animation des images
	 */
	private AnimationEx currentAnimation;
	/**
	 * Animations possibles du Sprite
	 */
	private Map<String, AnimationEx> animations;
	/**
	 * Dimensions et position du sprite en unités du monde
	 */
	private Rectangle bounds;
	/**
	 * Sprites fils liés par une ancre au sprite courant
	 */
	private Map<String, Set<AnimatedSprite>> childrenLinkedSprites;
	/**
	 * Sprite parent auquel celui-ci est lié.
	 */
	private AnimatedSprite parentLinkSprite;
	/**
	 * Ancre qui lie le parent à ce sprite
	 */
	private String parentLinkAnchor;
	/**
	 * Indique si le sprite doit être dessiné
	 */
	private boolean visible;
	
	public AnimatedSprite() {
		animations = new HashMap<String, AnimationEx>();
		bounds = new Rectangle();
		sprite = new Sprite();
		visible = true;
	}
	
	public AnimatedSprite(AnimatedSprite model) {
		animations = new HashMap<String, AnimationEx>();
		for (Map.Entry<String, AnimationEx> animation : animations.entrySet()) {
			animations.put(animation.getKey(), new AnimationEx(animation.getValue()));
		}
		bounds = new Rectangle(model.bounds);
		sprite = new Sprite(model.sprite);
		visible = model.visible;
	}
	
	public AnimatedSprite getParentLinkSprite() {
		return parentLinkSprite;
	}

	/**
	 * Dessine la frame courante et réalise l'actions associée
	 * à cette frame (son...)
	 */
	public void draw(float delta, SpriteBatch batch, float offsetX, float offsetY) {
		if (!visible) {
			return;
		}
		// Choisit l'image en fonction du temps écoulé
		updateStateTime(delta);
		
		// Dessine l'image
		draw(batch, offsetX, offsetY);
	}
	
	/**
	 * Dessine la frame courante et réalise l'actions associée
	 * à cette frame (son...). Utilisée après update(delta) pour
	 * découper en 2 étapes le dessin du clip, au lieu de tout
	 * faire en une fois avec draw(float, SpriteBatch).
	 * @param f 
	 * @param x 
	 */
	public void draw(SpriteBatch batch, float offsetX, float offsetY) {
		if (!visible) {
			return;
		}
		
		// Place et dimensionne le sprite
		sprite.setBounds(
			bounds.x + offsetX,
			bounds.y + offsetY,
			bounds.width, //bounds.width * ppuX, 
			bounds.height);//bounds.height * ppuY);
		
		// Dessine la frame
		if (sprite.getTexture() != null) {
			sprite.draw(batch);
		}
		
		// Réalise l'action associée si on vient de changer de frame
		currentAnimation.runFrameRunnables();
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public AnimationEx getCurrentAnimation() {
		return currentAnimation;
	}

	/**
	 * Retourne la hauteur, en unités du monde
	 * @return
	 */
	public float getHeight() {
		return bounds.height;
	}
	
	/**
	 * Retourne la position de la bordure droite, en unités du monde
	 * @return
	 */
	public float getRight() {
		return bounds.x + bounds.width;
	}
	
	/**
	 * Retourne la position de la bordure haute, en unités du monde
	 * @return
	 */
	public float getTop() {
		return bounds.y + bounds.height;
	}
	
	/**
	 * Retourne la largeur, en unités du monde
	 * @return
	 */
	public float getWidth() {
		return bounds.width;
	}
	
	/**
	 * Retourne la position x, en unités du monde
	 * @return
	 */
	public float getX() {
		return bounds.x;
	}
	
	/**
	 * Retourne la position y, en unités du monde
	 * @return
	 */
	public float getY() {
		return bounds.x;
	}
	
	public boolean isVisible() {
		return visible && currentAnimation != null;
	}

	/**
	 * Lie le sprite indiqué de façon à ce que son ancre anchor soit placée
	 * au-dessus de l'ancre du même nom du sprite courant 
	 * @param anchor
	 * @param sprite
	 */
	public void linkSprite(String anchor, AnimatedSprite sprite) {
		// Ajout du sprite aux sprites enfants liés
		if (childrenLinkedSprites == null) {
			childrenLinkedSprites = new HashMap<String, Set<AnimatedSprite>>();
		}
		Set<AnimatedSprite> sprites = childrenLinkedSprites.get(anchor);
		if (sprites == null) {
			sprites = new HashSet<AnimatedSprite>();
			childrenLinkedSprites.put(anchor, sprites);
		}
		sprites.add(sprite);
		
		// Ajout du parent à ce sprite
		sprite.parentLinkSprite = this;
		sprite.parentLinkAnchor = anchor;
	}
	
	/**
	 * Délie le sprite indiqué
	 * @param sprite
	 */
	public void unlinkSprite(String anchor, AnimatedSprite sprite) {
		// Ajout du sprite aux sprites enfants liés
		if (childrenLinkedSprites == null) {
			return;
		}
		Set<AnimatedSprite> sprites = childrenLinkedSprites.get(anchor);
		if (sprites == null) {
			return;
		}
		sprites.remove(sprite);
		
		// Suppression du parent de ce sprite
		sprite.parentLinkSprite = null;
		sprite.parentLinkAnchor = null;
	}

	/**
	 * Charge l'animation décrite dans le clip indiqué et le rend accessible
	 * via l'étiquette indiquée.
	 * @param tag Etiquette permettant de sélectionner cette animation
	 * @param regions TextureRegion à animer
	 * @param file Fichier properties descriptif du clip à charger
	 */
	public void loadAnimation(String tag, String file) {
		// Charge le fichier de propriétés
		PropertiesEx properties = new PropertiesEx();
		FileHandle fh = Gdx.files.internal(file);
		InputStream inStream = fh.read();
		try {
			properties.load(inStream);
			inStream.close();
		} catch (IOException e) {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException ex) {
				}
			}
		}
		
		// Charge les TextureRegion
		String atlasName = properties.getProperty("spriteSheet.atlas");
		String regionName = properties.getProperty("spriteSheet.region");
		TextureAtlas atlas = (TextureAtlas)Assets.getAtlas(atlasName);
		if (atlas == null) {
			throw new IllegalArgumentException("No atlas named " + atlasName + " could be found.");
		}
		Array<AtlasRegion> regions = atlas.findRegions(regionName);
		if (regions == null) {
			throw new IllegalArgumentException("No region named " + regionName + " could be found.");
		}
		TextureRegion[] frames = new TextureRegion[regions.size];
		for (int index = 0; index < regions.size; index++) {
			frames[index] = regions.get(index);
		}
		
		// Crée et stocke l'animation
		AnimationEx animation = new AnimationEx(frames, properties);
		animation.setPlayMode(properties.getIntProperty("animation.playMode", Animation.NORMAL));
		animations.put(tag, animation);
	}

	/**
	 * Déplace l'enfant pour que son ancre corresponde à celle de l'ancre du parent
	 * @param parent
	 * @param parentAnchorPos
	 * @param child
	 * @param childAnchorPos
	 */
	private void moveLinkedChild(AnimatedSprite parent, Point parentAnchorPos, AnimatedSprite child, Point childAnchorPos) {
		// Calcule la différence entre les 2 positions (en pixels)
		// et place l'enfant par rapport au parent
		child.setPosition(
			parent.bounds.x + parentAnchorPos.getX() - childAnchorPos.getX(), 
			parent.bounds.y + (parent.bounds.height - parentAnchorPos.getY()) - (child.bounds.height - childAnchorPos.getY()));		
	}
	
	/**
	 * Change l'animation courante
	 * @param tag
	 */
	public void setCurrentAnimation(String tag) {
		// Chargement de l'animation demandée
		AnimationEx animation = animations.get(tag);
		// Le sprite est affiché uniquement si l'animation correspondante est trouvée
		setVisible(animation != null);
		// Changement de l'animation courante
		currentAnimation = animation;
		if (currentAnimation != null) {
			// Force la màj de la frame courante
			currentAnimation.forceNewFrame();
		}
		// RAZ du statetime pour redémarrer l'animation au début
		setStateTime(0f);
	}

	/**
	 * Définit la position, en unités du monde
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		bounds.x = x;
		bounds.y = y;
	}

	/**
	 * Définit la taille, en unités du monde
	 * @param width
	 * @param height
	 */
	public void setSize(float width, float height) {
		bounds.width = width;
		bounds.height = height;
	}
	
	/**
	 * Choisit l'image en fonction du temps indiqué. Peut être appelé
	 * avec draw(SpriteBatch) pour découper en 2 étapes le dessin
	 * du clip. Utile notamment pour réutiliser le même clip plusieurs fois.
	 * @param stateTime
	 */
	public void setStateTime(float stateTime) {
		if (currentAnimation != null) {
			currentAnimation.setStateTime(stateTime);
			updateCurrentFrame();
		}
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setX(float x) {
		bounds.x = x;
	}
	
	public void setY(float y) {
		bounds.y = y;
	}

	/**
	 * Choisit la trame courante en fonction du temps écoulé
	 */
	private void updateCurrentFrame() {
		// Màj et récupération de la trame courante
		Frame currentFrame = currentAnimation.updateCurrentFrame();
		if (currentAnimation.isNewFrame() && currentFrame != null) {
			// Définition de la région à utiliser par le sprite
			sprite.setRegion(currentFrame.getRegion());
			bounds.width = sprite.getRegionWidth();
			bounds.height = sprite.getRegionHeight();
			// Définition des propriétés du sprite en fonction de la frame
			sprite.setRotation(currentFrame.getRotation());
			// Mise à jour de la taille du sprite
			setSize(sprite.getRegionWidth(), sprite.getRegionHeight());
			// Liaison des ancres
			updateLinkedSpritesPositions();
		}
	}

	/**
	 * Met à jour la position des sprites liés de façon à ce que leur
	 * ancre corresponde à l'ancre indiquée
	 */
	private void updateLinkedSpritesPositions() {
		if (currentAnimation == null) {
			return;
		}
		
		Point parentAnchorPos;
		Point childAnchorPos;
		Frame currentFrame = currentAnimation.getCurrentFrame();
		
		// Mise à jour de la position par rapport à celle du parent
		if (parentLinkSprite != null) {
			parentAnchorPos = parentLinkSprite.currentAnimation.getCurrentFrame().getAnchorPos(parentLinkAnchor);
			childAnchorPos = currentFrame.getAnchorPos(parentLinkAnchor);
			if (parentAnchorPos != null && childAnchorPos != null) {
				moveLinkedChild(parentLinkSprite, parentAnchorPos, this, childAnchorPos);
			}
		}
		
		// Mise à jour de la position des enfants par rapport à celle-ci
		if (childrenLinkedSprites != null) {
			Set<AnimatedSprite> children;
			String anchorName;
			for (Map.Entry<String, Set<AnimatedSprite>> childLink : childrenLinkedSprites.entrySet()){
				// Récupère la position de l'ancre dans le parent
				anchorName = childLink.getKey();
				parentAnchorPos = currentFrame.getAnchorPos(anchorName);
				if (parentAnchorPos == null) {
					continue;
				}
				// Récupère l'ensemble des enfants accrochés à cette ancre
				children = childLink.getValue();
				for (AnimatedSprite child : children) {
					// Récupère la position de l'ancre dans l'enfant
					if (child.currentAnimation != null && child.currentAnimation.getCurrentFrame() != null) {
						childAnchorPos = child.currentAnimation.getCurrentFrame().getAnchorPos(anchorName);
						if (childAnchorPos != null) {
							// Déplace l'enfant
							moveLinkedChild(this, parentAnchorPos, child, childAnchorPos);
						}
					}
				}
			}
		}
	}
	
	public void updateStateTime(float delta) {
		if (currentAnimation != null) {
			setStateTime(currentAnimation.getStateTime() + delta);
		}
	}
}
