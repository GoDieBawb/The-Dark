/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity;

import com.jme3.app.SimpleApplication;
import mygame.entity.monster.MonsterManager;
import mygame.entity.player.PlayerManager;

/**
 *
 * @author Bawb
 */
public class EntityManager {
    
    private PlayerManager     playerManager;
    private MonsterManager    monsterManager;
    private SimpleApplication app;
    
    public EntityManager(SimpleApplication app) {
        this.app = app;
        createPlayerManager();
        createMonsterManager();
    }
    
    private void createPlayerManager() {
        playerManager = new PlayerManager(app);
    }
    
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    
    private void createMonsterManager() {
        monsterManager = new MonsterManager(app);
    }
    
    public MonsterManager getMonsterManager() {
        return monsterManager;
    }
    
    public void update(float tpf) {
        monsterManager.update(tpf);
        playerManager.update(tpf);
    }
    
}
