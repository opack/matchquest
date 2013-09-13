package com.slamdunk.utils.dbg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class ControllerLogic {

	boolean wasPressed;
	Controller controller;

	final Vector2 pressedPosition = new Vector2();
	final Vector2 currentPosition = new Vector2();
	final Vector2 tmp = new Vector2();

	public ControllerLogic(Controller controller, Vector2 slingshotPosition) {
		this.controller = controller;
		wasPressed = false;
		this.pressedPosition.set(slingshotPosition);
	}

	public void update(float delta) {

		if (Gdx.input.isTouched()) {

			if (!wasPressed) {
				// pressedPosition.set(Gdx.input.getX(),
				// Gdx.graphics.getHeight() - Gdx.input.getY());
				wasPressed = true;
				controller.charging = true;
			}

			currentPosition.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

			tmp.set(currentPosition).sub(pressedPosition);
			tmp.mul(-1f);

			controller.angle = tmp.angle();
			controller.power = tmp.len();
			System.out.println("ControllerLogic.update() angle=" + controller.angle + " / pwr=" + controller.power);

		} else {

			if (wasPressed) {
				// shoot
				controller.charging = false;
				wasPressed = false;
			}

		}

	}

}