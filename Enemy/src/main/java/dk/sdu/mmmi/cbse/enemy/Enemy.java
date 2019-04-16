package dk.sdu.mmmi.cbse.enemy;

import data.Entity;
import data.GameData;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import Interfaces.ICombatEntity;

/**
 *
 * @author Sigurd E. Espersen
 */
public class Enemy extends Entity implements ICombatEntity{

    private boolean up;
    private float speed;
    
    private boolean isShooting;

    private boolean dead = false;

    private int life;
    
    private boolean hit = false;

    public void setIsShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setIsHit(boolean isHit) {
        this.hit = isHit;
    }


    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }
    
    @Override
    public int getLife() {
        return life;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public boolean isHit() {
        return hit;
    }

    @Override
    public boolean isShooting() {
        return isShooting;
    }

    @Override
    public void execute(GameData gameData, Entity entity) {
        float radians = entity.getPositionRadians();
        float dt = gameData.getDelta();

        if (up) {
            entity.setPositionX((float) ((float) entity.getPositionX() + cos(radians) * speed * dt));
            entity.setPositionY((float) ((float) entity.getPositionY() + sin(radians + 1) * speed * dt));
        }
        
        // Speed
        if (up ) {
            entity.setPositionX((float) (cos(entity.getPositionRadians()) * speed * dt + entity.getPositionX()));
            entity.setPositionY((float) (sin(entity.getPositionRadians()) * speed * dt + entity.getPositionY()));
        }
    }


}
