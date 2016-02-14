/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.control;

/**
 *
 * @author Bawb
 */
 public abstract class InteractionControl {
    
    private boolean enabled;
     
    public abstract void update(float tpf);
    
    //Determines whether the Player is interacting
    public void setEnabled(boolean newVal) {
        enabled = newVal;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
}
