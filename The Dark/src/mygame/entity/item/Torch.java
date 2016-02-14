/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import mygame.entity.Entity;

/**
 *
 * @author Bawb
 */
public class Torch  extends Entity {
    
    private final TorchControl torchControl;
    private final TorchLight   torchLight;
    
    //Constructs the Torch Entity
    public Torch(SimpleApplication app, Node scene) {
        torchControl = new TorchControl(app.getStateManager(), this);
        torchLight   = new TorchLight(app.getStateManager());
        addControl(torchControl);
        torchLight.setPosition(getWorldTranslation());
        scene.addLight(torchLight);
        torchLight.setIsLit(true);
        torchControl.setFirstLit();
    }
    
    //Lights the torch
    public void Light() {
        ((ParticleEmitter) getModel().getChild("Flame")).setEnabled(true);
        torchLight.setIsLit(true);
        torchControl.setFirstLit();
    }
    
    //Extinguishes the torch
    public void Extinguish() {
        ((ParticleEmitter) getModel().getChild("Flame")).setEnabled(false);
        ((ParticleEmitter) getModel().getChild("Flame")).killAllParticles();
        torchLight.setIsLit(false);
        torchLight.setRadius(0);
        torchLight.setColor(ColorRGBA.Black);
        torchControl.stopLight();
    }
    
    //Returns the torches particle emitter
    public ParticleEmitter getFlame() {
        return (ParticleEmitter) getModel().getChild("Flame");
    }
    
    //Returns the torch light
    public TorchLight getTorchLight() {
        return torchLight;
    }
    
    //Returns whether the torch is lit
    public boolean isLit() {
        return torchLight.isLit();
    }
    
}
