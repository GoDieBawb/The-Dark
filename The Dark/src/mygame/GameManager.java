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
import mygame.menu.MenuManager;
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
    private MenuManager        menuManager;
    
    //Initializes Sub Managers
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication) app;
        createUtilityManager();
        createSceneManager();
        createEntityManager();
        createMenuManager();
        initSounds();
        menuManager.showMenu();
        //startNewGame();
    }
    
    //Loads the Game Sounds into individual Audio Nodes
    private void initSounds() {
    
        utilityManager.getAudioManager().loadSound("Ambience" , "Sounds/Organ.ogg", true);
        utilityManager.getAudioManager().loadSound("Gunshot", "Sounds/Gunshot.ogg", false);
        utilityManager.getAudioManager().loadSound("Empty", "Sounds/Empty.ogg", false);
        utilityManager.getAudioManager().loadSound("Footsteps", "Sounds/Footsteps.wav", true);
        utilityManager.getAudioManager().loadSound("Door", "Sounds/Door.ogg", false);
        utilityManager.getAudioManager().loadSound("Scream", "Sounds/Scream.wav", false);
        utilityManager.getAudioManager().loadSound("Reloading", "Sounds/Reloading.wav", false);
        utilityManager.getAudioManager().getSound("Ambience").setVolume(.6f);
        
    }
    
    //Starts a New Game From the Dark House Scene
    public void startNewGame() {
        sceneManager.initScene("Scenes/TestCell/BobsTestCell.j3o");
        entityManager.getPlayerManager().placePlayer();
        utilityManager.getAudioManager().getSound("Ambience").play();
        menuManager.hideMenu();
        entityManager.getPlayerManager().getPlayer().rotate(0,89,0);
        app.getFlyByCamera().setEnabled(true);
    }
    
    //When the Game is Ended Returns the Player to the Menu
    public void endGame() {
        entityManager.getPlayerManager().getPlayer().getHud().getInfoText().hide();
        utilityManager.getAudioManager().getSound("Ambience").stop();
        menuManager.showMenu();
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
    
    private void createMenuManager() {
        menuManager = new MenuManager(app, this);
    }
    
    public MenuManager getMenuManager() {
        return menuManager;
    }
    
    @Override
    public void update(float tpf) {
        
        //Update the Menu if Enabled
        if (menuManager.isEnabled()) {
            menuManager.update(tpf);
            return;
        }
        
        //Update the Entity and Scene Manager
        entityManager.update(tpf);
        sceneManager.update(tpf);
        
    }
    
}
