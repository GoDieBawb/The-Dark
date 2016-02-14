/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.effect.ParticleEmitter;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Bawb
 */

public class GunControl extends AbstractControl {

    private Long    lastShot;
    private boolean hasShot;
    private Gun     gun;
    private boolean reloading;
    private boolean up;
    private boolean recoiling;
    private ParticleEmitter smoke;
    
    //When constructed make sure it can be shot right away
    public GunControl(Gun gun) {
        lastShot = System.currentTimeMillis() - 2000;
        this.gun = gun;
    }
    
    //Turns off the particles when the model is initiated
    public void initSmoke() {

        smoke    = (ParticleEmitter) gun.getModel().getChild("Smoke");
        smoke.setParticlesPerSec(0);
        smoke.killAllParticles();

    }
    
    //Shoot the Gun
    public void shootGun() {
        recoiling = true;
        lastShot  = System.currentTimeMillis();
        hasShot   = true;
        smoke.setEnabled(true);
        smoke.setParticlesPerSec(20);
        smoke.setStartColor(ColorRGBA.Orange);
    }
    
    //Recoil the weapon
    private void recoil(float tpf) {
        
        //If the gun is not full recoiled keep moving
        if (gun.getLocalRotation().getX() < .02f) {
            gun.rotate(50*tpf,0,0);
        }
        
        //If the gun is fully recoiled stop recoiling
        else {
            recoiling = false;
            float y   = gun.getLocalRotation().getY();
            float z   = gun.getLocalRotation().getZ();
            float w   = gun.getLocalRotation().getW();
            gun.setLocalRotation(new Quaternion(.02f,y,z,w));
            smoke.setStartColor(ColorRGBA.White);
        }      

    }
    
    //Finish reloading the gun
    private void finishReloading() {
        gun.setLocalTranslation(-.25f,.6f,.25f);
        gun.setLocalRotation(new Quaternion(0.0f, 0.9993738f, 0.0f, 0.03538324f));
        reloading = false;
        hasShot   = false;
    }
    
    //Animate the Gun Reloading
    private void animateReload(float tpf) {
        
        //If the gun is moving upward move it up
        if (up) {
            gun.move(0,.5f*tpf,0);
        }
        
        //If not move it down
        else {
            gun.move(0,-.5f*tpf,0);
        }        
        
        //If too far up stop moving up
        if (gun.getLocalTranslation().y > .6f) {
            up = false;
        }
        
        //If too far down stop moving down
        else if (gun.getLocalTranslation().y < .5f) {
            up = true;
        }
        
    }
    
    //Returns whether the gun has been shot or not
    public boolean hasShot() {
        return hasShot;
    }
    
    //The Update Loop
    @Override
    protected void controlUpdate(float tpf) {

        //If gun is not equipped dont do anythign
        if (!gun.isEquipped())
            return;
        
        //
        if (System.currentTimeMillis() / 1000 - lastShot / 1000 > 1 && hasShot) {
        
            //If Ive shot and Im not reloading start reloading
            if (!reloading) {
                reloading = true;
                gun.startReloading();
                smoke.setParticlesPerSec(0);
            }
            
            //If reloading for more than 3 seconds done reloading
            if (reloading && System.currentTimeMillis() / 1000 - lastShot / 1000 > 3)
                finishReloading();
            
        }
        
        //If recoiling animate the recoil
        if (recoiling)
            recoil(tpf);
        
        //If reloading animate the reload
        if (reloading) {
            animateReload(tpf);
        }
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
