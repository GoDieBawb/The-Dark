/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.control;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import mygame.GameManager;
import mygame.entity.player.Player;
import mygame.util.InteractionManager;

/**
 *
 * @author Bawb
 */
public class ChaseControl extends InteractionControl {
    
    private boolean                  left, right, up, down, cursor;
    private final AppStateManager    stateManager;
    private final Player             player;
    private final SimpleApplication  app;
    private final Vector3f           camDir = new Vector3f(), camLeft = new Vector3f(), walkDirection = new Vector3f();
    private ChaseCameraManager       cameraManager;
    private boolean                  enabled;
    
    public ChaseControl(AppStateManager stateManager, Player player) {
        this.stateManager = stateManager;
        this.player       = player;
        app               = (SimpleApplication) stateManager.getApplication();
        createCameraManager();
    } 
    
    //Creates the Camera Manager
    private void createCameraManager() {
        cameraManager = new ChaseCameraManager(app, player);
    }
    
    //Updates which keys are pressed
    private void updateKeys() {
        InteractionManager im = stateManager.getState(GameManager.class).getUtilityManager().getInteractionManager();
        up     = im.getIsPressed("Up");
        down   = im.getIsPressed("Down");
        left   = im.getIsPressed("Left");
        right  = im.getIsPressed("Right");
        cursor = im.getIsPressed("Cursor");
    }
    
    //Actually moves the player based on the keys pressed
    private void chaseMove(float tpf){
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
    
    public ChaseCameraManager getCameraManager() {
        return cameraManager;
    }
    
    @Override
    public void setEnabled(boolean newVal) {
        super.setEnabled(newVal);
        cameraManager.setEnabled(newVal);
    }
    
    //If e is held down keep the cursor visible
    private void checkCursor() {
        
        if(cursor) {
            app.getInputManager().setCursorVisible(true);
            cameraManager.getChaseCam().setDragToRotate(true);
        }
            
        else {
            app.getInputManager().setCursorVisible(false);
            cameraManager.getChaseCam().setDragToRotate(false);
        }
        
    }
    
    //Updates proper methods on the loop
    @Override
    public void update(float tpf) {
        chaseMove(tpf);
        updateKeys();
        checkCursor();
        cameraManager.update(tpf);
    }
    
}
