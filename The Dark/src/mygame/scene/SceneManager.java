/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.util.PhysicsManager;

/**
 *
 * @author Bawb
 */
public class SceneManager {
    
    private Node              scene;
    private SimpleApplication app;
    private PhysicsManager    physicsManager;
    
    public SceneManager(SimpleApplication app, GameManager gm) {
        this.app       = app;
        physicsManager = gm.getUtilityManager().getPhysicsManager();
    }
    
    public void initScene(String path) {
        
        scene = (Node) app.getAssetManager().loadModel(path);
        RigidBodyControl rbc  = new RigidBodyControl(0f);
        scene.getChild(0).addControl(rbc);
        physicsManager.getPhysics().getPhysicsSpace().add(rbc);
        app.getRootNode().attachChild(scene);
        initLights();
        initFog();
        
    }
    
    private void initFog() {
        FilterPostProcessor ppf = app.getAssetManager().loadFilter("Effects/Fog.j3f");
        app.getViewPort().addProcessor(ppf);
    }    
    
    private void initLights() {
    
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(.1f));
        app.getRootNode().addLight(al);
        
        Node lightNode = (Node) scene.getChild("Lights");
        
        for (int i = 0; i < lightNode.getQuantity(); i++) {
        
            if (lightNode.getChild(i).getName().equals("Point")) {
            
                PointLight pl = new PointLight();
                pl.setColor(ColorRGBA.White.mult(1f));
                pl.setRadius(5);
                pl.setPosition(lightNode.getChild(i).getWorldTranslation());
                app.getRootNode().addLight(pl);
            
            }
            
        }
        
        lightNode.removeFromParent();
        
    }
    
    public Node getScene() {
        return scene;
    }
    
}
