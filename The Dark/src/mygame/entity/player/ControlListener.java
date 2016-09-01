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
    private boolean                  interact, leftItemPressed, rightItemPressed, debugLight, inventory;
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
    
        //Only Listen For Inventory Prior to Visible Check
        if (im.getIsPressed("Inventory")) {
            inventory = true;
        }
        
        else if (inventory) {
            inventoryPress();
        }
        
        //Don't Listen for Controls if Inventory is Visible
        if (player.getInventoryInterface().isVisible())
            return;
        
        if (im.getIsPressed("Interact")) {
            interact = true;
        }
        
        else if (interact) {
            interactPress();
        }
        
        if (im.getIsPressed("Click")) {
            
            if (!leftItemPressed) {
                leftPress();
            }
            
            leftItemPressed = true;
            
        }
        
        else if (leftItemPressed) {
            leftPressRelease();  
        }
        
        if (im.getIsPressed("RightClick")) {
            
            if (!rightItemPressed) {
                rightPress();
            }
            
            rightItemPressed = true;
            
        }
        
        else if (rightItemPressed) {
            rightPressRelease();
        }
        
        if (im.getIsPressed("DebugLight")) {
            debugLight = true;
        }
        
        else if (debugLight) {
            debugLightPress();
        }
            
    }
    
    private void inventoryPress() {
        
        inventory = false;
        
        if (player.getInventoryInterface().isVisible()) {
            stateManager.getApplication().getInputManager().setCursorVisible(false);
            player.getInventoryInterface().hide();
        }
        else {
            stateManager.getApplication().getInputManager().setCursorVisible(true);
            player.getInventoryInterface().show();
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
    
    private void leftPress() {
        
        if (player.hasLeft()) {
            player.getLeftEquip().press();
        }
        
    }
    
    private void leftHold() {
    
    }
    
    //Handles Player Toggling the Left Held Item
    private void leftPressRelease() {       
        
        leftItemPressed = false;      
        
        if (player.hasLeft())
            player.getLeftEquip().release();
            
    }

    private void rightPress() {
        if (player.hasRight())
            player.getRightEquip().press();  
    }
    
    private void rightHold() {
    
    }
    
    //Done when the player Uses the Right Item
    private void rightPressRelease() {
             
        rightItemPressed  = false;        
        
        if (player.hasRight())
            player.getRightEquip().release();        

    }    
    
    //Update Loop
    public void update() {
        updateKeys();
    }
    
}
