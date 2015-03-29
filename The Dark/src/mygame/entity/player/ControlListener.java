/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
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
    private boolean            interact, torch, shoot, debugLight;
    private AmbientLight       light;
    private boolean            isLit;
    private AppStateManager    stateManager;
    
    public ControlListener(AppStateManager stateManager, Player player) {
        this.stateManager = stateManager;
        im          = stateManager.getState(GameManager.class).getUtilityManager().getInteractionManager();
        this.player = player;
        light       =  new AmbientLight();
        light.setColor(ColorRGBA.White.mult(3));
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
                
                if(t.isLit()) {
                    t.Extinguish();
                }
                
                else {
                    player.getHud().showAlert("Light", "You light the torch...");
                    t.Light();
                }    
                    
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
            
            if (im.getIsPressed("DebugLight")) {
                debugLight = true;
            }
            
            else if (debugLight) {
                
                debugLight  = false;

                if (isLit) {
                    ((SimpleApplication) stateManager.getApplication()).getRootNode().removeLight(light);
                    isLit = false;
                }
                
                else {
                    ((SimpleApplication) stateManager.getApplication()).getRootNode().addLight(light);
                    isLit = true;
                }
                
                    
            }
            
    }
    
    public void update() {
        updateKeys();
    }
    
}
