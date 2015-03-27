/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity;

import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import mygame.util.script.Script;

/**
 *
 * @author Bawb
 */
public class Entity extends Node implements Scriptable {
    
    private Node    model;
    private boolean isHid;
    private boolean inProx;
    private Script  script;
    
    public void setModel(String path, AppStateManager stateManager) {
        model = (Node) stateManager.getApplication().getAssetManager().loadModel(path);
        attachChild(model);
    }
    
    public Node getModel() {
        return model;
    }
    
    public void hide() {
        isHid =  true;
        model.setLocalTranslation(0, 0, 0);    
    }
    
    public void show() {
        isHid =  false;
        model.setLocalTranslation(0, -15, 0);
    }
    
    public boolean isHid() {
        return isHid;
    }
    
    public void setScript(Script script) {
        this.script = script;
    }
    
    public Script getScript() {
        return script;
    }
    
    public void setProximity() {
        if (script == null)
            return;
    }
    
    public void setinProx(boolean newVal) {
        inProx = newVal;
    }
    
    public boolean inProx() {
        return inProx;
    }
    
}
