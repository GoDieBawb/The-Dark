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
    private FilterPostProcessor ppf;
    
    public SceneManager(SimpleApplication app, GameManager gm) {
        this.app       = app;
        physicsManager = gm.getUtilityManager().getPhysicsManager();
       initFog();
    }
    
    public void initScene(String path) {
        
        removeScene();
        scene                 = (Node) app.getAssetManager().loadModel(path);
        RigidBodyControl rbc  = new RigidBodyControl(0f);
        scene.getChild(0).addControl(rbc);
        physicsManager.getPhysics().getPhysicsSpace().add(rbc);
        app.getRootNode().attachChild(scene);
        app.getStateManager().getState(GameManager.class).getEntityManager().initEntities(scene);
        app.getStateManager().getState(GameManager.class).getEntityManager().getPlayerManager().placePlayer();
        initLights();
        
        if (scene.getUserData("Interior") == null)
            app.getViewPort().addProcessor(ppf);
        
    }
    
    public void removeScene() {
        physicsManager.clearPhysics(app.getStateManager(), scene);
        app.getViewPort().removeProcessor(ppf);
        if(scene != null)
        scene.detachAllChildren();
        scene = new Node();
    }    
    
    private void initFog() {
        ppf = app.getAssetManager().loadFilter("Effects/Fog.j3f");
    }    
    
    private void initLights() {
    
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(.01f));
        app.getRootNode().addLight(al);
        
        Node lightNode = (Node) scene.getChild("Lights");
        
        if(lightNode == null)
            return;
        
        for (int i = 0; i < lightNode.getQuantity(); i++) {
        
            if (lightNode.getChild(i).getName().equals("Point")) {
            
                PointLight pl = new PointLight();
                scene.addLight(pl);
                pl.setColor(ColorRGBA.White.mult(1f));
                pl.setRadius(5);
                pl.setPosition(lightNode.getChild(i).getWorldTranslation());
            
            }
            
        }
        
    }
    
    public Node getScene() {
        return scene;
    }
    
}
