/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import mygame.entity.player.Player;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import mygame.entity.EntityManager;
import mygame.entity.monster.Zombie;
import mygame.scene.SceneManager;
import mygame.util.UtilityManager;

/**
 *
 * @author Bawb
 */
public class GameManager extends AbstractAppState {
    
    private Player             player;
    private Zombie             zombie;
    private SimpleApplication  app;
    private UtilityManager     utilityManager;
    private EntityManager      entityManager;
    private SceneManager       sceneManager;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication) app;
        createUtilityManager();
        createEntityManager();
        createSceneManager();
        loadGame();
    }
    
    private void loadGame() {
        sceneManager.initScene();
        entityManager.getPlayerManager().placePlayer();
        entityManager.getMonsterManager().createZombie();
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
    }
    
}
