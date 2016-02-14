/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;

/**
 *
 * @author Bawb
 */
public class PhysicsManager {
    
    private final BulletAppState physics;
    
    //Create and Attach Bullet App State on Construction
    public PhysicsManager(AppStateManager stateManager) {
        physics = new BulletAppState();
        stateManager.attach(physics);
    }
    
    //Refresh the physical body of a Node
    public void rePhys(AppStateManager stateManager, Node node) {
        physics.getPhysicsSpace().removeAll(node);
        physics.getPhysicsSpace().add(node);
    } 
    
    //Remove a Nodes physics completely
    public void clearPhysics(AppStateManager stateManager, Node node) {
        
        if (node == null)
            node = ((SimpleApplication)stateManager.getApplication()).getRootNode();
        
        physics.getPhysicsSpace().removeAll(node);
        
    }
    
    //Give a Rigid Body to a Node
    public void addToPhysics(Node node) {
        RigidBodyControl rbc = new RigidBodyControl(0f);
        node.addControl(rbc);
    }
    
    //Get the Bullet App State
    public BulletAppState getPhysics() {
        return physics;
    }
    
}
