/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import mygame.GameManager;
import mygame.entity.item.Gun;
import mygame.entity.item.Torch;
import mygame.entity.monster.MonsterManager;
import mygame.entity.player.PlayerManager;
import mygame.scene.SceneManager;
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
    private ArrayList<Entity> sceneEntities;
    
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
    
    public void initEntities(SceneManager sceneManager) {

        Node scene = sceneManager.getScene();
        
        entityNode = (Node) scene.getChild("Entity Node");
        
        if(entityNode == null) {
            entityNode =  new Node();
        }
        
        else {
            
            sceneEntities = new ArrayList();
            
            for (int i = 0; i < entityNode.getQuantity(); i++) {
                
                Entity entity = new Entity();
                Node   model  = (Node) entityNode.getChild(i);
                       
                if (model.getUserData("Type") != null) {
                    
                    if (model.getUserData("Type").equals("Torch"))
                        entity = new Torch(app, scene);
                    
                    else if (model.getUserData("Type").equals("Gun"))
                        entity = new Gun(app.getStateManager()
                                        .getState(GameManager.class)
                                            .getUtilityManager()
                                                .getAudioManager());
                    
                }
                
                if (model.getUserData("Script") != null) {

                    String filePath = "Scripts/" + model
                                        .getUserData("Script") + ".yml";
                    
                    LinkedHashMap map   = (LinkedHashMap) app.getStateManager()
                                        .getState(GameManager.class)
                                            .getUtilityManager().getYamlManager()
                                                .loadYamlAsset(filePath, app.getStateManager());
                    
                    Script script = new Script(entity, app.getStateManager(), map);
                    
                    entity.setScript(script);
                    
                }
                
                entity.setModel(model);
                entity.setName(model.getName());
                sceneEntities.add(entity);

            }
            
            initEntityNode();
            
        }
        
        scene.attachChild(entityNode);
        sceneManager.initLights();
        
    }
    
    private void initEntityNode() {
        
        for (int i = 0; i < sceneEntities.size(); i++) {
            
           if(sceneEntities.get(i) instanceof Entity) {
               Entity e = (Entity) sceneEntities.get(i);
               e.setLocalTranslation(e.getModel().getWorldTranslation());
               e.attachChild(e.getModel());
               e.getModel().setLocalTranslation(0,0,0);
               entityNode.attachChild(e);
               e.getScript().initialize();
               
               if(e instanceof Gun) {
                   ((Gun)e).initModel();
               }
               
           }
           
           else {
               entityNode.getChild(i).removeFromParent();
           }
           
        }
        
        sceneEntities = null;
    
    }
    
    public Node getEntityNode() {
        return entityNode;
    }
    
    public void update(float tpf) {
        
        monsterManager.update(tpf);
        playerManager.update(tpf);
        
        for (int i = 0; i < entityNode.getQuantity(); i++) {
        
            if (!(entityNode.getChild(i) instanceof Entity)) {
                initEntityNode();
                return;
            }
            
            Entity currentEntity = (Entity) entityNode.getChild(i);
                
            if (currentEntity instanceof Actor)
                ((Actor) currentEntity).act();
            
            if (currentEntity.getScript() != null) {
                currentEntity.getScript().checkForTriggers();
            }
            
            if (playerManager.getPlayer().hasChecked() && entityNode.getQuantity()-1 == i) {
                playerManager.getPlayer().getHud().addAlert("Nothing", "There doesn't seem to be anything here.");
                playerManager.getPlayer().setHasChecked(false);
            }
            
        }
        
    }
    
}
