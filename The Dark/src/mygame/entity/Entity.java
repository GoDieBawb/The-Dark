/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity;

import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;

/**
 *
 * @author Bawb
 */
public abstract class Entity extends Node {
    
    private Node model;
    
    public void setModel(String path, AppStateManager stateManager) {
        model = (Node) stateManager.getApplication().getAssetManager().loadModel(path);
        attachChild(model);
    }
    
    public Node getModel() {
        return model;
    }
    
}
