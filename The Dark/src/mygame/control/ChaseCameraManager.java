/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package mygame.control;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import mygame.entity.player.Player;

/**
*
* @author Bob
*/
public class ChaseCameraManager {

    private SimpleApplication app;
    private Player            player;
    private ChaseCamera       cam;
    private boolean           enabled;
    private boolean           isClose;
  
    public ChaseCameraManager(Application app, Player player){
        this.app           = (SimpleApplication) app;
        this.player        = player;
        initCamera();
    }
  
    //Creates camera
     private void initCamera(){
        //Creates a new chase cam and attached it to the player.model for the game
        cam = new ChaseCamera(this.app.getCamera(), player, this.app.getInputManager());
        cam.setMinDistance(0.5f);
        cam.setMaxDistance(3);
        cam.setDefaultDistance(3);
        cam.setDragToRotate(false);
        cam.setDownRotateOnCloseViewOnly(false);
        cam.setRotationSpeed(5f);
        cam.setLookAtOffset(player.getLocalTranslation().add(0, .8f, 0));
        cam.setDefaultVerticalRotation(.145f);
        cam.setMaxVerticalRotation(.145f);
        cam.setMinVerticalRotation(.145f);
        app.getInputManager().setCursorVisible(false);
        app.getCamera().setFrustumNear(.75f);
    }

    private void chaseCamMove() {
        
        if (cam.getDistanceToTarget() < 1) {
            cam.setMinVerticalRotation(0f);
            cam.setMaxVerticalRotation(5f);
        }
        
        else {
            cam.setMaxVerticalRotation(.145f);
            cam.setMinVerticalRotation(.145f); 
        }
        
    }
    
    public ChaseCamera getChaseCam() {
        return cam;
    }
    
    public void setEnabled(boolean newVal) {
        enabled = newVal;
        cam.setEnabled(newVal);
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void update(float tpf) {
        
        chaseCamMove();
        
        if(cam.getDistanceToTarget() < 1) {
            
            if(isClose)
                return;
            
            player.getModel().removeFromParent();
            isClose = true;
        }
    
        else if(isClose) {
            player.attachChild(player.getModel());
            isClose = false;
        }
        
    }
  
  }