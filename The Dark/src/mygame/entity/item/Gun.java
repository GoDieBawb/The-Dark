/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.audio.AudioNode;
import mygame.entity.Entity;

/**
 *
 * @author Bawb
 */
public class Gun extends Entity {
    
    private GunControl gunControl;
    private boolean    equipped;
    private AudioNode  audioNode;
    
    public Gun(AudioNode audioNode) {
        this.audioNode = audioNode;
        gunControl     = new GunControl(this);
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
    
    public void fire() {
    
        if(gunControl.isReloading()) {
        
        }
        
        else {
            audioNode.playInstance();
            gunControl.shootGun();
        }
        
    }
    
}
