package dk.sdu.mmmi.cbse.player;

import data.Entity;
import data.GameData;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import Interfaces.ICombatEntity;

/**
 *
 * @author Gruppe 11
 */
public class Player extends Entity implements ICombatEntity {

    private boolean left, right, up, down;

    private float speed = 20;

    private boolean isShooting;

    private boolean dead = false;

    private int life;

    private boolean isHit = false;

    @Override
    public boolean isDead() {
        return dead;
    }
    
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public int getLife() {
        return life;
    }

    @Override
    public void setLife(int life) {
             if(life <= 0){
            dead = true;
        }
        this.life = life;
    }

    @Override
    public boolean isHit() {
        return isHit;
    }

    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
    }

    @Override
    public boolean isShooting() {
        return isShooting;
    }

    @Override
    public void setShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void execute(GameData gameData, Entity entity) {
        float dt = gameData.getDelta();

        // skal ind i en collision klasse
        if (entity.getPositionX() >= (gameData.getDisplayWidth() - 15)) {
            entity.setPositionX(gameData.getDisplayWidth() - 15);
        }

        if (entity.getPositionY() >= (gameData.getDisplayHeight() - 15)) {
            entity.setPositionY(gameData.getDisplayHeight() - 15);
        }

        if (entity.getPositionX() <= 15) {
            entity.setPositionX(15);
        }

        if (entity.getPositionY() <= (15)) {
            entity.setPositionY(15);
        }

        // Speed
        if (up) {
            entity.setPositionX((float) (cos(entity.getRadians()) * speed * dt + entity.getPositionX()));
            entity.setPositionY((float) (sin(entity.getRadians()) * speed * dt + entity.getPositionY()));
        }

        if (down) {
            entity.setPositionX((float) (cos(entity.getRadians()) * -speed * dt + entity.getPositionX()));
            entity.setPositionY((float) (sin(entity.getRadians()) * -speed * dt + entity.getPositionY()));
        }

        if (isHit) {
            life = - 1;
            isHit = false;
        }

        if (life <= 0) {
            dead = true;
        }
    }
}
