/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;


import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Bawb
 */
public class CandleControl extends AbstractControl {

    
    //Update loop for the torch and torch control
    @Override
    protected void controlUpdate(float tpf) {
        
        Candle t = ((Candle) spatial);
        FireLight f = (FireLight) t.getLight();
        f.update(tpf);
        f.setPosition(t.getFlame().getWorldTranslation());
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
