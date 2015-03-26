/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity;

import com.jme3.bullet.control.BetterCharacterControl;

/**
 *
 * @author Bawb
 */
public interface PhysicalEntity {
    
    public void createPhys();
    
    public BetterCharacterControl getPhys();
    
}
