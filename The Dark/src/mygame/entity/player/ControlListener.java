/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.app.state.AppStateManager;
import mygame.GameManager;
import mygame.entity.item.Gun;
import mygame.entity.item.Torch;
import mygame.util.InteractionManager;

/**
 *
 * @author Bawb
 */
public class ControlListener {
    
    private InteractionManager im;
    private Player             player;
    private boolean            interact, torch, shoot;
    
    public ControlListener(AppStateManager stateManager, Player player) {
        im          = stateManager.getState(GameManager.class).getUtilityManager().getInteractionManager();
        this.player = player;
    }
    
    private void updateKeys() {
    
            if (im.getIsPressed("Interact")) {
                interact = true;
            }
            
            else if (interact) {
                
                if(player.getHud().getInfoText().getIsVisible())
                    player.getHud().getInfoText().hide();
                
                else
                    player.setHasChecked(interact);
                
                interact      = false;
                
            }
            
            if (im.getIsPressed("Torch")) {
                torch = true;
            }
            
            else if (torch) {
                
                torch         = false;
                if (player.getChild("Torch") == null)
                    return;
                
                Torch t = ((Torch) player.getChild("Torch"));
                
                if(t.isLit())
                    t.Extinguish();
                else
                    t.Light();
                    
            }
            
            if (im.getIsPressed("Click")) {
                shoot = true;
            }
            
            else if (shoot) {
                
                shoot  = false;
                if (player.getChild("Gun") == null)
                    return;
                
                Gun g = ((Gun) player.getChild("Gun"));
                g.fire();
                    
            }
            
    }
    
    public void update() {
        updateKeys();
    }
    
}
