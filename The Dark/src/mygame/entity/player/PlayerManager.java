/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import mygame.GameManager;

/**
 *
 * @author Bawb
 */
public class PlayerManager {
    
    private final SimpleApplication app;
    private Player                  player;
    
    //On construct set field and create player
    public PlayerManager(SimpleApplication app) {
        this.app = app;
        createPlayer();
    }
    
    //Construct a new player object
    private void createPlayer() {
        player = new Player(app.getStateManager());
        //player.createChaseControl();
        player.createCameraManager();
        player.createPhys();
    }
    
    //Places the player in the scene.
    public void placePlayer() {
        GameManager gm = app.getStateManager().getState(GameManager.class);
        gm.getSceneManager().getScene().attachChild(player);
        gm.getSceneManager().getScene().attachChild(player.getCameraManager().getCameraNode());
        gm.getUtilityManager().getPhysicsManager().getPhysics().getPhysicsSpace().add(player.getPhys());
        gm.getUtilityManager().getPhysicsManager().getPhysics().getPhysicsSpace().setGravity(new Vector3f(0,-9.8f,0));
        
        //If there is a start spot warp the character control there
        if(gm.getSceneManager().getScene().getChild("StartSpot") != null)
            player.getPhys().warp(gm.getSceneManager().getScene().getChild("StartSpot").getWorldTranslation());
        
        //If not start in the center of the scene
        else
            player.getPhys().warp(new Vector3f(0,0,0));
        
    }
    
    //Returns the Player
    public Player getPlayer() {
        return player;
    }    
    
    //Updates The Camera and Control Listeners
    public void update(float tpf) {
        //player.getChaseControl().update(tpf);
        player.getCameraManager().update(tpf);
        player.getControlListener().update();
        
    }
    
}
