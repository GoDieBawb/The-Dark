/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.animation;

import com.jme3.animation.AnimControl;

/**
 *
 * @author Bawb
 */
public interface Animated {
    
    public AnimControl getAnimControl();
    
    public void idle();
    
}
