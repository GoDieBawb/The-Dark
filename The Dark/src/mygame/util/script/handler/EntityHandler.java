/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.script.handler;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.light.Light;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.animation.Animated;
import mygame.entity.item.Lamp;
import mygame.entity.player.Player;
import mygame.util.script.TagParser;

/**
 *
 * @author root
 */
public class EntityHandler extends CommandHandler {
    
    public EntityHandler(AppStateManager stateManager, TagParser parser) {
        super(stateManager, parser);
    }
    
    @Override
    public boolean handle(String rawCommand, Entity entity) {
        
        Player   player  = stateManager.getState(GameManager.class).getEntityManager().getPlayerManager().getPlayer();
        boolean  handled = true;
        String[] args    =  rawCommand.split(" ");
        String   command = args[0];
        
        //Sets the player's or the entity's model
        switch (command) {
            
            case "on":
            {
                Light l = ((Light) parser.parseTag(stateManager, args[1], entity));
                l.setEnabled(true);
                gm.getSceneManager().getScene().addLight(l);
                break;
            }
            
            case "off":
            {
                Light l = ((Light) parser.parseTag(stateManager, args[1], entity));
                l.setEnabled(false);
                gm.getSceneManager().getScene().removeLight(l);
                ((Light) parser.parseTag(stateManager, args[1], entity)).setEnabled(false);
                break;
            }  
            
            case "setmodel":
                if (args[1].toLowerCase().equals("player")) {
                    player.getModel().removeFromParent();
                    player.setModel(args[2], stateManager);
                }
                
                else {
                    ((Entity) entity).getModel().removeFromParent();
                    ((Entity) entity).setModel(args[1], stateManager);
                }   break;
                
            case "look":
                ((Entity) entity).lookAt(player.getModel().getWorldTranslation().add(0,.3f,0), new Vector3f(0,1,0));
                break;
                
            case "setrotation":
                float xRot = Float.valueOf(args[1]);
                float yRot = Float.valueOf(args[2]);
                float zRot = Float.valueOf(args[3]);
                ((Entity) entity).getModel().setLocalRotation(new Quaternion(0,0,0,1));
                ((Entity) entity).getModel().rotate(xRot,yRot,zRot);
                break;
                
            case "clearrotation":
                ((Entity) entity).getModel().setLocalRotation(new Quaternion(0,0,0,1));
                break;
                
            case "scale":
                float scale = Float.valueOf(args[1]);
                entity.scale(scale);
                break;
                
            case "move":
                
                if (args[1].toLowerCase().equals("player")) {
                    Vector3f spot = (Vector3f) parser.parseTag(stateManager, args[2], entity);
                    player.getPhys().warp(spot);
                }
                
                else {
                    
                    try {
                        
                        Node node     = (Node) parser.parseTag(stateManager, args[1], entity);
                        Vector3f spot = (Vector3f) parser.parseTag(stateManager, args[2], entity);
                        node.setLocalTranslation(spot);
                        
                    }
                    
                    catch (Exception e) {
                        
                        Vector3f spot = (Vector3f) parser.parseTag(stateManager, args[1], entity);
                        ((Entity) entity).setLocalTranslation(spot);
                        
                    }
                }
                
                break;
                
            case "clearlocation": {

                if (args.length == 1) {
                    entity.setLocalTranslation(0, 0, 0);
                }
                
                else {
                    Node node = (Node) parser.parseTag(stateManager, args[1], entity);
                    node.setLocalTranslation(0, 0, 0);
                }
                
                break;
                
            }
                
            case "hide":
                try {
                    
                    if (args[1].contains("entities")) {
                        
                        for (int j = 0; j < ((Entity) entity).getParent().getQuantity(); j++) {
                            
                            String entName = ((Entity) parser.parseTag(stateManager, args[1], entity)).getName();
                            
                            if (((Entity) entity).getParent().getChild(j).getName().equals(entName)) {
                                
                                ((Entity)((Entity) entity).getParent().getChild(j)).hide();
                                
                            }
                            
                        }

                    }

                    else {
                        Entity ent = (Entity) parser.parseTag(stateManager, args[1], entity);
                        ent.hide();
                        //stateManager.getState(SceneManager.class).addPhys();
                    }
                    
                }
                
                catch (Exception e) {
                    entity.setIsHid(true);
                    entity.getChild(0).setLocalTranslation(0,-15,0);
                }   
                
                break;
                
            case "remove":
                entity.setLocalTranslation(0,-15,0);
                entity.removeFromParent();
                if(entity instanceof Lamp)
                    ((Lamp) entity).extinguish();
                break;
                
            case "show":
                
                try {
                    
                    Entity ent = (Entity) parser.parseTag(stateManager, args[1], entity);
                    ent.show();
                    //stateManager.getState(SceneManager.class).addPhys();
                    
                }
                
                catch (Exception e) {
                    
                    ((Entity) entity).show();
                    //gm.getUtilityManager().getPhysicsManager().addPhys();
                    
                }   
                
                break;
            
            case "animate":
                
                if (args.length == 2) {
                    ((Animated) entity).setAnimation(args[1]);
                }
                
                else {
                    
                    if (!args[2].equals("noloop"))
                        System.out.println("Unknown Animate Argument: " + args[2]);
                    
                    else
                        ((Animated) entity).setAnimation(args[1], false);      
                    
                }
                
                break;
                
            case "strike": {
            
                //Gets Collision
                CollisionResults results = new CollisionResults();
                Ray              ray     = new Ray(stateManager.getApplication().getCamera().getLocation(), stateManager.getApplication().getCamera().getDirection());
                Node          entityNode = stateManager.getState(GameManager.class).getEntityManager().getEntityNode();
                 
                //Check to see if it colided with the entity node
                entityNode.collideWith(ray, results);
                 
                if (results.size() > 0) {
                    
                    //Find the Entity Hit
                    Entity hitEntity = findEntity(results.getCollision(1).getGeometry().getParent());
                    
                    float strikeDistance;
                    
                    if (args[1].contains("<") && args[1].contains(">"))
                        strikeDistance = (Float) parser.parseTag(stateManager, args[1], entity);
                    
                    else
                        strikeDistance = Float.valueOf(args[1]);
                    
                    //Run the hit script on the hit entity
                    if (hitEntity != null) {
                        
                        if (results.getClosestCollision().getDistance() < strikeDistance) {
                            hitEntity.getScript().hitAction();
                            hitEntity.setStriker(entity);
                            
                        }
                        
                    }
                    
                }
            
                break;
                
            }
                
            default:
                handled = false;
                break;
                
        }
        
        return handled;
    
    }
    
    //Gets the Entity out of a Node. For Shooting an Entity
    private Entity findEntity(Node node) {
    
        if (node instanceof Entity) { return (Entity) node; }
        
        else if (node != ((SimpleApplication) stateManager.getApplication()).getRootNode()){
            return findEntity(node.getParent());
        }

        else { return null; }
        
    }         
    
}
