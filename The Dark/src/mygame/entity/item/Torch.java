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
    
    private TorchControl torchControl;
    private TorchLight   torchLight;
    
    public Torch(SimpleApplication app, Node scene) {
        torchControl = new TorchControl(app.getStateManager(), this);
        torchLight   = new TorchLight();
        addControl(torchControl);
        torchLight.setPosition(getWorldTranslation());
        scene.addLight(torchLight);
        torchLight.setIsLit(true);
        torchControl.setFirstLit();
    }
    
    public void Light() {
        ((ParticleEmitter) getModel().getChild("Flame")).setEnabled(true);
        torchLight.setIsLit(true);
        torchControl.setFirstLit();
    }
    
    public void Extinguish() {
        ((ParticleEmitter) getModel().getChild("Flame")).setEnabled(false);
        ((ParticleEmitter) getModel().getChild("Flame")).killAllParticles();
        torchLight.setIsLit(false);
        torchLight.setRadius(0);
        torchLight.setColor(ColorRGBA.Black);
        torchControl.stopLight();
    }
    
    public ParticleEmitter getFlame() {
        return (ParticleEmitter) getModel().getChild("Flame");
    }
    
    public TorchLight getTorchLight() {
        return torchLight;
    }
    
    public boolean isLit() {
        return torchLight.isLit();
    }
    
}
