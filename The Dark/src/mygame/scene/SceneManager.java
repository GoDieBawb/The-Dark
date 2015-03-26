/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
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
    
    public void initScene() {
        
        scene = (Node) app.getAssetManager().loadModel("Scenes/Town.j3o");
        RigidBodyControl rbc  = new RigidBodyControl(0f);
        scene.getChild(0).addControl(rbc);
        physicsManager.getPhysics().getPhysicsSpace().add(rbc);
        app.getRootNode().attachChild(scene);
        app.getFlyByCamera().setEnabled(false);
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        addLight();
        
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
    
}
