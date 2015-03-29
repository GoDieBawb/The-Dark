/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;
import mygame.util.script.ScriptManager;
import mygame.util.yaml.YamlManager;

/**
 *
 * @author Bawb
 */
public class UtilityManager {
    
    private PhysicsManager     physicsManager;
    private InteractionManager interactionManager;
    private YamlManager        yamlManager;
    private GuiManager         guiManager;
    private ScriptManager      scriptManager;
    private AudioManager       audioManager;
    private SimpleApplication  app;
    
    public UtilityManager(SimpleApplication app) {
        this.app = app;
        createPhysicsManager();
        createInteractionManager();
        createYamlManager();
        createGuiManager();
        createScriptManager();
        createAudioManager();
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
    
    private void createYamlManager() {
        yamlManager = new YamlManager(app.getStateManager());
    }
    
    public YamlManager getYamlManager() {
        return yamlManager;
    }
    
    private void createGuiManager() {
        guiManager = new GuiManager(app);
    }
    
    public GuiManager getGuiManager() {
        return guiManager;
    }
    
    private void createScriptManager() {
        scriptManager = new ScriptManager(app.getStateManager());
    }
    
    public ScriptManager getScriptManager() {
        return scriptManager;
    }
    
    private void createAudioManager() {
        audioManager = new AudioManager(app.getStateManager());
    }
    
    public AudioManager getAudioManager() {
        return audioManager;
    }
    
}
