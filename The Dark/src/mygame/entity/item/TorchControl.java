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
public class TorchControl extends AbstractControl {

    @Override
    protected void controlUpdate(float tpf) {
        ((Torch) spatial).getTorchLight().update(tpf);
        ((Torch) spatial).getTorchLight().setPosition(((Torch) spatial).getFlame().getWorldTranslation());
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
