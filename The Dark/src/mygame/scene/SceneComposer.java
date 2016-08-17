/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.scene;

import mygame.entity.item.FireLight;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author Bawb
 * Allows for placement of lights within the scene composer
 * Currently setting the location of lights is bugged
 */
public class SceneComposer extends AbstractAppState {
    
    private SimpleApplication app;
    private ArrayList<FireLight> lights;
    
    //On initialize init lights
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        initLights();
    }
    
    //Inits lights in the scene composer
    private void initLights() {
    
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(.3f));
        app.getRootNode().addLight(al);
        Node scene     = (Node) app.getRootNode().getChild("Scene");
        Node lightNode = (Node) scene.getChild("Lights");
        
        if(lightNode == null)
            return;
        
        lights       = new ArrayList();
        
        //Iterate over the light node
        for (int i = 0; i < lightNode.getQuantity(); i++) {
        
            //If the name of the node is point add a point light
            if (lightNode.getChild(i).getName().equals("Point")) {
            
                PointLight pl = new PointLight();
                pl.setColor(ColorRGBA.White.mult(1f));
                pl.setRadius(5);
                pl.setPosition(lightNode.getChild(i).getWorldTranslation());
                app.getRootNode().addLight(pl);
            
            }
            
            //If the name of the node is torch add a torch light
            else if (lightNode.getChild(i).getName().equals("Torch")) {
            
                FireLight tl = new FireLight(app.getStateManager());
                scene.addLight(tl);
                tl.setColor(ColorRGBA.White.mult(1f));
                tl.setRadius(5);
                tl.setPosition(lightNode.getChild(i).getWorldTranslation());
                tl.setIsLit(true);
                lights.add(tl);
                
            }
            
        }
        
    }    
    
    //Run on a loop to update the light node
    @Override
    public void update(float tpf) {
        
        for(int i = 0;  i < lights.size(); i++) {
            lights.get(i).update(tpf);
        }        
        
    }
    
    //Clean up the Application State
    @Override
    public void cleanup() {
        super.cleanup();
    }
}
