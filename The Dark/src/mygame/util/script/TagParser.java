/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.script;

import mygame.util.script.handler.SymbolHandler;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.Scriptable;
import mygame.entity.item.Torch;
import mygame.entity.player.Player;

/**
 *
 * @author Bob
 */
public class TagParser {
    
    //This method returns on object based on the tag string
    public Object parseTag(AppStateManager stateManager, String tag, Scriptable entity) {
        
        //Split the tag on . character
        String[] args  = tag.split("\\.");
        GameManager gm = stateManager.getState(GameManager.class);
        Object obj     = entity;
    
        //Iterate over the list of arguments
        for(int i = 0; i < args.length; i++) {
            
            if (args[i].contains("sym")) {
                String[] strAr = args[i].split("#");
                         obj   = entity.getScript().getSymTab().get(strAr[1]);
            }
            
            else if (args[i].contains("const")) {
                String[] strAr = tag.split("#");
                obj            = checkForConstants(strAr[1]);
                return obj;
            }
            
            //If the current argument is player the object must be the player
            else if (args[i].equals("player")) {
                
                obj = gm.getEntityManager().getPlayerManager().getPlayer();
                
            }
            
            //If the argument is entity or entity 
            else if (args[i].contains("entity") || args[i].contains("entities")) {
            
                //If it contains a # the next string is the name of the entity
                if (args[i].contains("#")) {
                
                    String[] strAr = args[i].split("#");
                    
                    if (strAr[1].equals("model"))
                        obj = ((Entity) entity).getModel();
                    
                    else
                        obj            = gm.getSceneManager().getScene().getChild(strAr[1]);
                    
                    
                }
                
                //If no # the tag is referring to the entity itself
                else
                    obj = entity;
            
            }
            
            //If the argument is child it will be a node by that name
            else if (args[i].contains("child")) {
            
                //The object will be the Node named by the string after the #
                String[] strAr = args[i].split("#");
                obj            = (Node) ((Node) obj).getChild(strAr[1]);
            
            }
            
            //Returns if the player isDead
            else if (args[i].equals("dead")) {
            
                obj = (Boolean) ((Player) obj).isDead();
            
            }
            
            //Returns the currently casted objects location
            else if(args[i].equals("location")) {
            
                obj = ((Node) obj).getLocalTranslation();
                
            }
            
            //Checks the distance between the current vector object and another
            else if (args[i].equals("distance")) {
            
                String[] strAr  = args[i].split("#");
                Vector3f check  = (Vector3f) parseTag(stateManager, strAr[1], entity);
                obj             = ((Vector3f) obj).distance(check);
            
            }
            
            else if (args[i].contains("add")) {
            
                String[] strAr  = args[i].split("#");
                String[] floats = strAr[1].split(",");
                float x         = Float.valueOf(floats[0]);
                float y         = Float.valueOf(floats[1]);
                float z         = Float.valueOf(floats[2]);
                obj             = ((Vector3f) obj).add(x,y,z);
                
            }
            
            //If the object is on the north side of the map
            else if (args[i].equals("north")) {
            
                obj = (Boolean) (((Node) obj).getLocalTranslation().x < 0);
            
            }
            
            //If the object is on the south side of the map
            else if (args[i].equals("south")) {
            
                obj = (Boolean) (((Node) obj).getLocalTranslation().x > 0);
            
            }
            
            //If the object is on the east side of the map
            else if (args[i].equals("east")) {
            
                obj = (Boolean) (((Node) obj).getLocalTranslation().z < 0);
            
            }
            
            //If the object is one the west sdide of the map
            else if (args[i].equals("west")) {
            
                obj = (Boolean) (((Node) obj).getLocalTranslation().z > 0);
            
            }
            
            //Flips a boolean
            else if (args[i].equals("!")) {
            
                if (((Boolean) obj)) {
                
                   obj = (Boolean) false; 
                
                }
                
                else {
                
                    obj = (Boolean) true; 
                
                }
                
                
            }
            
            //Casts to a boolean whether the current entity is hid
            else if (args[i].equals("ishid")) {
            
                obj = (Boolean) ((Entity) obj).isHid();
            
            }
            
            //Casts to a boolean that states whether the player is in a scriptables proximity
            else if (args[i].equals("inprox")) {
            
                obj = ((Scriptable) obj).inProx();
            
            }
            
            //Casts to the players inventory
            else if(args[i].equals("inventory")) {
            
                obj = ((Player) obj).getInventory();
                
            }
            
            //Casts to a boolean that states if a hashmap contains a string
            else if(args[i].contains("contains")) {
                
                String[] strAr = args[i].split("#");
                obj = ((HashMap<String, Integer>)obj).get(strAr[1]);
                
                if (obj == null)
                    obj = false;
                
                else
                    obj = true;
                
            }
            
            //Casts to the players torch
            else if(args[i].contains("torch")) {
            
                if (((Player) obj).getCameraManager().getCameraNode().getChild("Torch") !=null) {
                    obj = ((Player) obj).getCameraManager().getCameraNode().getChild("Torch");
                }
                
                else {
                    obj = null;
                }
                
            }
            
            //Checks whether the current object is lit
            else if (args[i].contains("lit")) {
                
                if (obj == null)
                    obj = false;
                
                else
                    obj = (boolean) ((Torch) obj).getTorchLight().isLit();
                
            }
            
            //If not on the list above it is an unknown tag argument
            else {
            
                System.out.println("Unkown Tag Argument: " + args[i]);
                
            }
            
            //If at the final tag then return the final casted object
            if (i == args.length-1) {
                return obj;
                
            }
        
        }
        
        return obj;
        
    }
    
    private Object checkForConstants(String objString) {
        
        if (objString.matches("[0-9]*")) {
            return Integer.valueOf(objString);
        }
        
        else if (objString.matches("[0-9]*\\.[0-9]*")) {
            return Float.valueOf(objString);
        }
        
        return objString;
        
    }
    
}
