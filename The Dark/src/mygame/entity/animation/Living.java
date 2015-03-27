/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.animation;

import mygame.entity.animation.Animated;

/**
 *
 * @author Bawb
 */
public interface Living extends Animated {
    
    public void run();
    
    public void die();

}
