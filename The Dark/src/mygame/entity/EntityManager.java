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
import mygame.entity.item.Candle;
import mygame.entity.item.Gun;
import mygame.entity.item.Torch;
import mygame.entity.item.Usable;
import mygame.entity.monster.MonsterManager;
import mygame.entity.npc.It;
import mygame.entity.npc.Npc;
import mygame.entity.player.PlayerManager;
import mygame.scene.SceneManager;
import mygame.util.FileWalker;
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
    private FileWalker        fileWalker;
    
    //Create the Player and Monster Manager
    public EntityManager(SimpleApplication app) {
        this.app = app;
        createPlayerManager();
        createMonsterManager();
        fileWalker = new FileWalker();
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
    
    //Intializes the Entities in The Scene
    public void initEntities(SceneManager sceneManager) {

        System.out.println("Initializing Entities...");
        //Get Scene Node From Scene Manager
        Node scene = sceneManager.getScene();
        
        //Set the Entity Node to the Scene's proper Child
        entityNode = (Node) scene.getChild("Entity Node");
        
        //If there is No Premade Entity Node Make One
        if(entityNode == null) {
            entityNode =  new Node();
        }
        
        //If Not there are Premade entities in the Scene Already
        else {
            
            //Clear the Scene Entities List
            sceneEntities = new ArrayList();
            
            //Iterate over the Entity Node
            for (int i = 0; i < entityNode.getQuantity(); i++) {
                
                //Create Generic Entity and Set the model
                Entity entity = new Entity();
                Node   model  = (Node) entityNode.getChild(i);
                
                //If the Entity has a Special Type Set it
                if (model.getUserData("Type") != null) {
                    
                    String type = model.getUserData("Type");
                    
                    switch (type) {
                        
                        case "Torch":
                            entity = new Torch(app.getStateManager());
                            break;
                            
                        case "Gun":
                            entity = new Gun(app.getStateManager());
                            break;
                            
                        case "Npc":
                            entity = new Npc();
                            break;
                            
                        case "Candle":
                            entity = new Candle(app.getStateManager());
                            break;
                            
                        case "Item":
                            entity = new Usable(app.getStateManager());
                            break;
                            
                        default:
                            System.out.println("Unknown Entity Type: " + type);
                            break;
                            
                    }
                    
                }
                
                //Set the Entities Model Name and Add the Entity itself to the Scene
                entity.setModel(model);
                entity.setName(model.getName());
                
                //Get the Script User Data from the Entity. 
                //Without this Scene Will Crash
                if (model.getUserData("Script") != null) {

                    String fileName = model.getUserData("Script");
                    String filePath = fileWalker.walk("assets/Scripts", fileName);
                           filePath = filePath.split("assets/")[1];

                    //Script are Linked Hashmaps of Strings
                    LinkedHashMap map   = (LinkedHashMap) app.getStateManager()
                                        .getState(GameManager.class)
                                            .getUtilityManager().getYamlManager()
                                                .loadYamlAsset(filePath, app.getStateManager());
                    
                    //Create the Script with the current entity and its map
                    Script script = new Script(entity, app.getStateManager(), map);
                    
                    //Set the script to the entity
                    entity.setScript(script);
                    
                }
                
                sceneEntities.add(entity);

            }
            
            initEntityNode();
            
        }
        
        scene.attachChild(entityNode);
        sceneManager.initLights();
        
    }
    
    //Because the Model is already in the scene the entity must be properly placed
    private void initEntityNode() {
        
        for (int i = 0; i < sceneEntities.size(); i++) {
            
           //Make sure everything in the Entity Node is an Entity
           if(sceneEntities.get(i) instanceof Entity) {
               
               Entity e = (Entity) sceneEntities.get(i);
               //Set the entity to the models translation
               e.setLocalTranslation(e.getModel().getWorldTranslation());
               //Attach the Model to the entity
               e.attachChild(e.getModel());
               //Center the Entity Model on the Entity
               e.getModel().setLocalTranslation(0,0,0);
               //Attach the Entity to the Entity Node
               entityNode.attachChild(e);
               //Intialize the Entities Script
               e.getScript().initialize();
               
               //The Gun needs to initialize its particle Emitter
               if(e instanceof Gun) {
                   ((Gun)e).initModel();
               }
               
           }
           
           //If there is a non entity in the entity node remove it
           else {
               entityNode.getChild(i).removeFromParent();
           }
           
        }
        
        sceneEntities = null;
    
    }
    
    //Return the Entity Node
    public Node getEntityNode() {
        return entityNode;
    }
    
    public void update(float tpf) {
        
        //Update Sub Managers
        monsterManager.update(tpf);
        playerManager.update(tpf);
        
        //Iterate over the entity node for actions
        for (int i = 0; i < entityNode.getQuantity(); i++) {
        
            //If this is not an entity re-init entity Node
            if (!(entityNode.getChild(i) instanceof Entity)) {
                initEntityNode();
                return;
            }
            
            //Set Current Entity
            Entity currentEntity = (Entity) entityNode.getChild(i);
            
            //If entity has update Action Loop its Act
            if (currentEntity instanceof Actor)
                ((Actor) currentEntity).act();
            
            //If the entity is scripted check for its triggers
            if (currentEntity.getScript() != null) {
                currentEntity.getScript().checkForTriggers();
            }
            
            //Make sure all entities have checked for players action before informing nothign
            if (playerManager.getPlayer().hasChecked() && entityNode.getQuantity()-1 == i) {
                playerManager.getPlayer().getHud().addAlert("Nothing", "There doesn't seem to be anything here.");
                playerManager.getPlayer().setHasChecked(false);
            }
            
        }
        
    }
    
}
