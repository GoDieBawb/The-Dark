/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import mygame.entity.EntityManager;
import mygame.scene.SceneManager;
import mygame.util.UtilityManager;

/**
 *
 * @author Bawb
 */
public class GameManager extends AbstractAppState {
    
    private SimpleApplication  app;
    private UtilityManager     utilityManager;
    private EntityManager      entityManager;
    private SceneManager       sceneManager;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication) app;
        createUtilityManager();
        createSceneManager();
        createEntityManager();
        initSounds();
        loadGame();
    }
    
    private void initSounds() {
    
        utilityManager.getAudioManager().loadSound("Ambience" , "Sounds/Organ.ogg", true);
        utilityManager.getAudioManager().loadSound("Gunshot", "Sounds/Gunshot.ogg", false);
        utilityManager.getAudioManager().loadSound("Empty", "Sounds/Empty.ogg", false);
        utilityManager.getAudioManager().loadSound("Footsteps", "Sounds/Footsteps.wav", true);
        utilityManager.getAudioManager().loadSound("Door", "Sounds/Door.ogg", false);
        utilityManager.getAudioManager().loadSound("Scream", "Sounds/Scream.wav", false);
        utilityManager.getAudioManager().loadSound("Reloading", "Sounds/Reloading.wav", false);
        utilityManager.getAudioManager().getSound("Ambience").setVolume(.6f);
        utilityManager.getAudioManager().getSound("Ambience").play();
        
    }
    
    private void loadGame() {
        sceneManager.initScene("Scenes/DarkHouse.j3o");
    }
    
    private void createEntityManager() {
        entityManager = new EntityManager(app);
    }
    
    public EntityManager getEntityManager() {
        return entityManager;
    }
    
    private void createUtilityManager() {
        utilityManager = new UtilityManager(app);
    }
    
    public UtilityManager getUtilityManager() {
        return utilityManager;
    }
    
    private void createSceneManager() {
        sceneManager = new SceneManager(app, this);
    }
    
    public SceneManager getSceneManager() {
        return sceneManager;
    }
    
    @Override
    public void update(float tpf) {
        entityManager.update(tpf);
        sceneManager.update(tpf);
    }
    
}
