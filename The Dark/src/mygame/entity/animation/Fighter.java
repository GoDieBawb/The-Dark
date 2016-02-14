/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.animation;

/**
 *
 * @author Bawb
 * Interface for a fighting Entity
 */
public interface Fighter extends Living {
    
    //All fighters can attack in some way
    public void attack();
    
}
