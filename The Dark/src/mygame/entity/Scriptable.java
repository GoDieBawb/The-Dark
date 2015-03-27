/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity;

import mygame.util.script.Script;

/**
 *
 * @author Bawb
 */
public interface Scriptable {
    
    void setScript(Script Script);
    
    public Script getScript();
    
    public void setProximity();
    
    public void setinProx(boolean newVal);
    
    public boolean inProx();
    
}
