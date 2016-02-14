/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.animation;

import com.jme3.animation.AnimControl;

/**
 *
 * @author Bawb
 * Interface for Animated Entity
 */
public interface Animated {
    
    //Returns the animated entity's Animation Control
    public AnimControl getAnimControl();
    
    //All animated entities have an idle animation at least
    public void idle();
    
}
