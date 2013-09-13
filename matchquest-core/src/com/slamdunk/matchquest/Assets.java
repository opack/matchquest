/*
 * Copyright 2011 Rod Hyde (rod@badlydrawngames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.slamdunk.matchquest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;
import com.slamdunk.utils.Clip;
import com.slamdunk.utils.Config;

public class Assets {

	private static final String TEXT_FONT = Config.asString("fonts.characterFont", "ocr_a.fnt");

	private static Map<String, Disposable> disposables;
	
	private static TextureAtlas actionsAtlas;
	private static TextureAtlas attributesAtlas;
	private static TextureAtlas contextMenuAtlas;
	private static TextureAtlas dungeonMapAtlas;
	private static TextureAtlas hudAtlas;
	private static TextureAtlas itemsAtlas;
	private static TextureAtlas minimapAtlas;
	private static TextureAtlas padAtlas;
	private static TextureAtlas playerAtlas;
	private static TextureAtlas rabiteAtlas;
	private static TextureAtlas uiAtlas;
	private static TextureAtlas worldMapAtlas;
	
	public static TextureRegion pathMarker;
	public static TextureRegion pathMarkerDisabled;
	public static TextureRegion wall;
	public static TextureRegion fog;
	public static TextureRegion ground;
	public static TextureRegion grass;
	public static TextureRegion entranceDoor;
	public static TextureRegion exitDoor;
	public static TextureRegion commonDoor;
	public static TextureRegion village;
	public static TextureRegion castle;
	public static TextureRegion rock;
	public static TextureRegion pathUp;
	public static TextureRegion pathDown;
	public static TextureRegion pathLeft;
	public static TextureRegion pathRight;
	
	public static TextureRegion hud_menuskin;
	public static TextureRegion ui_msgBox;
	public static TextureRegion hud_heart;
	public static TextureRegion hud_sword;
	public static TextureRegion hud_shield;
	public static TextureRegion hud_coin;
	
	public static TextureRegion menu_open;
	public static TextureRegion menu_close;
	public static TextureRegion menu_torch;
	public static TextureRegion menu_torch_disabled;
	public static TextureRegion menu_move;
	public static TextureRegion menu_move_disabled;
	public static TextureRegion menu_attack;
	public static TextureRegion menu_attack_disabled;
	
	public static TextureRegion cross;
	public static TextureRegion center;
	public static TextureRegion areaUnvisited;
	public static TextureRegion areaVisited;
	public static TextureRegion areaExit;
	public static TextureRegion areaCurrent;
	public static TextureRegion pathUnknownVertical;
	public static TextureRegion pathUnknownHorizontal;
	public static TextureRegion pathExistsVertical;
	public static TextureRegion pathExistsHorizontal;
	public static TextureRegion minimapBackground;
	
	public static Clip action_super_attack_melee_clip;
	public static Clip action_super_attack_dist_clip;
	public static Clip action_super_defend_clip;
	public static Clip action_super_heal_clip;
	public static Clip action_super_loot_clip;
	public static Clip action_super_special_clip;
	public static Clip action_hyper_attack_melee_clip;
	public static Clip action_hyper_attack_dist_clip;
	public static Clip action_hyper_defend_clip;
	public static Clip action_hyper_heal_clip;
	public static Clip action_hyper_loot_clip;
	public static Clip action_hyper_special_clip;
	public static TextureRegion action_attack_melee;
	public static TextureRegion action_attack_dist;
	public static TextureRegion action_heal;
	public static TextureRegion action_loot;
	public static TextureRegion action_defend;
	public static TextureRegion action_special;
	public static TextureRegion action_super_attack_melee;
	public static TextureRegion action_super_attack_dist;
	public static TextureRegion action_super_heal;
	public static TextureRegion action_super_loot;
	public static TextureRegion action_super_defend;
	public static TextureRegion action_super_special;
	public static TextureRegion action_hyper_attack_melee;
	public static TextureRegion action_hyper_attack_dist;
	public static TextureRegion action_hyper_heal;
	public static TextureRegion action_hyper_loot;
	public static TextureRegion action_hyper_defend;
	public static TextureRegion action_hyper_special;
	
	public static TextureRegion attribute_empty;
	public static TextureRegion attribute_strength;
	public static TextureRegion attribute_strength_superh;
	public static TextureRegion attribute_strength_superv;
	public static TextureRegion attribute_strength_superx;
	public static TextureRegion attribute_constitution;
	public static TextureRegion attribute_constitution_superh;
	public static TextureRegion attribute_constitution_superv;
	public static TextureRegion attribute_constitution_superx;
	public static TextureRegion attribute_dexterity;
	public static TextureRegion attribute_dexterity_superh;
	public static TextureRegion attribute_dexterity_superv;
	public static TextureRegion attribute_dexterity_superx;
	public static TextureRegion attribute_focus;
	public static TextureRegion attribute_focus_superh;
	public static TextureRegion attribute_focus_superv;
	public static TextureRegion attribute_focus_superx;
	public static TextureRegion attribute_will;
	public static TextureRegion attribute_will_superh;
	public static TextureRegion attribute_will_superv;
	public static TextureRegion attribute_will_superx;
	public static TextureRegion attribute_luck;
	public static TextureRegion attribute_luck_superh;
	public static TextureRegion attribute_luck_superv;
	public static TextureRegion attribute_luck_superx;
	public static TextureRegion attribute_hyper;
	public static TextureRegion attribute_rabite;
	
	public static TextureRegion player_idle;
	public static TextureRegion rabite_idle;
	
	//public static Animation playerWalkingRightAnimation;

	public static BitmapFont characterFont;
	public static BitmapFont hudFont;

	public static float soundVolume;
	public static Sound[] swordSounds;
	public static Sound[] doorOpenSounds;
	public static Sound stepsSound;
	public static Sound bumpSound;
	public static Sound biteSound;
	public static Sound dieSound;
	public static Sound punchSound;
	public static Sound actionSimpleAttackSound;
	public static Sound actionSimpleDefenseSound;
	public static Sound actionSimpleHealSound;
	public static Sound actionSimpleLootSound;
	
	// Musique de fond, instanciée à la demande
	public static float musicVolume;
	public static String[] dungeonMusics;
	public static String[] menuMusics;
	public static String[] villageMusics;
	public static String[] worldmapMusics;
	public static Music music;
	public static String currentMusic = "";

	private static Map<String, Clip> visualEffectsClips;
	
	public static final float VIRTUAL_WIDTH = 30.0f;
	public static final float VIRTUAL_HEIGHT = 20.0f;
	
	public static float pixelDensity;

	public static boolean asBoolean(Properties properties, String name, boolean fallback) {
		String v = properties.getProperty(name);
		if (v == null) return fallback;
		return Boolean.parseBoolean(v);
	}

	public static float asFloat(Properties properties, String name, float fallback) {
		String v = properties.getProperty(name);
		if (v == null) return fallback;
		return Float.parseFloat(v);
	}

	public static int asInt(Properties properties, String name, int fallback) {
		String v = properties.getProperty(name);
		if (v == null) return fallback;
		return Integer.parseInt(v);
	}

	public static String asString(Properties properties, String name, String fallback) {
		String v = properties.getProperty(name);
		if (v == null) return fallback;
		return v;
	}

	private static float calculatePixelDensity () {
		FileHandle textureDir = Gdx.files.internal("textures");
		FileHandle[] availableDensities = textureDir.list();
		FloatArray densities = new FloatArray();
		for (int i = 0; i < availableDensities.length; i++) {
			try {
				float density = Float.parseFloat(availableDensities[i].name());
				densities.add(density);
			} catch (NumberFormatException ex) {
				// Ignore anything non-numeric, such as ".svn" folders.
			}
		}
		densities.shrink(); // Remove empty slots to get rid of zeroes.
		densities.sort(); // Now the lowest density comes first.
		//DBG DDEreturn CameraHelper.bestDensity(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, densities.items);
		return 24;
	}
	
	private static void createAnimations () {
		//playerWalkingRightAnimation = new Animation(PLAYER_FRAME_DURATION, Assets.playerWalkingRight1, Assets.playerWalkingRight2);
		//robotWalkingLeftAnimation = new Animation(ROBOT_FRAME_DURATION, robotLeft1, robotLeft2, robotLeft3, robotLeft4, robotLeft3,	robotLeft2);
	}
	

	// TODO Faire un cache pour retourner les mêmes Textures et TextureRegions si la même sheet est chargée plusieurs fois
	public static Clip createClip(String spritesFile, int frameCols, int frameRows, float frameDuration) {
		Texture sheet = new Texture(Gdx.files.internal("textures/" + spritesFile));
		return createClip(sheet, frameCols, frameRows, frameDuration);
	}

	public static Clip createClip(String atlasName, String regionName, float frameDuration) {
		TextureAtlas atlas = (TextureAtlas)disposables.get(atlasName);
		if (atlas == null) {
			return null;
		}
		Array<AtlasRegion> regions = atlas.findRegions(regionName);
		if (regions == null) {
			return null;
		}
		TextureRegion[] frames = new TextureRegion[regions.size];
		for (int index = 0; index < regions.size; index++) {
			frames[index] = regions.get(index);
		}
		return new Clip(frameDuration, frames);
	}

	public static Clip createClip(Texture spriteSheet, int frameCols, int frameRows, float frameDuration) {
		TextureRegion[][] tmp = TextureRegion.split(
				spriteSheet,
				spriteSheet.getWidth() / frameCols,
				spriteSheet.getHeight() / frameRows);
		TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
		int index = 0;
		for (int i = 0; i < frameRows; i++) {
			for (int j = 0; j < frameCols; j++) {
				frames[index++] = tmp[i][j];
			}
		}
		return new Clip(frameDuration, frames);
	}

	public static void dispose() {
		for (Disposable disposable : disposables.values()) {
			disposable.dispose();
		}
		disposables.clear();
		music = null;
		currentMusic = "";
	}

	public static Clip getVisualEffectClip(String name) {
		Clip baseClip = visualEffectsClips.get(name);
		if (baseClip == null) {
			return null;
		}
		return new Clip(baseClip);
	}

//	private static Sound[] loadSounds (String dir) {
//		// Sur desktop, files.internal ne sait pas récupérer un répertoire dans les
//		// assets, puisque tout le contenu se retrouve dans le classpath. Du coup
//		// c'est dur d'en parcourir un. Pour contourner ça, on fait un cas particulier
//		// dans le cas desktop pour aller regarder dans bin.
//		FileHandle dirHandle;
//		if (Gdx.app.getType() == ApplicationType.Android) {
//		   dirHandle = Gdx.files.internal("sounds/" + dir);
//		} else {
//		  // ApplicationType.Desktop ..
//		  dirHandle = Gdx.files.internal("./assets/sounds/" + dir);
//		}
//		
//		FileHandle[] fhs = dirHandle.list();
//		System.out.println("Assets.loadSounds() "+fhs.length);
//		List<Sound> sounds = new ArrayList<Sound>();
//		for (int i = 0; i < fhs.length; i++) {
//			String name = fhs[i].name();
//			// DDE On ne filtre pas sur les ogg
//			//if (name.endsWith(".ogg")) {
//				sounds.add(loadSound(dir + "/" + name));
//			//}
//		}
//		Sound[] result = new Sound[0];
//		return sounds.toArray(result);
//	}

	public static void load () {
		disposables = new HashMap<String, Disposable>();
		pixelDensity = calculatePixelDensity();
		loadTextures();
		createAnimations();
		loadFonts();
		loadSounds();
		loadClips();
	}

	private static TextureAtlas loadAtlas(String name) {
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/" + name + ".pack"));
		disposables.put(name, atlas);
		return atlas;
	}

	private static void loadAtlases() {
		actionsAtlas = loadAtlas("actions");
		attributesAtlas = loadAtlas("attributes");
		contextMenuAtlas = loadAtlas("contextmenu");
		dungeonMapAtlas = loadAtlas("dungeonmap");
		hudAtlas = loadAtlas("hud");
		itemsAtlas = loadAtlas("items");
		minimapAtlas = loadAtlas("minimap");
		padAtlas = loadAtlas("pad");
		playerAtlas = loadAtlas("player");
		rabiteAtlas = loadAtlas("rabite");
		uiAtlas = loadAtlas("ui");
		worldMapAtlas = loadAtlas("worldmap");
	}

	public static Clip loadClip(String clipProperties) {
		// Chargement du fichier de propriétés
		Properties properties = new Properties();
		FileHandle fh = Gdx.files.internal("clips/" + clipProperties);
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
		
		// Création du clip
		Clip clip = null;
		if (properties.containsKey("spriteSheet.file")) {
			clip = createClip(
				properties.getProperty("spriteSheet.file"), 
				Integer.parseInt(properties.getProperty("spriteSheet.nbCols")), 
				Integer.parseInt(properties.getProperty("spriteSheet.nbRows")), 
				Float.parseFloat(properties.getProperty("frameDuration")));
		} else {
			clip = createClip(
				properties.getProperty("spriteSheet.atlas"),
				properties.getProperty("spriteSheet.region"),
				Float.parseFloat(properties.getProperty("frameDuration")));
		}
		
		// Initialisation du clip
		clip.alignX = asFloat(properties, "align.x", 0.0f);
		clip.alignY = asFloat(properties, "align.y", 0.0f);
		clip.flipH = asBoolean(properties, "flip.h", false);
		clip.flipV = asBoolean(properties, "flip.v", false);
		clip.offsetX = asFloat(properties, "offset.x", 0.0f);
		clip.offsetY = asFloat(properties, "offset.y", 0.0f);
		clip.scaleX = asFloat(properties, "scale.x", 1.0f);
		clip.scaleY = asFloat(properties, "scale.y", 1.0f);
		clip.stretchX = asBoolean(properties, "stretch.x", false);
		clip.stretchY = asBoolean(properties, "stretch.y", false);
		clip.setPlayMode(asInt(properties, "playMode", Animation.LOOP));
		
		// Chargement des sons à jouer
		for (int frame = 0; frame < clip.getFrameCount(); frame++) {
			String soundFile = asString(properties, "soundOnFrame." + frame, "");
			final Sound sound = (Sound)disposables.get(soundFile);
			if (sound != null) {
				clip.setKeyFrameRunnable(frame, new Runnable(){
					@Override
					public void run() {
						Assets.playSound(sound);
					}
				});
			}
		}
		
		// Chargement des runnables
		// TODO...				
		return clip;
	}
	
	public static void loadClips() {
		visualEffectsClips = new HashMap<String, Clip>();
//		visualEffectsClips.put("simpleAttackMelee", loadClip("simpleAttackMelee.clip"));
		
		action_super_attack_melee_clip = loadClip("action_super_attack_melee.clip");
		action_super_attack_dist_clip = loadClip("action_super_attack_dist.clip");
		action_super_defend_clip = loadClip("action_super_defend.clip");
		action_super_heal_clip = loadClip("action_super_heal.clip");
		action_super_loot_clip = loadClip("action_super_loot.clip");
		action_super_special_clip = loadClip("action_super_special.clip");
		action_hyper_attack_melee_clip = loadClip("action_hyper_attack_melee.clip");
		action_hyper_attack_dist_clip = loadClip("action_hyper_attack_dist.clip");
		action_hyper_defend_clip = loadClip("action_hyper_defend.clip");
		action_hyper_heal_clip = loadClip("action_hyper_heal.clip");
		action_hyper_loot_clip = loadClip("action_hyper_loot.clip");
		action_hyper_special_clip = loadClip("action_hyper_special.clip");
	}
	
	private static BitmapFont loadFont(String subDir, String name, float fontScale) {
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/" + subDir + "/" + name), false);
		font.setScale(fontScale);
		disposables.put(name, font);
		return font;
	}
	
	private static void loadFonts () {
		String fontSubDir = "";// DDE DBG + (int)pixelDensity + "/";
		
		//characterFont.setScale(1.0f / pixelDensity);
		characterFont = loadFont(fontSubDir, TEXT_FONT, 0.4f);
		hudFont = loadFont(fontSubDir, TEXT_FONT, 0.9f);

		
	}
	
	private static Sound loadSound (String filename) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + filename));
		disposables.put(filename, sound);
		return sound;
	}
	
	private static void loadSounds () {
		// Effets sonores
		soundVolume = Config.asFloat("sounds.sfx.volume", 1.0f);
		swordSounds = new Sound[]{
			loadSound("sword/sword-01.ogg"),
			loadSound("sword/sword-02.ogg"),
			loadSound("sword/sword-03.ogg"),
		};
		doorOpenSounds = new Sound[]{
			loadSound("door/door_open-01.ogg"),
			loadSound("door/door_open-02.ogg"),
			loadSound("door/door_open-03.ogg"),
			loadSound("door/door_open-04.ogg")
		};
		stepsSound = loadSound("steps.ogg");
		bumpSound = loadSound("player/bump.ogg");
		biteSound = loadSound("rabite/bite.ogg");
		dieSound = loadSound("rabite/die.ogg");
		punchSound = loadSound("player/punch.ogg");
		
		actionSimpleAttackSound = loadSound("actions/simple_attack.ogg");
		actionSimpleDefenseSound = loadSound("actions/simple_defense.ogg");
		actionSimpleHealSound = loadSound("actions/simple_heal.ogg");
		actionSimpleLootSound = loadSound("actions/simple_loot.ogg");
		
		// Musiques
		musicVolume = Config.asFloat("sounds.music.volume", 0.5f);
		dungeonMusics = new String[]{
			"dungeon/8bit Dungeon Boss.ogg",
			"dungeon/8bit Dungeon Level.ogg",
			"dungeon/Video Dungeon Boss.ogg",
			"dungeon/Video Dungeon Crawl.ogg"
		};
		menuMusics = new String[]{
				"menu/Home Base Groove.ogg"
			};
		villageMusics = new String[]{
				"village/Mellowtron.ogg"
			};
		worldmapMusics = new String[]{
				"worldmap/Ambler.ogg",
				"worldmap/Jaunty Gumption.ogg",
				"worldmap/Move Forward.ogg"
			};
	}

	private static TextureRegion loadTexture(String file) {
		Texture texture = new Texture(Gdx.files.internal("textures/" + file));
		TextureRegion region = new TextureRegion(texture);
		disposables.put(file, texture);
		return region;
	}

	private static TextureRegion loadTexture(TextureAtlas atlas, String regionName) {
		AtlasRegion region = atlas.findRegion(regionName);
		if (region == null) {
			throw new IllegalArgumentException("La région " + regionName + " est introuvable dans l'atlas.");
		}
		return region;
	}
	
	private static TextureRegion loadTexture(TextureAtlas atlas, String regionName, int index) {
		AtlasRegion region = atlas.findRegion(regionName, index);
		if (region == null) {
			throw new IllegalArgumentException("La région " + regionName + " ou son index " + index + " est introuvable dans l'atlas.");
		}
		return region;
	}

	private static void loadTextures () {
		loadAtlases();
		
		// Atlas actions
		action_attack_melee = loadTexture(actionsAtlas, "action_attack_melee");
		action_attack_dist = loadTexture(actionsAtlas, "action_attack_dist");
		action_heal = loadTexture(actionsAtlas, "action_heal");
		action_loot = loadTexture(actionsAtlas, "action_loot");
		action_defend = loadTexture(actionsAtlas, "action_defend");
		action_special = loadTexture(actionsAtlas, "action_special");
		action_super_attack_melee = loadTexture(actionsAtlas, "action_super_attack_melee");
		action_super_attack_dist = loadTexture(actionsAtlas, "action_super_attack_dist");
		action_super_heal = loadTexture(actionsAtlas, "action_super_heal");
		action_super_loot = loadTexture(actionsAtlas, "action_super_loot");
		action_super_defend = loadTexture(actionsAtlas, "action_super_defend");
		action_super_special = loadTexture(actionsAtlas, "action_super_special");
		action_hyper_attack_melee = loadTexture(actionsAtlas, "action_hyper_attack_melee");
		action_hyper_attack_dist = loadTexture(actionsAtlas, "action_hyper_attack_dist");
		action_hyper_heal = loadTexture(actionsAtlas, "action_hyper_heal");
		action_hyper_loot = loadTexture(actionsAtlas, "action_hyper_loot");
		action_hyper_defend = loadTexture(actionsAtlas, "action_hyper_defend");
		action_hyper_special = loadTexture(actionsAtlas, "action_hyper_special");
		
		// Atlas attributes
		attribute_empty = loadTexture(attributesAtlas, "empty");
		attribute_strength = loadTexture(attributesAtlas, "strength");
		attribute_constitution = loadTexture(attributesAtlas, "constitution");
		attribute_dexterity = loadTexture(attributesAtlas, "dexterity");
		attribute_focus = loadTexture(attributesAtlas, "focus");
		attribute_will = loadTexture(attributesAtlas, "will");
		attribute_luck = loadTexture(attributesAtlas, "luck");
		
		attribute_strength_superh = loadTexture(attributesAtlas, "strength_superh");
		attribute_constitution_superh = loadTexture(attributesAtlas, "constitution_superh");
		attribute_dexterity_superh = loadTexture(attributesAtlas, "dexterity_superh");
		attribute_focus_superh = loadTexture(attributesAtlas, "focus_superh");
		attribute_will_superh = loadTexture(attributesAtlas, "will_superh");
		attribute_luck_superh = loadTexture(attributesAtlas, "luck_superh");
														
		attribute_strength_superv = loadTexture(attributesAtlas, "strength_superv");
		attribute_constitution_superv = loadTexture(attributesAtlas, "constitution_superv");
		attribute_dexterity_superv = loadTexture(attributesAtlas, "dexterity_superv");
		attribute_focus_superv = loadTexture(attributesAtlas, "focus_superv");
		attribute_will_superv = loadTexture(attributesAtlas, "will_superv");
		attribute_luck_superv = loadTexture(attributesAtlas, "luck_superv");
		
		attribute_strength_superx = loadTexture(attributesAtlas, "strength_superx");
		attribute_constitution_superx = loadTexture(attributesAtlas, "constitution_superx");
		attribute_dexterity_superx = loadTexture(attributesAtlas, "dexterity_superx");
		attribute_focus_superx = loadTexture(attributesAtlas, "focus_superx");
		attribute_will_superx = loadTexture(attributesAtlas, "will_superx");
		attribute_luck_superx = loadTexture(attributesAtlas, "luck_superx");
		
		attribute_hyper = loadTexture(attributesAtlas, "hyper");
		
		// Atlas contextmenu
		// TODO Refaire l'atlas pour supprimer le préfixe menu_
		menu_open = loadTexture(contextMenuAtlas, "menu_open");
		menu_close = loadTexture(contextMenuAtlas, "menu_close");
		menu_torch = loadTexture(contextMenuAtlas, "menu_torch");
		menu_torch_disabled = loadTexture(contextMenuAtlas, "menu_torch_disabled");
		menu_move = loadTexture(contextMenuAtlas, "menu_move");
		menu_move_disabled = loadTexture(contextMenuAtlas, "menu_move_disabled");
		menu_attack = loadTexture(contextMenuAtlas, "menu_attack");
		menu_attack_disabled = loadTexture(contextMenuAtlas, "menu_attack_disabled");
		
		// Atlas dungeonmap
		fog = loadTexture(dungeonMapAtlas, "fog");
		wall = loadTexture(dungeonMapAtlas, "wall");
		ground = loadTexture(dungeonMapAtlas, "ground");
		entranceDoor = loadTexture(dungeonMapAtlas, "browndoor_in", 0);
		exitDoor = loadTexture(dungeonMapAtlas, "browndoor_out", 3);
		commonDoor = loadTexture(dungeonMapAtlas, "darkdoor_in", 0);
		
		// Atlas hud
		hud_heart = loadTexture(hudAtlas, "heart");
		hud_sword = loadTexture(hudAtlas, "sword");
		hud_shield = loadTexture(hudAtlas, "shield");
		hud_coin = loadTexture(hudAtlas, "coin");
		
		// Atlas minimap
		areaUnvisited = loadTexture(minimapAtlas, "area_unvisited");
		areaVisited = loadTexture(minimapAtlas, "area_visited");
		areaExit = loadTexture(minimapAtlas, "area_exit");
		areaCurrent = loadTexture(minimapAtlas, "area_current");
		pathUnknownVertical = loadTexture(minimapAtlas, "path-unknown_vertical");
		pathUnknownHorizontal = loadTexture(minimapAtlas, "path-unknown_horizontal");
		pathExistsVertical = loadTexture(minimapAtlas, "path-exists_vertical");
		pathExistsHorizontal = loadTexture(minimapAtlas, "path-exists_horizontal");
		minimapBackground = loadTexture(minimapAtlas, "background");
		
		// Atlas pad
		cross = loadTexture(padAtlas, "cross");
		center = loadTexture(padAtlas, "center");
		
		// Atlas player
		
		// Atlas rabite
		hud_menuskin = loadTexture(rabiteAtlas, "menuskin");
		attribute_rabite = loadTexture(rabiteAtlas, "rabite-idle", 0);
		
		// Atlas ui
		ui_msgBox = loadTexture(uiAtlas, "msgBox");
		
		// Atlas worldmap
		pathMarker = loadTexture(worldMapAtlas, "path-marker");
		pathMarkerDisabled = loadTexture(worldMapAtlas, "path-marker_disabled");
		grass = loadTexture(worldMapAtlas, "grass");
		village = loadTexture(worldMapAtlas, "village");
		castle = loadTexture(worldMapAtlas, "castle");
		rock = loadTexture(worldMapAtlas, "rock");
		pathUp = loadTexture(worldMapAtlas, "path_up");
		pathDown = loadTexture(worldMapAtlas, "path_down");
		pathLeft = loadTexture(worldMapAtlas, "path_left");
		pathRight = loadTexture(worldMapAtlas, "path_right");
	}
	
	public static boolean playMusic(String file) {
		// Si on cherche à jouer la même musique, rien à faire à part
		// s'assurer qu'elle est bien en train de tourner
		if (currentMusic.equals(file)) {
			if (!music.isPlaying()) {
				music.play();
			}
			return true;
		}
		
		
		// Arrêt de la musique en cours
		stopMusic();
		
		// S'il n'y a aucune musique à jouer, on ne joue rien
		if (file == null || file.isEmpty()) {
			return false;
		}
		
		// Lancement de la musque
		music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/" + file));
		music.setLooping(true);
		music.setVolume(musicVolume);
		music.play();
		currentMusic = file;
		
		// On s'assure que la musique sera libérée
		disposables.put(file, music);
		return true;
	}
	
	public static void playSound(Sound sound) {
		if (sound == null) {
			return;
		}
		sound.play(soundVolume);
	}

	public static void stopMusic() {
		if (music != null && music.isPlaying()) {
			music.stop();
		}
	}

	private static float toHeight (TextureRegion region) {
		return region.getRegionHeight() / pixelDensity;
	}
	
	private static float toWidth (TextureRegion region) {
		return region.getRegionWidth() / pixelDensity;
	}

	public static Sound getSound(String file) {
		return (Sound)disposables.get(file);
	}
	
	public static TextureAtlas getAtlas(String name) {
		return (TextureAtlas)disposables.get(name);
	}
}
