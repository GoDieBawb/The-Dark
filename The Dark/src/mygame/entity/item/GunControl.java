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
    
    public GunControl(Gun gun) {
        lastShot = System.currentTimeMillis() - 2000;
        this.gun = gun;
    }
    
    public void initSmoke() {

        smoke    = (ParticleEmitter) gun.getModel().getChild("Smoke");
        smoke.setParticlesPerSec(0);
        smoke.killAllParticles();
        
    }
    
    public void shootGun() {
        recoiling = true;
        lastShot  = System.currentTimeMillis();
        hasShot   = true;
        smoke.setEnabled(true);
        smoke.setParticlesPerSec(20);
        smoke.setStartColor(ColorRGBA.Orange);
    }
    
    private void recoil(float tpf) {
        
        if (gun.getLocalRotation().getX() < .02f) {
            gun.rotate(50*tpf,0,0);
        }
        
        else {
            recoiling = false;
            float y = gun.getLocalRotation().getY();
            float z = gun.getLocalRotation().getZ();
            float w = gun.getLocalRotation().getW();
            gun.setLocalRotation(new Quaternion(.02f,y,z,w));
            smoke.setStartColor(ColorRGBA.White);
        }
            
                    
        
    }
    
    private void finishReloading() {
        gun.setLocalTranslation(-.25f,.6f,.1f);
        gun.setLocalRotation(new Quaternion(0.0f, 0.9993738f, 0.0f, 0.03538324f));
        reloading = false;
        hasShot   = false;
    }
    
    private void animateReload(float tpf) {
        
        if (up) {
            gun.move(0,.5f*tpf,0);
        }
        
        else {
            gun.move(0,-.5f*tpf,0);
        }        
        
        if (gun.getLocalTranslation().y > .6f) {
            up = false;
        }
        
        else if (gun.getLocalTranslation().y < .5f) {
            up = true;
        }
        
    }
    
    public boolean hasShot() {
        return hasShot;
    }
    
    @Override
    protected void controlUpdate(float tpf) {

        if (!gun.isEquipped())
            return;
        
        if (System.currentTimeMillis() / 1000 - lastShot / 1000 > 1 && hasShot) {
        
            if (!reloading) {
                reloading = true;
                gun.startReloading();
                smoke.setParticlesPerSec(0);
            }
            
            if (reloading && System.currentTimeMillis() / 1000 - lastShot / 1000 > 3)
                finishReloading();
            
        }
        
        if (recoiling)
            recoil(tpf);
        
        if (reloading) {
            animateReload(tpf);
        }
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
