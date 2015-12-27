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
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.entity.item.Torch;
import mygame.entity.player.PlayerManager;
import mygame.util.PhysicsManager;

/**
 *
 * @author Bawb
 */
public class SceneManager {
    
    private Node                         scene;
    private final SimpleApplication      app;
    private final PhysicsManager         physicsManager;
    private FilterPostProcessor          fog;
    private FilterPostProcessor          water;
    
    public SceneManager(SimpleApplication app, GameManager gm) {
        this.app       = app;
        physicsManager = gm.getUtilityManager().getPhysicsManager();
        initEffects();
    }
    
    public void initScene(String path) {
        
        removeScene();
        
        scene                 = (Node) app.getAssetManager().loadModel(path);
        RigidBodyControl rbc  = new RigidBodyControl(0f);
        PlayerManager    pm   = app.getStateManager().getState(GameManager.class).getEntityManager().getPlayerManager();
        scene.getChild(0).addControl(rbc);
        physicsManager.getPhysics().getPhysicsSpace().add(rbc);
        app.getRootNode().attachChild(scene);
        app.getStateManager().getState(GameManager.class).getEntityManager().initEntities(this);
        pm.placePlayer();
        
        if (path.toLowerCase().contains("town"))
            app.getViewPort().addProcessor(fog);
        
        else
            app.getViewPort().removeProcessor(fog);
        
        if (path.toLowerCase().contains("well"))
            app.getViewPort().addProcessor(water);
        
        else
            app.getViewPort().removeProcessor(water);
        
    }
    
    public void removeScene() {
        
        physicsManager.clearPhysics(app.getStateManager(), scene);
        app.getViewPort().removeProcessor(fog);
        
        if(scene != null)
            scene.detachAllChildren();
        
        scene = new Node();
        scene.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
    }    
    
    private void initEffects() {
        fog   = app.getAssetManager().loadFilter("Effects/Fog.j3f");
        water = app.getAssetManager().loadFilter("Effects/Water.j3f");
    }    
    
    public void initLights() {
    
        PlayerManager pm   = app.getStateManager().getState(GameManager.class).getEntityManager().getPlayerManager();
        
        if(pm.getPlayer().getChild("Torch") != null) {
            scene.addLight(((Torch) pm.getPlayer().getChild("Torch")).getTorchLight());
        }        
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(.05f));
        scene.addLight(al);
        
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
    
    public void update(float tpf) {
        
    }
    
}
