package mygame.entity.player;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import mygame.GameManager;
import mygame.util.InteractionManager;

/**
 *
 * @author Bawb
 */

public class ControlListener {
    
    private final InteractionManager im;
    private final Player             player;
    private boolean                  interact, leftItemPressed, rightItemPressed, debugLight;
    private final AmbientLight       light;
    private boolean                  isLit;
    private final AppStateManager    stateManager;
    
    //Constructs the Control Listener
    public ControlListener(AppStateManager stateManager, Player player) {
        this.stateManager = stateManager;
        im                = stateManager.getState(GameManager.class).getUtilityManager().getInteractionManager();
        this.player       = player;
        //This is the Cheater Light
        light             =  new AmbientLight();
        light.setColor(ColorRGBA.White.mult(3));
    }
    
    //Updates the Keys
    private void updateKeys() {
    
        if (im.getIsPressed("Interact")) {
            interact = true;
        }
        
        else if (interact) {
            interactPress();
        }
        
        if (im.getIsPressed("Torch")) {
            leftItemPressed = true;
        }
        
        else if (leftItemPressed) {
            leftPress();  
        }
        
        if (im.getIsPressed("Click")) {
            rightItemPressed = true;
        }
        
        else if (rightItemPressed) {
            rightPress();
        }
        
        if (im.getIsPressed("DebugLight")) {
            debugLight = true;
        }
        
        else if (debugLight) {
            debugLightPress();
        }
            
    }
    
    //Handles the Player pressing interact
    private void interactPress() {
        
        interact  = false;
        
        //Restart the game if the player is dead
        if (player.isDead()) {
            player.getHud().getInfoText().hide();
            player.restartGame();
            return;
        }
        
        //Close the messages window if it is visible
        if (player.getHud().getInfoText().getIsVisible()) {
            player.getHud().getInfoText().hide();
            player.getHud().checkAlert();
        }
        
        //If not dead and messages are not visible the player is attempting to interact
        else
            player.setHasChecked(true);
        
    }
    
    //Done when the player Uses the Right Item
    private void rightPress() {
        
        //Sets the shoot Interact To False
        rightItemPressed  = false;
        
        //Dont do anything if the hand is empty
        if (player.hasRight())
            player.getRightEquip().use();        

            
    }
    
    //Toggles the Debug Light on and Off
    private void debugLightPress() {
        
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
    
    //Handles Player Toggling the Left Held Item
    private void leftPress() {
        
        leftItemPressed = false;
        
        if (player.hasLeft())
            player.getLeftEquip().use();

            
    }
    
    //Update Loop
    public void update() {
        updateKeys();
    }
    
}
