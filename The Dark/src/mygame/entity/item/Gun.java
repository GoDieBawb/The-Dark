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
    
    private GunControl gunControl;
    private boolean    equipped;
    private AudioNode  gunShot;
    private AudioNode  reload;
    
    public Gun(AudioManager am) {
        gunShot    = am.getSound("Gunshot");
        reload     = am.getSound("Reloading");
        gunControl = new GunControl(this);
        addControl(gunControl);
    }
    
    public void setEquipped(boolean newVal) {
        equipped = newVal;
    }
    
    public boolean isEquipped() {
        return equipped;
    }
    
    public void initModel() {
        gunControl.initSmoke();
    }
    
    public void startReloading() {
        reload.setTimeOffset(1);
        reload.play();
    }
    
    public GunControl getGunControl() {
        return gunControl;
    }
    
    public void fire() {
    
        if(gunControl.hasShot()) {
        
        }
        
        else {
            gunShot.playInstance();
            gunControl.shootGun();
        }
        
    }
    
}
