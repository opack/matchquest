package com.slamdunk.utils.animatedsprite;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slamdunk.utils.Point;
import com.slamdunk.utils.PropertiesEx;

public class Frame {
	/**
	 * Indice de la trame dans l'animation
	 */
	private int index;
	/**
	 * Portion de l'image à afficher
	 */
	private TextureRegion region;
	/**
	 * Code à exécuter lorsque cette frame est affichée la première fois
	 */
	private List<FrameRunnable> runnables;
	/**
	 * Ancres utilisables pour position la frame par rapport à d'autres clips
	 */
	private Map<String, Point> anchors;
	/**
	 * Rotation à appliquer à l'image
	 */
	private float rotation;
	/**
	 * Retournement horizontal à appliquer à l'image
	 */
	private boolean flipH;
	/**
	 * Retournement vertical à appliquer à l'image
	 */
	private boolean flipV;
	/**
	 * Agrandissement de la largeur à appliquer à l'image
	 */
	private float scaleX;
	/**
	 * Agrandissement de la hauteur à appliquer à l'image
	 */
	private float scaleY;
	
	public Frame(int index, TextureRegion region) {
		this.index = index;
		this.region = region;
	}
	
	public Frame(Frame model) {
		if (model.anchors != null) {
			anchors = new HashMap<String, Point>();
			for (Map.Entry<String, Point> anchor : model.anchors.entrySet()) {
				anchors.put(anchor.getKey(), anchor.getValue());
			}
		}
		flipH = model.flipH;
		flipV = model.flipV;
		index = model.index;
		region = model.region;
		rotation = model.rotation;
		if (model.runnables != null) {
			runnables = new ArrayList<FrameRunnable>(model.runnables);
		}
		scaleX = model.scaleX;
		scaleY = model.scaleY;
	}

	public void addRunnable(FrameRunnable runnable) {
		if (runnables == null) {
			runnables = new ArrayList<FrameRunnable>();
		}
		runnables.add(runnable);
	}
	
	public void addAnchor(String name, Point position) {
		if (anchors == null) {
			anchors = new HashMap<String, Point>();
		}
		anchors.put(name, position);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public TextureRegion getRegion() {
		return region;
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public boolean isFlipH() {
		return flipH;
	}

	public void setFlipH(boolean flipH) {
		this.flipH = flipH;
	}

	public boolean isFlipV() {
		return flipV;
	}

	public void setFlipV(boolean flipV) {
		this.flipV = flipV;
	}

	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	public void load(PropertiesEx properties) {
		final String frameIndexKey = "frame" + index;
		rotation = properties.getFloatProperty(frameIndexKey + ".rotation", 0f);
		flipH = properties.getBooleanProperty(".flip.h", false);
		flipV = properties.getBooleanProperty(".flip.v", false);
		scaleX = properties.getFloatProperty(frameIndexKey + ".scale.x", 1.0f);
		scaleY = properties.getFloatProperty(frameIndexKey + ".scale.y", 1.0f);
		
		// Chargement des ancres
		loadAnchors(properties, frameIndexKey);
		
		// Chargement des runnables
		loadRunnables(properties, frameIndexKey);
	}
	
	/**
	 * Charge les ancres pour cette frame depuis le fichier de propriétés
	 * sous la clé de frame indiquée
	 * @param properties
	 * @param frameKey
	 */
	private void loadAnchors(PropertiesEx properties, String frameKey) {
		int index = 0;
		String key;
		String name;
		int x;
		int y;
		do {
			key = frameKey + ".anchor" + index;
			// Récupération des propriétés de l'ancre
			name = properties.getStringProperty(key + ".name", null);
			if (name != null) {
				x = properties.getIntProperty(key + ".x", 0);
				y = properties.getIntProperty(key + ".y", 0);
				// Ajout de l'ancre
				addAnchor(name, new Point(x, y));
			}
			index++;
		} while (name != null);
	}

	/**
	 * Charge les runnables pour cette frame depuis le fichier de propriétés
	 * sous la clé de frame indiquée
	 * @param properties
	 * @param frameKey
	 */
	private void loadRunnables(PropertiesEx properties, String frameKey) {
		int index = 0;
		String key;
		String type;
		do {
			key = frameKey + ".runnable" + index;
			// Récupération du type de runnable et de la classe associée
			type = properties.getStringProperty(key + ".type", null);
			if (type != null) {
				Class<FrameRunnable> runnableClass;
				try {
					runnableClass = (Class<FrameRunnable>) Class.forName(type);
					Object instance = runnableClass.newInstance();
					
					// Initialisation de l'instance
					Method loadMethod = runnableClass.getDeclaredMethod("load", PropertiesEx.class, String.class);
					loadMethod.invoke(instance, properties, key);
					
					// Ajout aux runnables
					addRunnable((FrameRunnable)instance);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			index++;
		} while (type != null);
	}

	public List<FrameRunnable> getRunnables() {
		return runnables;
	}

	/**
	 * Retourne la position de l'ancre indiquée ou null
	 * @param anchorName
	 * @return
	 */
	public Point getAnchorPos(String anchorName) {
		if (anchors == null) {
			return null;
		}
		return anchors.get(anchorName);
	}
}
