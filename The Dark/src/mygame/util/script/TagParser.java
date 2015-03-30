/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.script;

import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.HashMap;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.Scriptable;
import mygame.entity.player.Player;

/**
 *
 * @author Bob
 */
public class TagParser {

    public Object parseTag(AppStateManager stateManager, String tag, Scriptable entity) {
        
        String[] args  = tag.split("\\.");
        GameManager gm = stateManager.getState(GameManager.class);
        Object obj     = entity;
    
        for(int i = 0; i < args.length; i++) {
        
            if (args[i].equals("player")) {
                
                obj = gm.getEntityManager().getPlayerManager().getPlayer();
                
            }
            
            else if (args[i].contains("entity") || args[i].contains("entities")) {
            
                if (args[i].contains("#")) {
                
                    String[] strAr = args[i].split("#");
                    obj            = gm.getSceneManager().getScene().getChild(strAr[1]);
                    
                }
                
                else
                obj = entity;
            
            }
            
            else if (args[i].contains("child")) {
            
                String[] strAr = args[i].split("#");
                obj            = (Node) ((Node) obj).getChild(strAr[1]);
            
            }
            
            else if (args[i].equals("dead")) {
            
                obj = (Boolean) ((Player) obj).isDead();
            
            }
            
            else if(args[i].equals("location")) {
            
                obj = ((Node) obj).getLocalTranslation();
                
            }
            
            else if (args[i].equals("distance")) {
            
                String[] strAr  = args[i].split("#");
                Vector3f check  = (Vector3f) parseTag(stateManager, strAr[1], entity);
                obj             = ((Vector3f) obj).distance(check);
            
            }
            
            else if (args[i].equals("north")) {
            
                obj = (Boolean) (((Node) obj).getLocalTranslation().x < 0);
            
            }
            
            else if (args[i].equals("south")) {
            
                obj = (Boolean) (((Node) obj).getLocalTranslation().x > 0);
            
            }
            
            else if (args[i].equals("east")) {
            
                obj = (Boolean) (((Node) obj).getLocalTranslation().z < 0);
            
            }
            
            else if (args[i].equals("west")) {
            
                obj = (Boolean) (((Node) obj).getLocalTranslation().z > 0);
            
            }
            
            else if (args[i].equals("!")) {
            
                if (((Boolean) obj)) {
                
                   obj = (Boolean) false; 
                
                }
                
                else {
                
                    obj = (Boolean) true; 
                
                }
                
                
            }
            
            else if (args[i].equals("ishid")) {
            
                obj = (Boolean) ((Entity) obj).isHid();
            
            }
            
            else if (args[i].equals("inprox")) {
            
                obj = ((Scriptable) obj).inProx();
            
            }
            
            else if(args[i].equals("inventory")) {
            
                obj = ((Player) obj).getInventory();
                
            }
            
            else if(args[i].contains("contains")) {
                
                System.out.println("Checking: " + obj);
                String[] strAr = args[i].split("#");
                obj = ((HashMap<String, Integer>)obj).get(strAr[1]);
                
                if (obj == null)
                    obj = false;
                
                else
                    obj = true;
                
            }
            
            else {
            
                System.out.println("Unkown Tag Argument: " + args[i]);
                
            }
            
            if (i == args.length-1) {
                
                return obj;
                
            }
        
        }
      
      return obj;
        
    }
    
}
