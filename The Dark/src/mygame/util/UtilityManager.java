/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;

/**
 *
 * @author Bawb
 */
public class UtilityManager {
    
    private PhysicsManager     physicsManager;
    private InteractionManager interactionManager;
    private SimpleApplication app;
    
    public UtilityManager(SimpleApplication app) {
        this.app = app;
        createPhysicsManager();
        createInteractionManager();
    }
    
    private void createPhysicsManager() {
        physicsManager = new PhysicsManager(app.getStateManager());
    }
    
    public PhysicsManager getPhysicsManager() {
        return physicsManager;
    }
    
    private void createInteractionManager() {
        interactionManager = new InteractionManager(app);
    }
    
    public InteractionManager getInteractionManager() {
        return interactionManager;
    }
    
}
