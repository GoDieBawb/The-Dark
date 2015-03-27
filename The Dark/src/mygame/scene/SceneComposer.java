/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.scene;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;

/**
 *
 * @author Bawb
 */
public class SceneComposer extends AbstractAppState {
    
    private SimpleApplication app;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        initLights();
    }
    
    private void initLights() {
    
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(.3f));
        app.getRootNode().addLight(al);
        Node scene     = (Node) app.getRootNode().getChild("Scene");
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
    
    @Override
    public void update(float tpf) {
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
    }
}
