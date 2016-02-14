/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.monster;

import mygame.entity.Actor;

/**
 *
 * @author Bawb
 */
public interface Monster extends Actor {
    
    //All Monsters Can attack
    public void attack();
    
}
