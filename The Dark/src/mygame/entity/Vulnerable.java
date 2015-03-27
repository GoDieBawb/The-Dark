/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity;

/**
 *
 * @author Bawb
 */
public interface Vulnerable {
    
    public void setMaxHealth(int newVal);
    
    public int  getMaxHealth();
    
    public void setHealth(int newVal);
    
    public int  getHealth();
    
    public void endLife();
    
}
