package mygame.control;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.entity.player.Player;
import mygame.util.InteractionManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author root
 */
public class CameraManager {
    
    private boolean                 left, right, up, down, cursor;
    private final Vector3f          camDir = new Vector3f(), camLeft = new Vector3f(), walkDirection = new Vector3f();
    private final SimpleApplication app;
    private final Player            player;
    private final AppStateManager   stateManager;   
    private final Camera            cam;
    private final Node              camNode;
    private       boolean           isEnabled;
    
    public CameraManager(SimpleApplication app, Player player) {
        this.app        = app;
        stateManager    = app.getStateManager();
        this.player     = player;
        cam             = app.getCamera();
        camNode         = new Node();
        isEnabled       = true;
        initCamera();
    }
    
    public void setEnabled(boolean newVal) {
        isEnabled = newVal;
    }
    
    public Node getCameraNode() {
        return camNode;
    }
    
    public void clearCameraNode() {
        camNode.detachAllChildren();
    }
    
    private void initCamera() {
        float scale = .5f;
        app.getCamera().setFrustumNear(app.getCamera().getFrustumNear()*scale);
        app.getCamera().setFrustumLeft(app.getCamera().getFrustumLeft()*scale);
        app.getCamera().setFrustumRight(app.getCamera().getFrustumRight()*scale);
        app.getCamera().setFrustumTop(app.getCamera().getFrustumTop()*scale);
        app.getCamera().setFrustumBottom(app.getCamera().getFrustumBottom()*scale);    
        
    }
    
     private void updateKeys() {
        InteractionManager im = stateManager.getState(GameManager.class).getUtilityManager().getInteractionManager();
        up     = im.getIsPressed("Up");
        down   = im.getIsPressed("Down");
        left   = im.getIsPressed("Left");
        right  = im.getIsPressed("Right");
        cursor = im.getIsPressed("Cursor");
    }
    
    //Actually moves the player based on the keys pressed
    private void movePlayer(float tpf){
        
        camDir.set(app.getCamera().getDirection()).multLocal(10.0f, 0.0f, 10.0f);
        camLeft.set(app.getCamera().getLeft()).multLocal(10.0f);
        walkDirection.set(0, 0, 0);
        
        if (up) {
            walkDirection.addLocal(camDir);
            player.run();
        }
        
        else if (down) {
            walkDirection.addLocal(camDir.negate());
            player.run();
        }
        
        if (left) {
            walkDirection.addLocal(camLeft);
            player.run();
        }
        
        else if (right) {
            walkDirection.addLocal(camLeft.negate());
            player.run();
        }
        
        else if (!up && !down) {
            player.idle();
        }
        
        float speedMult;
        speedMult = player.getSpeedMult()*.3f;
        
        player.getPhys().setWalkDirection(walkDirection.mult(speedMult));
        player.getPhys().setViewDirection(camDir);
        
    }    
    
    public void update(float tpf) {
        
        if (!isEnabled)
            return;
        
        updateKeys();
        movePlayer(tpf);
        
        Vector3f camLoc    = app.getCamera().getLocation();
        Vector3f camSpot   = player.getWorldTranslation().add(camDir.negate().normalize().multLocal(.5f,0,.5f).add(0,.85f,0));
        
        camNode.setLocalTranslation(camSpot);
        
        if (player.hasRight()) {
            
            boolean isActing = player.getRightEquip().isActing();  

            if (!isActing) {
                player.getRightEquip().setLocalTranslation(cam.getDirection().normalize().add(camLeft.normalize().negate().mult(.3f)).add(0,-.25f,0));
                player.getRightEquip().lookAt(cam.getDirection().normalize().multLocal(1,1,1).mult(500).negate(), new Vector3f(0,1,0));
            }
            
        }
        
        if (player.hasLeft()) {
            
            boolean isActing = player.getLeftEquip().isActing();  
            
            if (!isActing) {
                player.getLeftEquip().setLocalTranslation(cam.getDirection().normalize().add(camLeft.normalize().mult(.3f)).add(0,-.25f,0));
                player.getLeftEquip().lookAt(cam.getDirection().normalize().multLocal(1,1,1).mult(500), new Vector3f(0,1,0));
            }
            
        }
        
        cam.setLocation(camNode.getWorldTranslation());
        
    }
    
}
