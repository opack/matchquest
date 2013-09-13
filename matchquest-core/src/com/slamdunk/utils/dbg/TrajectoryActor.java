package com.slamdunk.utils.dbg;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TrajectoryActor extends Actor {  
  
    private Controller controller;  
    private ProjectileEquation projectileEquation;  
    private Sprite trajectorySprite;  
  
    public int trajectoryPointCount = 20;  
    public float timeSeparation = 1f;  
  
    public TrajectoryActor(Controller controller, float gravity, Sprite trajectorySprite) {  
        this.controller = controller;  
        this.trajectorySprite = trajectorySprite;  
        this.projectileEquation = new ProjectileEquation();  
        this.projectileEquation.gravity = gravity;  
    }  
  
    @Override  
    public void act(float delta) {  
        super.act(delta);  
        projectileEquation.startVelocity.set(controller.power, 0f);  
        projectileEquation.startVelocity.rotate(controller.angle);  
    }  
  
    @Override  
    public void draw(SpriteBatch batch, float parentAlpha) {  
        float t = 0f;  
        float width = getWidth();  
        float height = getHeight();  
  
        float timeSeparation = this.timeSeparation;  
          
        for (int i = 0; i < trajectoryPointCount; i++) {  
            float x = getX() + projectileEquation.getX(t);  
            float y = getY() + projectileEquation.getY(t);  
  
            batch.setColor(getColor());  
            batch.draw(trajectorySprite, x, y, width, height);  
  
            t += timeSeparation;  
        }  
    }  
  
    @Override  
    public Actor hit(float x, float y, boolean touchable) {  
        return null;  
    }  
  
}  