/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.light.Light;

/**
 *
 * @author root
 */
public interface Lamp {
    
    public Light getLight();
    
    public void light();
    
    public void extinguish();
    
}
