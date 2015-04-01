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
                
                if (player.isDead()) {
                    player.restartGame();
                    interact = false;
                    return;
                }
                
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
                
                if (player.getChild("Torch") == null) {
                    return;
                }
                
                Torch t = ((Torch) player.getChild("Torch"));
                
                if(t.isLit()) {
                    t.Extinguish();
                }
                
                else {
                    
                    if (player.getInventory().get("Matches") == null) {
                    
                        player.getHud().showAlert("Matches", "Where are the matches?");
                        
                    }
                    
                    else if (((Integer)player.getInventory().get("Matches")) > 0) {
                        
                        int newMatches = ((Integer)player.getInventory().get("Matches"))-1;
                        String matchInfo = "You have " + newMatches + " matches left";
                        
                        if(newMatches == 1)
                            matchInfo = "You are down to your last match";
                        
                        player.getHud().showAlert("Light", "You light the torch..." + matchInfo);
                        t.Light();
                        player.getInventory().put("Matches", newMatches);
                        
                    }
                    
                    else {
                        player.getHud().showAlert("Matches", "You are all out of matches");
                    }
                    
                }    
                    
            }
            
            if (im.getIsPressed("Click")) {
                shoot = true;
            }
            
            else if (shoot) {
                
                shoot  = false;
                
                if (player.getChild("Gun") == null)
                   return;               
                
                if (((Gun) player.getChild("Gun")).getGunControl().hasShot())
                    return;                
                
                if (player.getInventory().get("Bullets") == null) {
                    player.getHud().showAlert("Bullets", "Where are the bullets?");
                    return;
                }
                
                else {
                
                    if (((Integer)player.getInventory().get("Bullets")) > 0) {
                    
                        int newBullets    = ((Integer)player.getInventory().get("Bullets"))-1;
                        String bulletInfo = "You have " + newBullets + " bullets left";
                        
                        if(newBullets == 1) 
                            bulletInfo = "You are down to your last bullet...";
                        
                        else if (newBullets == 0) {
                            
                            bulletInfo = "You have fired your last shot..";
                            
                        }
                        
                        Gun g = ((Gun) player.getChild("Gun"));
                        g.fire();   
                        player.getHud().showAlert("Gun", bulletInfo);
                        player.getInventory().put("Bullets", newBullets);
                                
                    }
                    
                    else {
              
                        player.getHud().showAlert("Gun", "You are out of bullets");
                        
                    }
                    
                }    
                    
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
