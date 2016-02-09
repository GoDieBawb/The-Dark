package mygame.util.script;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import mygame.GameManager;
import mygame.entity.animation.Animated;
import mygame.entity.Entity;
import mygame.entity.animation.Living;
import mygame.entity.Scriptable;
import mygame.entity.animation.Fighter;
import mygame.entity.item.Gun;
import mygame.entity.item.Torch;
import mygame.entity.npc.It;
import mygame.entity.player.Player;
import org.lwjgl.opengl.Display;

/**
 *
 * @author Bob
 */
public class CommandParser {

    private AppStateManager stateManager;
    private GameManager     gm;
    private TagParser       parser;
    
    public CommandParser(AppStateManager stateManager) {
    
        this.stateManager = stateManager;
        parser            = new TagParser();
        gm                = stateManager.getState(GameManager.class);
    
    }
    
    public void parse(ArrayList commands, Entity entity) {
        
        Player  player  = gm.getEntityManager().getPlayerManager().getPlayer();
        boolean go      = true;
        boolean met     = false;
        
        for (int i = 0; i < commands.size(); i++) {
            
            if (player.isDead())
                break;
                
            String[] args     = ((String) commands.get(i)).split(" ");
            String   command  = args[0];
            
            if (command.equals("if")) {
                
                if (ifCheck(entity, args)) {
                    go  = true;
                    met = true;
                }
                
                else {
                    go  = false;
                    met = false;
                }
                
            }       
            
            else if (command.equals("elseif")) {
                
                if (!met) {
                    
                    if (ifCheck(entity, args)) {
                        
                        go  = true;
                        met = true;
                        
                    }
                    
                }
                
                else {
                
                    go  = false;
                    
                }
            
            }
            
            else if (command.equals("else")) {

                if(met)
                go = false;
                else
                go = true;
            
            }
            
            else if (command.equals("end")) {
                
                go = true;    
            
            }
            
            else if(!go) {
                continue;
            }
            
            else if (command.equals("debug")) {
                
                String[] a     = ((String) commands.get(i)).split(" ", 2);
                String debugMessage = a[1];
                System.out.println(debugMessage);
                
            }
            
            else if (command.equals("debugtag")) {
            
                String[] a     = ((String) commands.get(i)).split(" ", 2);
                System.out.println(parser.parseTag(stateManager, a[1], entity));
            
            }
            
            else if (command.equals("finish")) {
            
                ((SimpleApplication) stateManager.getApplication()).getRootNode().detachAllChildren();
                player.getHud().addAlert("Finish", "Thanks for playing");
                
            }
            
            else if (command.equals("fail")) {
            
                player.die();

            }
            
            else if (command.equals("changescene")) {
            
                String scenePath = "Scenes/" + args[1] + ".j3o";
                gm.getSceneManager().initScene(scenePath);
                    
                try {
                    player.getPhys().warp((Vector3f) (parser.parseTag(stateManager, args[2], entity)));
                }
                    
                catch(Exception e) {
                }
                    
            }
            
            else if (command.equals("setmodel")) {
            
                if (args[1].toLowerCase().equals("player")) {
                    player.getModel().removeFromParent();
                    player.setModel(args[2], stateManager);
                }
                
                else {
                    ((Entity) entity).getModel().removeFromParent();
                    ((Entity) entity).setModel(args[1], stateManager);
                }
                
                
            
            }
            
            else if (command.equals("look")) {
                
                ((Entity) entity).lookAt(player.getModel().getWorldTranslation().add(0,.3f,0), new Vector3f(0,1,0));
                
            }
            
            else if (command.equals("setrotation")) {
                
                float xRot = Float.valueOf(args[1]);
                float yRot = Float.valueOf(args[2]);
                float zRot = Float.valueOf(args[3]);
                ((Entity) entity).setLocalRotation(new Quaternion(0,0,0,1));
                ((Entity) entity).rotate(xRot,yRot,zRot);
                
            }            
            
            else if (command.equals("clearrotation")) {
                
                ((Entity) entity).getModel().setLocalRotation(new Quaternion(0,0,0,1));
                
            }
            
            else if (command.equals("scale")) {
                float scale = Float.valueOf(args[1]);
                entity.scale(scale);
            }
            
            else if (command.equals("drop")) {
            
                player.dropItem();
            
            }
            
            else if (command.equals("give")) {
                
                try {
                    int amount = Integer.valueOf(args[2]);
                    player.getInventory().put(args[1], amount);
                }
                
                catch(Exception e) {
                    player.getInventory().put(args[1], 1);
                }
            
            }
            
            else if (command.equals("take")) {
                
                try {
                    int amount    = Integer.valueOf(args[2]);
                    int newAmount = ((Integer) player.getInventory().get(args[1])) - amount;
                    player.getInventory().put(args[1], newAmount);
                }
              
                catch(Exception e) {
                    player.getInventory().remove(args[1]);
                }
            
            }
            
            else if (command.equals("chat")) {
            
                String[] a     = ((String) commands.get(i)).split(" ", 2);
                player.getHud().addAlert(((Entity) entity).getName(), a[1]);
            
            }
            
            else if (command.equals("delaychat")) {
            
                String[] a     = ((String) commands.get(i)).split(" ", 2);
                player.getHud().addAlert(((Entity) entity).getName(), a[1]);
            
            }
            
            else if (command.equals("move")) {
            
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
            
            }
            
            else if (command.equals("hide")) {
            
                try {
                    
                    if (args[1].contains("entities")) {
                        
                        for (int j = 0; j < ((Entity) entity).getParent().getQuantity(); j++) {
                        
                            String entName = ((Entity) parser.parseTag(stateManager, args[1], entity)).getName();
                            
                            if (((Entity) entity).getParent().getChild(i).getName().equals(entName)) {
                                
                                ((Entity)((Entity) entity).getParent().getChild(i)).hide();
                                
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
                
            }              
            
            else if (command.equals("remove")) {
                
                entity.setLocalTranslation(0,-15,0);
                entity.removeFromParent();
                
                if(entity instanceof Torch)
                    ((Torch) entity).Extinguish();
                
            }
            
            else if (command.equals("equipleft")) {
                player.attachChild(entity);
                //((SimpleApplication)stateManager.getApplication()).getGuiNode().attachChild(entity);
                entity.setLocalTranslation(.25f,.6f,.1f);
            }
            
            else if (command.equals("equipright")) {
                
                player.attachChild(entity);
                //((SimpleApplication)stateManager.getApplication()).getGuiNode().attachChild(entity);
                entity.setLocalTranslation(-.25f,.6f,.25f);
            
                if(entity instanceof Gun) {
                    System.out.println("Equipping Gun!");
                    ((Gun) entity).setEquipped(true);
                    player.getHud().attachCrossHair();
                }
                
            }
            
            else if (command.equals("show")) {
            
                try {
                    
                    Entity ent = (Entity) parser.parseTag(stateManager, args[1], entity);
                    ent.show();
                    //stateManager.getState(SceneManager.class).addPhys();
                    
                }    
               
                catch (Exception e) {
                    
                    ((Entity) entity).show();
                    //gm.getUtilityManager().getPhysicsManager().addPhys();
                
                }  
                
            }  
            
            else if (command.equals("idle")) {
            
                ((Living) entity).idle();
            
            }
            
            else if (command.equals("animateattack")) {
                
                ((Fighter) entity).attack();
            
            }
            
            else if (command.equals("die")) {
            
                if (entity instanceof It) {
                    ((It) entity).die();
                }
                
                else {
                    ((Living) entity).die();
                }    
                    
            } 
            
            else if (command.equals("noanim")) {
            
                ((Animated) entity).getAnimControl().clearChannels();    
            
            }
            
            else {
            
                System.out.println("Unknown comand: " + command);
            
            }
            
        }
        
    }
  
  private boolean ifCheck(Scriptable entity, String[] args) {
      
      Object comp1 = null;
      Object comp2 = null;
      String comp  = null;
      
      try {
          
          comp1      = parser.parseTag(stateManager, args[1], entity);
          comp2      = parser.parseTag(stateManager, args[3], entity);
          comp       = args[2];
          
      }
      catch (Exception e) {
      }
      
      boolean truthVal  = false;
      
      if (comp == null) {
      
          if (comp1 instanceof Boolean) {
              
              truthVal = (Boolean) comp1;
              
          }
          
          else {
          
              truthVal = false;
          
          }
          
      }
      
      else if (comp.equals("&&")) {
          
          if (comp1 instanceof Boolean && comp2 instanceof Boolean) {
              
              Boolean a = (Boolean) comp1;
              Boolean b = (Boolean) comp2;
              
              if (a&&b) {
              truthVal = true;
              }
              else{
              truthVal = false;
              }
          
          }
          
          else {
          
              truthVal = false;
          
          }
          
          return truthVal;
          
      }
      
      else if (comp.equals("!!")) {
          
          if (comp1 instanceof Boolean && comp2 instanceof Boolean) {
              
              Boolean a = (Boolean) comp1;
              Boolean b = (Boolean) comp2;
              
              if (!a && !b) {
              truthVal = true;
              }
              else{
              truthVal = false;
              }
          
          }
          
          else {
          
              truthVal = false;
          
          }
          
          return truthVal;
          
      }
      
      else if (comp.equals("!&")) {
          
          if (comp1 instanceof Boolean && comp2 instanceof Boolean) {
              
              Boolean a = (Boolean) comp1;
              Boolean b = (Boolean) comp2;
              
              if (a && !b) {
              truthVal = false;
              }
              else{
              truthVal = true;
              }
              
            return truthVal;  
          
          }
          

          
          else {
          
              truthVal = true;
          
          }
          
          return truthVal;
          
      }
      
      else if (comp.equals("==")) {
          
          //if (comp1.getClass().getSimpleName().equals(comp2.getClass().getSimpleName())) {
          if (comp1.equals(comp2)) {
          
              if (comp1 ==  comp2) {
                  
                  truthVal = true;
                  
              }
              
              else {
              
                  truthVal = false;
              
              }
          
          }
          
          else {
          
              truthVal = false;
          
          }
          
      }
      
    return truthVal;
    
    }
        
  }
