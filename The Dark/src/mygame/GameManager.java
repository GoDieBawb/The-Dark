/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import mygame.util.InteractionManager;
import mygame.util.PhysicsManager;
import mygame.entity.player.Player;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import mygame.entity.monster.Zombie;

/**
 *
 * @author Bawb
 */
public class GameManager extends AbstractAppState {
    
    private Player             player;
    private Zombie             zombie;
    private InteractionManager interactionManager;
    private SimpleApplication  app;
    private PhysicsManager     physicsManager;
    private Node               scene;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication) app;
        createInteractionManager();
        createPhysicsManager();
        createScene();
        createPlayer();
        createZombie();
        addLight();
    }
    
    private void createScene() {
        
        scene = (Node) app.getAssetManager().loadModel("Scenes/Town.j3o");
        RigidBodyControl rbc  = new RigidBodyControl(0f);
        scene.getChild(0).addControl(rbc);
        physicsManager.getPhysics().getPhysicsSpace().add(rbc);
        app.getRootNode().attachChild(scene);
        app.getFlyByCamera().setEnabled(false);
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        //scene.setMaterial(mat);
        //((Geometry)scene.getChild("NavMesh")).getMaterial().getAdditionalRenderState().setWireframe(true);
        
    }
    
    public Node getScene() {
        return scene;
    }
    
    private void addLight() {
        AmbientLight al     = new AmbientLight();
        DirectionalLight dl = new DirectionalLight();
        app.getRootNode().addLight(al);
        app.getRootNode().addLight(dl);
        al.setColor(ColorRGBA.White.mult(.1f));
        
        dl.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        
    }
    
    private void createInteractionManager() {
        interactionManager = new InteractionManager(app);
    }
    
    public InteractionManager getInteractionManager() {
        return interactionManager;
    }
    
    private void createZombie() {
        zombie = new Zombie(app.getStateManager());
        zombie.createPhys();
        zombie.createFinderControl(app.getStateManager());
        app.getRootNode().attachChild(zombie);
        physicsManager.getPhysics().getPhysicsSpace().add(zombie.getPhys());
        zombie.getPhys().warp(new Vector3f(5,1,5));
    }
    
    public Zombie getZombie() {
        return zombie;
    }
    
    private void createPlayer() {
        player = new Player(app.getStateManager());
        player.createChaseControl();
        player.createPhys();
        app.getRootNode().attachChild(player);
        physicsManager.getPhysics().getPhysicsSpace().add(player.getPhys());
        physicsManager.getPhysics().getPhysicsSpace().setGravity(new Vector3f(0,-50,0));
        player.getPhys().warp(scene.getChild("StartSpot").getWorldTranslation());
    }
    
    public Player getPlayer() {
        return player;
    }
    
    private void createPhysicsManager() {
        physicsManager = new PhysicsManager(app.getStateManager());
    }
    
    public PhysicsManager getPhysicsManager() {
        return physicsManager;
    }
    
    @Override
    public void update(float tpf) {
        player.getChaseControl().update(tpf);
        zombie.getFinderControl().update(tpf);
    }
    
}
