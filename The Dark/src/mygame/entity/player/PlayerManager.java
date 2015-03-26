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
        app.getRootNode().attachChild(player);
        gm.getUtilityManager().getPhysicsManager().getPhysics().getPhysicsSpace().add(player.getPhys());
        gm.getUtilityManager().getPhysicsManager().getPhysics().getPhysicsSpace().setGravity(new Vector3f(0,-50,0));
        player.getPhys().warp(gm.getSceneManager().getScene().getChild("StartSpot").getWorldTranslation());
    }
    
    public Player getPlayer() {
        return player;
    }    
    
    public void update(float tpf) {
        player.getChaseControl().update(tpf);
    }
    
}
