/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.player.Player;
import mygame.util.AudioManager;

/**
 *
 * @author Bawb
 */
public class Gun extends Item {
    
    private final GunControl gunControl;
    private final AudioNode  gunShot;
    private final AudioNode  reload;
    private boolean          equipped;
    
    
    //Load the proper sounds from the audiomanager
    public Gun(AppStateManager stateManager) {
        super(stateManager);
        AudioManager am = stateManager.getState(GameManager.class).getUtilityManager().getAudioManager();
        gunShot         = am.getSound("Gunshot");
        reload          = am.getSound("Reloading");
        gunControl      = new GunControl(this, stateManager);
        addControl(gunControl);
    }
    
    //Determines whether the gun is equipped
    public void setEquipped(boolean newVal) {
        equipped = newVal;
    }
    
    //Returns whether the gun is equipped
    public boolean isEquipped() {
        return equipped;
    }
    
    //Initiates the Smoke Particles
    public void initModel() {
        gunControl.initSmoke();
    }
    
    //Starts the reloading of the weapon and plays the sound
    public void startReloading() {
        reload.setTimeOffset(1);
        reload.play();
    }
    
    //Returns the Gun Control Haha Gun Control
    public GunControl getGunControl() {
        return gunControl;
    }
    
    //Fires the Gun
    public void fire() {
    
        //If the player has not shot play a gun shot and shoot the gun
        if(!gunControl.hasShot()) {
            gunShot.playInstance();
            gunControl.shootGun();
        }
        
    }
    
    @Override
    public void equip(Player player, boolean isLeft) {    
        super.equip(player, isLeft);
        player.getHud().attachCrossHair();
        setEquipped(true);
    }
    
    @Override
    public void unequip(Player player) {
        super.unequip(player);
    }

    @Override
    public void use() {
         
        Player player = stateManager.getState(GameManager.class).getEntityManager().getPlayerManager().getPlayer(); 
        
        //If the player is still reloading don't do anything
        if (getGunControl().hasShot())
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
                fire();
                
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
    
}
