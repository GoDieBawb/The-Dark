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
    
    public boolean inProx();
    
}
