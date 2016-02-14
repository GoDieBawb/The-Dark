/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.animation;

/**
 *
 * @author Bawb
 */
public interface Living extends Animated {
    
    //Living Entities Can Run
    public void run();
    
    //Living Entities Can Die
    public void die();

}
