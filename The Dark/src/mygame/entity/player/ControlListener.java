/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.app.state.AppStateManager;
import mygame.GameManager;
import mygame.util.InteractionManager;

/**
 *
 * @author Bawb
 */
public class ControlListener {
    
    private InteractionManager im;
    private Player             player;
    private Long               lastChecked;
    private boolean            isDown;
    
    public ControlListener(AppStateManager stateManager, Player player) {
        im          = stateManager.getState(GameManager.class).getUtilityManager().getInteractionManager();
        this.player = player;
        lastChecked = 0L;
    }
    
    private void updateKeys() {
    
        Long cooldown = System.currentTimeMillis() / 1000 - lastChecked / 1000;
        
        if(cooldown > 2) {
            
            if (im.getIsPressed("Interact")) {
                isDown = true;
            }
            
            else if (isDown) {
                player.setHasChecked(isDown);
                lastChecked = System.currentTimeMillis();
                isDown      = false;
            }
            
        }
            
    }
    
    public void update() {
        updateKeys();
    }
    
}
