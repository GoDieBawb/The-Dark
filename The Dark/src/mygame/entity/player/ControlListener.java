package mygame.entity.player;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.item.Gun;
import mygame.entity.item.Torch;
import mygame.util.InteractionManager;

/**
 *
 * @author Bawb
 */

public class ControlListener {
    
    private final InteractionManager im;
    private final Player             player;
    private boolean                  interact, torch, shoot, debugLight;
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
            torch = true;
        }
        
        else if (torch) {
            torchPress();  
        }
        
        if (im.getIsPressed("Click")) {
            shoot = true;
        }
        
        else if (shoot) {
            shootPress();
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
        
        //Restart the game if the player is dead
        if (player.isDead()) {
            player.restartGame();
            interact = false;
            return;
        }
        
        //Close the messages window if it is visible
        if (player.getHud().getInfoText().getIsVisible()) {
            player.getHud().getInfoText().hide();
            player.getHud().checkAlert();
        }
        
        //If not dead and messages are not visible the player is attempting to interact
        else
            player.setHasChecked(interact);
            
        interact      = false;
        
    }
    
    //Done when the player presses shoot
    private void shootPress() {
        
        //Sets the shoot Interact To False
        shoot  = false;
         
        //Dont do anything if the player doesnt have a gun
        if (player.getChild("Gun") == null)
           return;               
         
        //If the player is still reloading don't do anything
        if (((Gun) player.getChild("Gun")).getGunControl().hasShot())
            return;                
         
        //If the player has no bullets inform them
        if (player.getInventory().get("Bullets") == null) {
            player.getHud().addAlert("Bullets", "Where are the bullets?");
        }
         
        else {
         
            //If the player has more than one bullet
            if (((Integer)player.getInventory().get("Bullets")) > 0) {
            
                //Saves the new amount of bullets
                int newBullets           = ((Integer)player.getInventory().get("Bullets"))-1;
                
                //Gets Collision
                CollisionResults results = new CollisionResults();
                Ray              ray     = new Ray(stateManager.getApplication().getCamera().getLocation(), stateManager.getApplication().getCamera().getDirection());
                Node          entityNode = stateManager.getState(GameManager.class).getEntityManager().getEntityNode();
                 
                //Check to see if it colided with the entity node
                entityNode.collideWith(ray, results);
                 
                if (results.size() > 0) {
                    
                    //Find the Entity Hit
                    Entity hitEntity = findEntity(results.getCollision(1).getGeometry().getParent());
                    
                    //Run the hit script on the hit entity
                    if (hitEntity != null) {
                        hitEntity.getScript().hitAction();
                    }
                    
                }
                 
                //Inform them of how many bullets the have left
                String bulletInfo = "You have " + newBullets + " bullets left";
                 
                if(newBullets == 1) 
                    bulletInfo = "You are down to your last bullet...";
                 
                else if (newBullets == 0) {
                     
                    bulletInfo = "You have fired your last shot..";
                     
                }
                 
                //Fire the guns
                Gun g = ((Gun) player.getChild("Gun"));
                g.fire();
                //Update the players inventory
                player.getHud().addAlert("Gun", bulletInfo);
                player.getInventory().put("Bullets", newBullets);
                         
            }
           
            //If the player is out of bullets inform them
            else {
       
                player.getHud().addAlert("Gun", "You are out of bullets");
                 
            }
             
        }    
            
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
    
    //Handles Player Toggling the Torch
    private void torchPress() {
        
        torch = false;
            
        //If the player has no torch do nothing
        if (player.getChild("Torch") == null) {
                return;
        }
            
        //If the torch is lit extinguish it
        Torch t = ((Torch) player.getChild("Torch"));
            
        if (t.isLit()) {
            t.Extinguish();
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
                t.Light();
                player.getInventory().put("Matches", newMatches);
                    
            }
             
            //If no matches inform player
            else {
                player.getHud().addAlert("Matches", "You are all out of matches");
            }
                
        }
            
    }
    
    //Gets the Entity out of a Node. For Shooting an Entity
    private Entity findEntity(Node node) {
    
        if (node instanceof Entity) {
            return (Entity) node;
        }
        
        else if (node != ((SimpleApplication) stateManager.getApplication()).getRootNode()){
            return findEntity(node.getParent());
        }
        
        else {
            return null;
        }
        
    }
    
    //Update Loop
    public void update() {
        updateKeys();
    }
    
}
