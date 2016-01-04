/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import mygame.GameManager;

/**
 *
 * @author Bawb
 */
public class PlayerManager {
    
    private SimpleApplication app;
    private Player            player;
    
    public PlayerManager(SimpleApplication app) {
        this.app = app;
        createPlayer();
    }
    
    private void createPlayer() {
        player = new Player(app.getStateManager());
        player.createChaseControl();
        player.createPhys();
    }
    
    public void placePlayer() {
        GameManager gm = app.getStateManager().getState(GameManager.class);
        gm.getSceneManager().getScene().attachChild(player);
        gm.getUtilityManager().getPhysicsManager().getPhysics().getPhysicsSpace().add(player.getPhys());
        gm.getUtilityManager().getPhysicsManager().getPhysics().getPhysicsSpace().setGravity(new Vector3f(0,-9.8f,0));
        
        if(gm.getSceneManager().getScene().getChild("StartSpot") != null)
            player.getPhys().warp(gm.getSceneManager().getScene().getChild("StartSpot").getWorldTranslation());
        
        else
            player.getPhys().warp(new Vector3f(0,0,0));
        
    }
    
    public Player getPlayer() {
        return player;
    }    
    
    public void update(float tpf) {
        player.getChaseControl().update(tpf);
        player.getControlListener().update();
    }
    
}
