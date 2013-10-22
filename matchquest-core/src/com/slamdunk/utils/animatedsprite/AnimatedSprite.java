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
	 * Object charg� des manipulations et du dessin de la trame
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
	 * Dimensions et position du sprite en unit�s du monde
	 */
	private Rectangle bounds;
	/**
	 * Sprites fils li�s par une ancre au sprite courant
	 */
	private Map<String, Set<AnimatedSprite>> childrenLinkedSprites;
	/**
	 * Sprite parent auquel celui-ci est li�.
	 */
	private AnimatedSprite parentLinkSprite;
	/**
	 * Ancre qui lie le parent � ce sprite
	 */
	private String parentLinkAnchor;
	/**
	 * Indique si le sprite doit �tre dessin�
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
	 * Dessine la frame courante et r�alise l'actions associ�e
	 * � cette frame (son...)
	 */
	public void draw(float delta, SpriteBatch batch, float offsetX, float offsetY) {
		if (!visible) {
			return;
		}
		// Choisit l'image en fonction du temps �coul�
		updateStateTime(delta);
		
		// Dessine l'image
		draw(batch, offsetX, offsetY);
	}
	
	/**
	 * Dessine la frame courante et r�alise l'actions associ�e
	 * � cette frame (son...). Utilis�e apr�s update(delta) pour
	 * d�couper en 2 �tapes le dessin du clip, au lieu de tout
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
		
		// R�alise l'action associ�e si on vient de changer de frame
		currentAnimation.runFrameRunnables();
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public AnimationEx getCurrentAnimation() {
		return currentAnimation;
	}

	/**
	 * Retourne la hauteur, en unit�s du monde
	 * @return
	 */
	public float getHeight() {
		return bounds.height;
	}
	
	/**
	 * Retourne la position de la bordure droite, en unit�s du monde
	 * @return
	 */
	public float getRight() {
		return bounds.x + bounds.width;
	}
	
	/**
	 * Retourne la position de la bordure haute, en unit�s du monde
	 * @return
	 */
	public float getTop() {
		return bounds.y + bounds.height;
	}
	
	/**
	 * Retourne la largeur, en unit�s du monde
	 * @return
	 */
	public float getWidth() {
		return bounds.width;
	}
	
	/**
	 * Retourne la position x, en unit�s du monde
	 * @return
	 */
	public float getX() {
		return bounds.x;
	}
	
	/**
	 * Retourne la position y, en unit�s du monde
	 * @return
	 */
	public float getY() {
		return bounds.x;
	}
	
	public boolean isVisible() {
		return visible && currentAnimation != null;
	}

	/**
	 * Lie le sprite indiqu� de fa�on � ce que son ancre anchor soit plac�e
	 * au-dessus de l'ancre du m�me nom du sprite courant 
	 * @param anchor
	 * @param sprite
	 */
	public void linkSprite(String anchor, AnimatedSprite sprite) {
		// Ajout du sprite aux sprites enfants li�s
		if (childrenLinkedSprites == null) {
			childrenLinkedSprites = new HashMap<String, Set<AnimatedSprite>>();
		}
		Set<AnimatedSprite> sprites = childrenLinkedSprites.get(anchor);
		if (sprites == null) {
			sprites = new HashSet<AnimatedSprite>();
			childrenLinkedSprites.put(anchor, sprites);
		}
		sprites.add(sprite);
		
		// Ajout du parent � ce sprite
		sprite.parentLinkSprite = this;
		sprite.parentLinkAnchor = anchor;
	}
	
	/**
	 * D�lie le sprite indiqu�
	 * @param sprite
	 */
	public void unlinkSprite(String anchor, AnimatedSprite sprite) {
		// Ajout du sprite aux sprites enfants li�s
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
	 * Charge l'animation d�crite dans le clip indiqu� et le rend accessible
	 * via l'�tiquette indiqu�e.
	 * @param tag Etiquette permettant de s�lectionner cette animation
	 * @param regions TextureRegion � animer
	 * @param file Fichier properties descriptif du clip � charger
	 */
	public void loadAnimation(String tag, String file) {
		// Charge le fichier de propri�t�s
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
		
		// Cr�e et stocke l'animation
		AnimationEx animation = new AnimationEx(frames, properties);
		animation.setPlayMode(properties.getIntProperty("animation.playMode", Animation.NORMAL));
		animations.put(tag, animation);
	}

	/**
	 * D�place l'enfant pour que son ancre corresponde � celle de l'ancre du parent
	 * @param parent
	 * @param parentAnchorPos
	 * @param child
	 * @param childAnchorPos
	 */
	private void moveLinkedChild(AnimatedSprite parent, Point parentAnchorPos, AnimatedSprite child, Point childAnchorPos) {
		// Calcule la diff�rence entre les 2 positions (en pixels)
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
		// Chargement de l'animation demand�e
		AnimationEx animation = animations.get(tag);
		// Le sprite est affich� uniquement si l'animation correspondante est trouv�e
		setVisible(animation != null);
		// Changement de l'animation courante
		currentAnimation = animation;
		if (currentAnimation != null) {
			// Force la m�j de la frame courante
			currentAnimation.forceNewFrame();
		}
		// RAZ du statetime pour red�marrer l'animation au d�but
		setStateTime(0f);
	}

	/**
	 * D�finit la position, en unit�s du monde
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		bounds.x = x;
		bounds.y = y;
	}

	/**
	 * D�finit la taille, en unit�s du monde
	 * @param width
	 * @param height
	 */
	public void setSize(float width, float height) {
		bounds.width = width;
		bounds.height = height;
	}
	
	/**
	 * Choisit l'image en fonction du temps indiqu�. Peut �tre appel�
	 * avec draw(SpriteBatch) pour d�couper en 2 �tapes le dessin
	 * du clip. Utile notamment pour r�utiliser le m�me clip plusieurs fois.
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
	 * Choisit la trame courante en fonction du temps �coul�
	 */
	private void updateCurrentFrame() {
		// M�j et r�cup�ration de la trame courante
		Frame currentFrame = currentAnimation.updateCurrentFrame();
		if (currentAnimation.isNewFrame() && currentFrame != null) {
			// D�finition de la r�gion � utiliser par le sprite
			sprite.setRegion(currentFrame.getRegion());
			bounds.width = sprite.getRegionWidth();
			bounds.height = sprite.getRegionHeight();
			// D�finition des propri�t�s du sprite en fonction de la frame
			sprite.setRotation(currentFrame.getRotation());
			// Mise � jour de la taille du sprite
			setSize(sprite.getRegionWidth(), sprite.getRegionHeight());
			// Liaison des ancres
			updateLinkedSpritesPositions();
		}
	}

	/**
	 * Met � jour la position des sprites li�s de fa�on � ce que leur
	 * ancre corresponde � l'ancre indiqu�e
	 */
	private void updateLinkedSpritesPositions() {
		if (currentAnimation == null) {
			return;
		}
		
		Point parentAnchorPos;
		Point childAnchorPos;
		Frame currentFrame = currentAnimation.getCurrentFrame();
		
		// Mise � jour de la position par rapport � celle du parent
		if (parentLinkSprite != null) {
			parentAnchorPos = parentLinkSprite.currentAnimation.getCurrentFrame().getAnchorPos(parentLinkAnchor);
			childAnchorPos = currentFrame.getAnchorPos(parentLinkAnchor);
			if (parentAnchorPos != null && childAnchorPos != null) {
				moveLinkedChild(parentLinkSprite, parentAnchorPos, this, childAnchorPos);
			}
		}
		
		// Mise � jour de la position des enfants par rapport � celle-ci
		if (childrenLinkedSprites != null) {
			Set<AnimatedSprite> children;
			String anchorName;
			for (Map.Entry<String, Set<AnimatedSprite>> childLink : childrenLinkedSprites.entrySet()){
				// R�cup�re la position de l'ancre dans le parent
				anchorName = childLink.getKey();
				parentAnchorPos = currentFrame.getAnchorPos(anchorName);
				if (parentAnchorPos == null) {
					continue;
				}
				// R�cup�re l'ensemble des enfants accroch�s � cette ancre
				children = childLink.getValue();
				for (AnimatedSprite child : children) {
					// R�cup�re la position de l'ancre dans l'enfant
					if (child.currentAnimation != null && child.currentAnimation.getCurrentFrame() != null) {
						childAnchorPos = child.currentAnimation.getCurrentFrame().getAnchorPos(anchorName);
						if (childAnchorPos != null) {
							// D�place l'enfant
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
