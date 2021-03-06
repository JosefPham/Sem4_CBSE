package dk.sdu.mmmi.cbse.weapon;

import Interfaces.ICombatEntity;
import data.Entity;
import data.GameData;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author Gruppe 11
 */
public class Weapon extends Entity implements ICombatEntity {

    private boolean shoot;
    private float speed;
    private boolean up;
    private boolean dead = false;
    private long timeAlive;
    private int life;

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isShoot() {
        return shoot;
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    public Weapon(boolean up) {
        this.up = up;
        timeAlive = System.currentTimeMillis();
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void execute(GameData gd, Entity entity) {
        float dt = gd.getDelta();
        if (up) {
            entity.setPositionX((float) (cos(entity.getRadians()) * speed * dt + (entity.getPositionX())));
            entity.setPositionY((float) (sin(entity.getRadians()) * speed * dt + (entity.getPositionY())));
        }
        if ((timeAlive + 2000) < System.currentTimeMillis()) {
            dead = true;
        }
    }

    @Override
    public int getLife() {
        return 0;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public boolean isHit() {
        System.out.println("Dead");
        return true;
    }

    @Override
    public boolean isShooting() {
        System.out.println("Not to be used");
        return false;
    }

    @Override
    public void setShooting(boolean b) {
        shoot = b;
    }

    @Override
    public void setLife(int life) {
             if(life <= 0){
            dead = true;
        }
        this.life = life;
    }

}
