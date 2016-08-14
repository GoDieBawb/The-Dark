/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.scene;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.Matrix3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author root
 */
public class RoomCreator extends AbstractAppState {
    
    private SimpleApplication app;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        
        try {
            createRoom();
        }
        
        catch (Exception e) {
            System.out.println("Error is: " + e); 
        }
        
    }
    
    private void createRoom() {
       
        Node     rootNode  = app.getRootNode();
        Node     sceneNode = (Node) rootNode.getChild("Scene Node");
        Node     room      = new Node();
        Node     floor     = new Node();
        Box      floorBox  = new Box(5, .2f, 5);
        Geometry floorGeom = new Geometry("Floor", floorBox);
        Material floorMat  = app.getAssetManager().loadMaterial("Materials/InteriorFloor.j3m");
        
        floor.attachChild(floorGeom);
        floor.setMaterial(floorMat);
        floor.setName("Floor");
        
        for (int i = 0; i < 4; i++) {
            
            Node wall;
            
            switch(i) {
            
                case 0:
                    wall = createWall(false);
                    wall.setLocalTranslation(0,0,5);
                    break;
                    
                case 1:
                    wall = createWall(false);
                    wall.setLocalTranslation(0,0,-5);
                    break;
                    
                case 2:
                    wall = createWall(true);
                    wall.setLocalTranslation(5, 0, 0);
                    break;
                    
                default:
                    wall = createWall(true);
                    wall.setLocalTranslation(-5, 0, 0);
                    break;  
            
            }
            
            room.attachChild(wall);
            
        }
        
        room.attachChild(floor); 
        room.setName("Room");
        room.scale(.7f, .25f, .7f);
        sceneNode.attachChild(room);
        
    }
    
    private Node createWall(boolean isRight) {
        
        float width;
        float length;
        
        if (isRight) {
            width  = .2f;
            length = 5f;
        }
        
        else {
            width  = 5f;
            length = .2f;
        }
        
        Node     wall      = new Node();      
        Box      wallBox   = new Box(width, 5, length); 
        Geometry wallGeom  = new Geometry("Wall", wallBox);
        Material wallMat   = app.getAssetManager().loadMaterial("Materials/InteriorWall.j3m");
        
        wall.attachChild(wallGeom);
        wall.setMaterial(wallMat);
        wall.setName("Wall");
        
        return wall;
        
    }
    
}
