/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.audio.AudioNode;
import mygame.entity.Entity;
import mygame.util.AudioManager;

/**
 *
 * @author Bawb
 */
public class Gun extends Entity {
    
    private final GunControl gunControl;
    private final AudioNode  gunShot;
    private final AudioNode  reload;
    private boolean          equipped;
    
    //Load the proper sounds from the audiomanager
    public Gun(AudioManager am) {
        gunShot    = am.getSound("Gunshot");
        reload     = am.getSound("Reloading");
        gunControl = new GunControl(this);
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
    
}
