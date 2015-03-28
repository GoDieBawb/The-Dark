/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import java.util.LinkedHashMap;
import mygame.GameManager;
import mygame.entity.monster.MonsterManager;
import mygame.entity.player.PlayerManager;
import mygame.util.script.Script;

/**
 *
 * @author Bawb
 */
public class EntityManager {
    
    private PlayerManager     playerManager;
    private MonsterManager    monsterManager;
    private Node              entityNode;
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
        monsterManager = new MonsterManager(app, this);
    }
    
    public MonsterManager getMonsterManager() {
        return monsterManager;
    }
    
    public void initEntities(Node scene) {

        entityNode = (Node) scene.getChild("Entity Node");
        
        if(entityNode == null) {
            entityNode =  new Node();
        }
        
        else {
            
            for (int i = 0; i < entityNode.getQuantity(); i++) {
                
                Entity entity = new Entity();
                Node   model  = (Node) entityNode.getChild(i);
                       
                if (model.getUserData("Type") != null) {
                    
                }
                
                else {
                    entity = new Entity();
                }
                
                if (model.getUserData("Script") != null) {

                    String filePath = "Assets/Scripts/" + model
                                        .getUserData("Script") + ".yml";
                    
                    LinkedHashMap map   = (LinkedHashMap) app.getStateManager()
                                        .getState(GameManager.class)
                                            .getUtilityManager().getYamlManager()
                                                .loadYaml(filePath);
                    
                    Script script = new Script(entity, app.getStateManager(), map);
                    
                    entity.setScript(script);
                    
                    
                }
                
                entity.setLocalTranslation(model.getWorldTranslation());
                entity.attachChild(model);
                model.setLocalTranslation(0,0,0);
                entityNode.attachChild(entity);
        
            }
            
        }
        
        scene.attachChild(entityNode);
        
    }
    
    public Node getEntityNode() {
        return entityNode;
    }
    
    public void update(float tpf) {
        
        monsterManager.update(tpf);
        playerManager.update(tpf);
        
        for (int i = 0; i < entityNode.getQuantity(); i++) {
        
            Entity currentEntity = (Entity) entityNode.getChild(i);
            
            if (currentEntity instanceof Actor)
                ((Actor) currentEntity).act();
            
            if (currentEntity.getScript() != null) {
                currentEntity.getScript().checkForTriggers();
            }
            
            if (playerManager.getPlayer().hasChecked() && entityNode.getQuantity()-1 == i) {
                playerManager.getPlayer().setHasChecked(false);
                playerManager.getPlayer().getHud().showAlert("Nothing", "There doesn't seem to be anything here.");
            }
            
        }
        
    }
    
}
