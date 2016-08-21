/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.app.state.AppStateManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Bawb
 */

public class GunControl extends AbstractControl {

    private Long     lastShot;
    private boolean  hasShot;
    private boolean  reloading;
    private boolean  up;
    private boolean  recoiling;
    private ParticleEmitter smoke;
    private final Gun       gun;
    private final AppStateManager stateManager;
    
    //When constructed make sure it can be shot right away
    public GunControl(Gun gun, AppStateManager stateManager) {
        this.stateManager = stateManager;
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
        gun.setIsActing(true);
        recoiling     = true;
        lastShot      = System.currentTimeMillis();
        hasShot       = true;
        smoke.setEnabled(true);
        smoke.setParticlesPerSec(20);
        smoke.setStartColor(ColorRGBA.Orange);
    }
    
    //Recoil the weapon
    private void recoil(float tpf) {
        
        Vector3f camDir = stateManager.getApplication().getCamera().getDirection();
        if (gun.isLeft())
            gun.lookAt(camDir.normalize().add(0,5,0).mult(50), new Vector3f(0,1,0));
        else
            gun.lookAt(camDir.normalize().add(0,5,0).mult(50).negate(), new Vector3f(0,1,0));

    }
    
    //Finish reloading the gun
    private void finishReloading() {
        gun.setLocalTranslation(-.47f,-.34f, -.92f);
        gun.setLocalRotation(new Quaternion(0.0f, 0.9993738f, 0.0f, 0.03538324f));
        smoke.setParticlesPerSec(0);
        gun.setIsActing(false);
        recoiling    = false;
        reloading    = false;
        hasShot      = false;
    }
    
    //Animate the Gun Reloading
    private void animateReload(float tpf) {
        
        float bottom = -.3f;
        float top    = -.2f;
        
        //If the gun is moving upward move it up
        if (up) {
            gun.move(0,.5f*tpf,0);
        }
        
        //If not move it down
        else {
            gun.move(0,-.5f*tpf,0);
        }        
        
        //If too far up stop moving up
        if (gun.getLocalTranslation().y > top) {
            up = false;
        }
        
        //If too far down stop moving down
        if (gun.getLocalTranslation().y < bottom) {
            up = true;
        }
        
    }
    
    //Returns whether the gun has been shot or not
    public boolean hasShot() {
        return hasShot;
    }
    
    private void keepLocation() {
        float y    = gun.getLocalTranslation().getY();
        Camera cam = stateManager.getApplication().getCamera();
        
        if (gun.isLeft())
            gun.setLocalTranslation(cam.getDirection().normalize().add(cam.getLeft().normalize().mult(.3f)).add(0,-.25f,0).setY(y));
        else
            gun.setLocalTranslation(cam.getDirection().normalize().add(cam.getLeft().normalize().negate().mult(.3f)).add(0,-.25f,0).setY(y));
        
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
                smoke.killAllParticles();
                smoke.setParticlesPerSec(20);
                smoke.setStartColor(ColorRGBA.White);
            }
            
            //If reloading for more than 3 seconds done reloading
            if (reloading && System.currentTimeMillis() / 1000 - lastShot / 1000 > 3) {
                finishReloading();
            }
            
        }
        
        if (gun.isActing()) {
            keepLocation();
        }
        
        //If recoiling animate the recoil
        if (recoiling) {
            recoil(tpf);
        }
            
        //If reloading animate the reload
        if (reloading) {
            animateReload(tpf);
        }
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
