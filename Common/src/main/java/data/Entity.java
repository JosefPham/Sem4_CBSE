/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import Interfaces.IEntityMovement;
import java.awt.Image;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Peter
 */
public class Entity {
    
    
    private final UUID ID = UUID.randomUUID();



    private float positionX, positionY, positionRadians;
    private float[] shapeX, shapeY;
    private Image[] sprite;
    private Map<Class, IEntityMovement> movingParts;
    
    
    public Entity(){
        movingParts = new ConcurrentHashMap<>();
    }
    
    public void addMovement(IEntityMovement movement){
        movingParts.put(movement.getClass(), movement);
    }
    
    public <E extends IEntityMovement> E getMovement(Class movementClass){
        return (E) movingParts.get(movementClass);
    }
    
    
    public float[] getShapeX() {
        return shapeX;
    }

    public void setShapeX(float[] shapeX) {
        this.shapeX = shapeX;
    }

    public float[] getShapeY() {
        return shapeY;
    }

    public void setShapeY(float[] shapeY) {
        this.shapeY = shapeY;
    }

    public Image[] getSprite() {
        return sprite;
    }

    public void setSprite(Image[] sprite) {
        this.sprite = sprite;
    }
    
    public String getID(){
        return ID.toString();
    }
    
        public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public float getPositionRadians() {
        return positionRadians;
    }

    public void setPositionRadians(float positionRadians) {
        this.positionRadians = positionRadians;
    }
    
   
    
}
