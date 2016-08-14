/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.app.state.AppStateManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.entity.player.Player;


/**
 *
 * @author Bawb
 */
public class Torch extends Item {
    
    private final TorchControl torchControl;
    private final TorchLight   torchLight;
    
    //Constructs the Torch Entity
    public Torch(AppStateManager stateManager) {
        super(stateManager);
        torchControl = new TorchControl(stateManager, this);
        torchLight   = new TorchLight(stateManager);
        Node scene   = stateManager.getState(GameManager.class).getSceneManager().getScene();
        addControl(torchControl);
        torchLight.setPosition(getWorldTranslation());
        scene.addLight(torchLight);
        torchLight.setIsLit(true);
        torchControl.setFirstLit();
    }
    
    //Lights the torch
    public void light() {
        ((ParticleEmitter) getModel().getChild("Flame")).setEnabled(true);
        torchLight.setIsLit(true);
        torchControl.setFirstLit();
    }
    
    //Extinguishes the torch
    public void extinguish() {
        ((ParticleEmitter) getModel().getChild("Flame")).setEnabled(false);
        ((ParticleEmitter) getModel().getChild("Flame")).killAllParticles();
        torchLight.setIsLit(false);
        torchLight.setRadius(0);
        torchLight.setColor(ColorRGBA.Black);
        torchControl.stopLight();
    }
    
    //Returns the torches particle emitter
    public ParticleEmitter getFlame() {
        return (ParticleEmitter) getModel().getChild("Flame");
    }
    
    //Returns the torch light
    public TorchLight getTorchLight() {
        return torchLight;
    }
    
    //Returns whether the torch is lit
    public boolean isLit() {
        return torchLight.isLit();
    }
    
    @Override
    public void equip(Player player, boolean isLeft) {
        super.equip(player, isLeft);
        attachChild(this);
        //setLocalTranslation(.25f,0f,.1f);
    }
    
    @Override
    public void unequip(Player player) {
        super.unequip(player);
    }
    
    @Override
    public void use() {
            
        //If the torch is lit extinguish it     
        if (isLit()) {
            extinguish();
        }
         
        //If not Check for Matches
        else {
                
            //If the player has never had matches inform them
            if (player.getInventory().get("Matches") == null) {
                
                player.getHud().addAlert("Matches", "Where are the matches?");
                    
            }
                
            //If the player has more than one match light the torch
            else if (((Integer)player.getInventory().get("Matches")) > 0) {
                    
                //Construct match information string
                int newMatches = ((Integer)player.getInventory().get("Matches"))-1;
                String matchInfo = "You have " + newMatches + " matches left";
                    
                if(newMatches == 1)
                    matchInfo = "You are down to your last match";
                    
                //Light the Torch and Inform players of matches left
                player.getHud().addAlert("Light", "You light the torch..." + matchInfo);
                light();
                player.getInventory().put("Matches", newMatches);
                    
            }
             
            //If no matches inform player
            else {
                player.getHud().addAlert("Matches", "You are all out of matches");
            }
                
        }
    
    }
    
}
