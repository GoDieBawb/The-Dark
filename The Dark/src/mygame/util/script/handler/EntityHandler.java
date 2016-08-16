/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.script.handler;

import com.jme3.app.state.AppStateManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.item.Torch;
import mygame.entity.player.Player;
import mygame.util.script.TagParser;

/**
 *
 * @author root
 */
public class EntityHandler extends Handler {
    
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
                System.out.println(xRot + " " + yRot + " " + zRot);
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
                
                else
                    
                    try {
                        
                        Node node     = (Node) parser.parseTag(stateManager, args[1], entity);
                        Vector3f spot = (Vector3f) parser.parseTag(stateManager, args[2], entity);
                        node.setLocalTranslation(spot);
                        
                    }
                    
                    catch (Exception e) {
                        
                        Vector3f spot = (Vector3f) parser.parseTag(stateManager, args[1], entity);
                        ((Entity) entity).setLocalTranslation(spot);
                        //stateManager.getState(SceneManager.class).addPhys();
                        
                    }
                
                    break;
                    
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
                if(entity instanceof Torch)
                    ((Torch) entity).extinguish();
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
                
            default:
                handled = false;
                break;
                
        }
        
        return handled;
    
    }
    
}
