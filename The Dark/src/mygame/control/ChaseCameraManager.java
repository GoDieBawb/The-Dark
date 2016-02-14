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
        cam.setDefaultHorizontalRotation(2.7f);
        cam.setMinDistance(0.5f);
        cam.setMaxDistance(.5f);
        cam.setDefaultDistance(.5f);
        cam.setDragToRotate(false);
        cam.setDownRotateOnCloseViewOnly(false);
        cam.setRotationSpeed(2f);
        cam.setLookAtOffset(player.getLocalTranslation().add(0, .85f, 0));
        cam.setDefaultVerticalRotation(3f);
        cam.setMaxVerticalRotation(4f);
        cam.setMinVerticalRotation(2f);
        app.getInputManager().setCursorVisible(false);
        float scale = .5f;
        app.getCamera().setFrustumNear(app.getCamera().getFrustumNear()*scale);
        app.getCamera().setFrustumLeft(app.getCamera().getFrustumLeft()*scale);
        app.getCamera().setFrustumRight(app.getCamera().getFrustumRight()*scale);
        app.getCamera().setFrustumTop(app.getCamera().getFrustumTop()*scale);
        app.getCamera().setFrustumBottom(app.getCamera().getFrustumBottom()*scale);
    }

    //Run on a loop to maintain backwards view
    private void chaseCamMove() {
        
        if (cam.getDistanceToTarget() < 1) {
            cam.setMinVerticalRotation(2f);
            cam.setMaxVerticalRotation(4f);
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
    
    //Update Loop
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