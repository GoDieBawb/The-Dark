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
import mygame.entity.item.Lamp;
import mygame.entity.player.PlayerManager;
import mygame.util.PhysicsManager;
import mygame.util.FileWalker;

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
    private FileWalker                   fw;
    
    //Inits Scene 
    public SceneManager(SimpleApplication app, GameManager gm) {
        this.app       = app;
        physicsManager = gm.getUtilityManager().getPhysicsManager();
        fw             = new FileWalker();
        initEffects();
    }
    
    //Inits the scene by path
    public void initScene(String path) {
        
        System.out.println("Initializing Scene...");
        
        removeScene();
        
        String[] p = path.split("/");
        path       = p[p.length-1];
        path       = path.split("\\.")[0];
        path       = fw.walk("assets/Scenes", path);
        path       = path.split("assets/")[1];
        path       = path.replace("data", "");
        System.out.println(path);
        
        scene                 = (Node) app.getAssetManager().loadModel(path);
        RigidBodyControl rbc  = new RigidBodyControl(0f);
        PlayerManager    pm   = app.getStateManager().getState(GameManager.class).getEntityManager().getPlayerManager();
        scene.getChild("SceneNode").addControl(rbc);
        physicsManager.getPhysics().getPhysicsSpace().add(rbc);
        app.getRootNode().attachChild(scene);
        app.getStateManager().getState(GameManager.class).getEntityManager().initEntities(this);
        
        pm.placePlayer();
        
        //Checks scene name and adds proper effects
        if (path.toLowerCase().contains("town"))
            app.getViewPort().addProcessor(fog);
        
        else
            app.getViewPort().removeProcessor(fog);
        
        if (path.toLowerCase().contains("well"))
            app.getViewPort().addProcessor(water);
        
        else
            app.getViewPort().removeProcessor(water);
        
        System.out.println("Scene Initialized!");
        
    }
    
    //Removes the Current Scene and Nulls it
    public void removeScene() {
        
        physicsManager.clearPhysics(app.getStateManager(), scene);
        app.getViewPort().removeProcessor(fog);
        
        if (scene == null)
            return;
        
        for (int i = 0; i < scene.getLocalLightList().size(); i++) {
            //scene;
        }
        
        if(scene != null)
            scene.detachAllChildren();
        
        scene = new Node();
        
    }    
    
    //Initializes Post Processor Filters
    private void initEffects() {
        fog   = app.getAssetManager().loadFilter("Effects/Fog.j3f");
        water = app.getAssetManager().loadFilter("Effects/Water.j3f");
    }    
    
    //Adds Lights to Nodes in the Light Node
    public void initLights() {
    
        PlayerManager pm   = app.getStateManager().getState(GameManager.class).getEntityManager().getPlayerManager();
        
        if(pm.getPlayer().hasLeft()) {
            
            if (pm.getPlayer().getLeftEquip() instanceof Lamp) {
                scene.addLight(((Lamp) pm.getPlayer().getLeftEquip()).getLight());
                System.out.println("Adding Left Lamp");
            }
            
        }        
        
        if (pm.getPlayer().hasRight()) {
            
            if (pm.getPlayer().getRightEquip() instanceof Lamp) {
                System.out.println("Adding Right Lamp");
                scene.addLight(((Lamp) pm.getPlayer().getRightEquip()).getLight());
            }
            
        }
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(.05f));
        
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
    
    //Returns the Current Scene
    public Node getScene() {
        return scene;
    }
    
    public void update(float tpf) {
        
    }
    
}
