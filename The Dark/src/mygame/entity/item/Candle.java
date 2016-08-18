package mygame.entity.item;

import com.jme3.app.state.AppStateManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.entity.player.Player;


/**
 *
 * @author Bawb
 */
public class Candle extends Item implements Lamp {
    
    private final CandleControl candleControl;
    private final FireLight     fireLight;
    
    //Constructs the Candle Entity
    public Candle(AppStateManager stateManager) {
        super(stateManager);
        candleControl = new CandleControl();
        fireLight     = new FireLight(stateManager);
        Node scene    = stateManager.getState(GameManager.class).getSceneManager().getScene();
        fireLight.setMinRadius(2);
        fireLight.setMaxRadius(8);
        addControl(candleControl);
        fireLight.setPosition(getWorldTranslation());
        scene.addLight(fireLight);
        fireLight.setIsLit(true);
    }
    
    //Lights the flame
    @Override
    public void light() {
        ((ParticleEmitter) getModel().getChild("Flame")).setEnabled(true);
        fireLight.setIsLit(true);
    }
    
    //Extinguishes the flame
    @Override
    public void extinguish() {
        ((ParticleEmitter) getModel().getChild("Flame")).setEnabled(false);
        ((ParticleEmitter) getModel().getChild("Flame")).killAllParticles();
        fireLight.setIsLit(false);
        fireLight.setRadius(0);
        fireLight.setColor(ColorRGBA.Black);
    }
    
    //Returns the torches particle emitter
    public ParticleEmitter getFlame() {
        return (ParticleEmitter) getModel().getChild("Flame");
    }
    
    //Returns the torch light
    @Override
    public Light getLight() {
        return fireLight;
    }
    
    //Returns whether the torch is lit
    public boolean isLit() {
        return fireLight.isLit();
    }
    
    @Override
    public void equip(Player player, boolean isLeft) {
        super.equip(player, isLeft);
        attachChild(this);
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
            else if (((Integer)player.getInventory().get("Matches").getAmount()) > 0) {
                    
                //Construct match information string
                int newMatches = ((Integer)player.getInventory().get("Matches").getAmount())-1;
                String matchInfo = "You have " + newMatches + " matches left";
                    
                if(newMatches == 1)
                    matchInfo = "You are down to your last match";
                    
                //Light the Candle and Inform players of matches left
                player.getHud().addAlert("Light", "You light the torch..." + matchInfo);
                light();
                player.getInventory().get("Matches").setAmount(newMatches);
                    
            }
             
            //If no matches inform player
            else {
                player.getHud().addAlert("Matches", "You are all out of matches");
            }
                
        }
    
    }
    
}
