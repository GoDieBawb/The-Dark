/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.script;

import com.jme3.app.state.AppStateManager;
import com.jme3.light.Light;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.Scriptable;
import mygame.entity.item.FireLight;
import mygame.entity.item.Lamp;
import mygame.entity.player.Player;

/**
 *
 * @author Bob
 */
public class TagParser {
    
    //This method returns on object based on the tag string
    public Object parseTag(AppStateManager stateManager, String tag, Scriptable entity) {
        
        //Split the tag on . character and remove <>
        tag            = tag.replace("<", "");
        tag            = tag.replace(">", "");
        String[] args  = tag.split("\\.");
        GameManager gm = stateManager.getState(GameManager.class);
        Object obj     = entity;
    
        //Iterate over the list of arguments
        for(int i = 0; i < args.length; i++) {
            
            String strippedTag = args[i].split("#")[0];
            
            if (strippedTag.equals("sym")) {
                String[] strAr = args[i].split("#");
                obj            = entity.getScript().getSymTab().get(strAr[1]);
            }
            
            else if (strippedTag.equals("const")) {
                String[] strAr = tag.split("#");
                obj            = checkForConstants(strAr[1]);
            }
            
            else if (strippedTag.equals("true")) {
                obj = true;
            }
            
            else if (strippedTag.equals("false")) {
                obj = false;
            }            
            
            //If the current argument is player the object must be the player
            else if (strippedTag.equals("player")) {
                
                obj = gm.getEntityManager().getPlayerManager().getPlayer();
                
            }
            
            //If the argument is entity or entity 
            else if (strippedTag.equals("entity") || strippedTag.equals("entities")) {
            
                //If it contains a # the next string is the name of the entity
                if (args[i].contains("#")) {
                
                    String[] strAr = args[i].split("#");
                    
                    if (strAr[1].equals("model"))
                        obj = ((Entity) entity).getModel();
                    
                    else
                        obj = gm.getSceneManager().getScene().getChild(strAr[1]);
                    
                    
                }
                
                //If no # the tag is referring to the entity itself
                else
                    obj = entity;
            
            }
            
            //If the argument is child it will be a node by that name
            else if (strippedTag.equals("child")) {
            
                //The object will be the Node named by the string after the #
                String[] strAr = args[i].split("#");
                obj            = (Node) ((Node) obj).getChild(strAr[1]);
            
            }
            
            else if (strippedTag.equals("light")) {
                obj = (Light) ((Node) obj).getLocalLightList().get(0);
            }
            
            //Returns if the player isDead
            else if (strippedTag.equals("dead")) {
            
                obj = (Boolean) ((Player) obj).isDead();
            
            }
            
            //Returns the currently casted objects location
            else if(strippedTag.equals("location")) {
            
                obj = ((Node) obj).getLocalTranslation();
                
            }
            
            //Checks the distance between the current vector object and another
            else if (strippedTag.equals("distance")) {
            
                String[] strAr  = args[i].split("#");
                Vector3f check  = (Vector3f) parseTag(stateManager, strAr[1], entity);
                obj             = ((Vector3f) obj).distance(check);
            
            }

            else if (strippedTag.equals("time")) {
                obj = System.currentTimeMillis();
            }
            
            else if (strippedTag.equals("add") || strippedTag.equals("sub")) {
                
                int mult = 1;
                
                if (args[i].contains("sub"))
                    mult = -1;
                
                String[] strAr  = args[i].split("#", 2);
                
                if (obj instanceof Number) {
                        
                    Number num = (Number) parseTag(stateManager, strAr[1], entity);

                    if (obj instanceof Long) {

                        Long a = (Long) obj;

                        if (num instanceof Long) {
                            Long b = (long) num*mult;
                            obj = a+b;
                        }

                        else if (num instanceof Float) {
                            float b = (float) num*mult;
                            obj = a+b;
                        }

                        else if (num instanceof Integer) {
                            int b = (int) num*mult;
                            obj = a+b;
                        }

                    }

                    else if (obj instanceof Float) {

                        Float a = (Float) obj;

                        if (num instanceof Long) {
                            Long b = (long) num*mult;
                            obj = a+b;
                        }

                        else if (num instanceof Float) {
                            float b = (float) num*mult;
                            obj = a+b;
                        }

                        else if (num instanceof Integer) {
                            int b = (int) num*mult;
                            obj = a+b;
                        }

                    }

                    else if (obj instanceof Integer) {

                        int a = (int) obj;

                        if (num instanceof Long) {
                            Long b = (long) num*mult;
                            obj = a+b;
                        }

                        else if (num instanceof Float) {
                            float b = (float) num*mult;
                            obj = a+b;
                        }

                        else if (num instanceof Integer) {
                            int b = (int) num*mult;
                            obj = a+b;
                        }

                    }                        
                    
                }
                
                else if (obj instanceof Vector3f) {
                    String[] floats = tag.split(strippedTag)[1].split("#")[1].split(",");
                    float x         = Float.valueOf(floats[0])*mult;
                    float y         = Float.valueOf(floats[1])*mult;
                    float z         = Float.valueOf(floats[2])*mult;
                    obj             = ((Vector3f) obj).add(x,y,z);
                }
                
            }
            
            //If the object is on the north side of the map
            else if (strippedTag.equals("north")) {
            
                obj = (Boolean) (((Node) obj).getLocalTranslation().x < 0);
            
            }
            
            //If the object is on the south side of the map
            else if (strippedTag.equals("south")) {
            
                obj = (Boolean) (((Node) obj).getLocalTranslation().x > 0);
            
            }
            
            //If the object is on the east side of the map
            else if (strippedTag.equals("east")) {
            
                obj = (Boolean) (((Node) obj).getLocalTranslation().z < 0);
            
            }
            
            //If the object is one the west sdide of the map
            else if (strippedTag.equals("west")) {
            
                obj = (Boolean) (((Node) obj).getLocalTranslation().z > 0);
            
            }
            
            //Flips a boolean
            else if (strippedTag.equals("!")) {
            
                if (((Boolean) obj)) {
                
                   obj = (Boolean) false; 
                
                }
                
                else {
                
                    obj = (Boolean) true; 
                
                }
                
                
            }
            
            //Casts to a boolean whether the current entity is hid
            else if (strippedTag.equals("ishid")) {
            
                obj = (Boolean) ((Entity) obj).isHid();
            
            }
            
            //Casts to a boolean that states whether the player is in a scriptables proximity
            else if (strippedTag.equals("inprox")) {
            
                obj = ((Scriptable) obj).inProx();
            
            }
            
            //Casts to the players flag list
            else if(strippedTag.equals("flags")) {
            
                obj = ((Player) obj).getFlagList();
                
            }
            
            else if (strippedTag.equals("inventory")) {
                obj = ((Player) obj).getInventory();
            }
            
            //Casts to boolean whether player is holding object in left hand
            else if (strippedTag.equals("hasleft")) {
                obj = ((Player) obj).hasLeft();
            }
            
            //Casts to boolean determining whther player is holding object in right hand
            else if (strippedTag.equals("hasright")) {
                obj = ((Player) obj).hasRight();
            }            
            
            //Casts to a boolean that states if a hashmap contains a string
            else if(strippedTag.equals("contains")) {
                
                String[] strAr = args[i].split("#");
                obj = ((HashMap<Object, Integer>)obj).get(strAr[1]);
                
                if (obj == null)
                    obj = false;
                
                else
                    obj = true;
                
            }
            
            //Casts to the players torch
            else if(strippedTag.equals("torch")) {
            
                if (((Player) obj).getCameraManager().getCameraNode().getChild("Torch") !=null) {
                    obj = ((Player) obj).getCameraManager().getCameraNode().getChild("Torch");
                }
                
                else {
                    obj = null;
                }
                
            }
            
            //Checks whether the current object is lit
            else if (strippedTag.equals("lit")) {
                
                if (obj == null)
                    obj = false;
                
                else {
                    obj = (boolean) ((FireLight)((Lamp) obj).getLight()).isLit();
                }
                
            }
            
            else if (strippedTag.equals("sym")) {
                String[] strAr = args[i].split("#");
                obj = ((Entity) obj).getScript().getSymTab().get(strAr[1]);
            }
            
            else if (strippedTag.equals("global")) {
                String[] strAr = args[i].split("#");
                obj = Script.GLOBAL_SYM_TAB.get(strAr[1]);
            }
            
            //If not on the list above it is an unknown tag argument
            else {
            
                System.out.println("Unkown Tag Argument: " + strippedTag);
                
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
