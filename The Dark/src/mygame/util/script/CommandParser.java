package mygame.util.script;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
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

/**
 *
 * @author Bob
 */
public class CommandParser {

    private final AppStateManager stateManager;
    private final GameManager     gm;
    private final TagParser       parser;
    
    public CommandParser(AppStateManager stateManager) {
    
        this.stateManager = stateManager;
        parser            = new TagParser();
        gm                = stateManager.getState(GameManager.class);
    
    }
    
    //Takes the current line from the script and executes the proper command
    public void parse(ArrayList commands, Entity entity) {
        
        Player  player  = gm.getEntityManager().getPlayerManager().getPlayer();
        
        //Start with assuming there is no condition to run the script
        boolean go      = true;
        boolean met     = false;
        
        for (int i = 0; i < commands.size(); i++) {
            
            if (player.isDead())
                break;
                
            String[] args     = ((String) commands.get(i)).split(" ");
            String   command  = args[0];
            
            //Starts an if logic statement
            if (command.equals("if")) {
                
                //If the condition returns true then go
                if (ifCheck(entity, args)) {
                    go  = true;
                    met = true;
                }
                
                //If not the condition is not met and the commands should not go
                else {
                    go  = false;
                    met = false;
                }
                
            }       
            
            //If the elseif command is run there may be a change in condition
            else if (command.equals("elseif")) {
                
                //If previous condition was not met else if comes into action
                if (!met) {
                    
                    //Check the condiition if true the script can go
                    if (ifCheck(entity, args)) {
                        
                        go  = true;
                        met = true;
                        
                    }
                    
                }
                
                //If the previous condition was met else if does not come into play
                else {
                
                    go  = false;
                    
                }
            
            }
            
            //Else simple flips the met condition
            else if (command.equals("else")) {

                if(met)
                    go = false;
                else
                    go = true;
            
            }
            
            //End of the if statement makes all future commands good to go
            else if (command.equals("end")) {
                
                go = true;    
            
            }
            
            //If not able to go continue onto the next command
            else if(!go) {
                continue;
            }
            
            //Debug command prints a message to the console
            else if (command.equals("debug")) {
                
                String[] a     = ((String) commands.get(i)).split(" ", 2);
                String debugMessage = a[1];
                System.out.println(debugMessage);
                
            }
            
            //Debugs a tag
            else if (command.equals("debugtag")) {
            
                String[] a     = ((String) commands.get(i)).split(" ", 2);
                System.out.println(parser.parseTag(stateManager, a[1], entity));
            
            }
            
            //End the game if the command is finish
            else if (command.equals("finish")) {
            
                ((SimpleApplication) stateManager.getApplication()).getRootNode().detachAllChildren();
                player.getHud().addAlert("Finish", "Thanks for playing");
                
            }
            
            //Fail command causes the player to die
            else if (command.equals("fail")) {
            
                player.die();

            }
            
            //Changes the scene and possibly teleports player
            else if (command.equals("changescene")) {
                
                //File Path for scene and boolean for teleporting
                String  scenePath;
                boolean teleport = false;
                
                //If args are 4 both Directory and Teleport Location
                if (args.length == 4) {
                    scenePath = "Scenes/" + args[1] + "/" + args[2] + ".j3o";
                    teleport = true;
                }
                
                //If Args are 3 Either Directory or Teleport Location
                else if (args.length == 3) {
                    
                    //If last arg contains .location it cannot be a location tag
                    if (!args[args.length -1 ].contains(".location")) {
                        scenePath = "Scenes/" + args[1] + "/" + args[2] + ".j3o";
                    }
                    
                    //The final args must be location
                    else {
                         scenePath = "Scenes/" + args[1] + ".j3o";
                         teleport = true;
                    }
                    
                }
                
                //There is only one argument and that is the direct scene path
                else {
                    scenePath = "Scenes/" + args[1] + ".j3o";
                }
                
                gm.getSceneManager().initScene(scenePath);
                
                //If teleport is true the player will warp to the final tag
                if (teleport)
                    player.getPhys().warp((Vector3f) (parser.parseTag(stateManager, args[args.length-1], entity)));
                    
            }
            
            //Sets the player's or the entity's model
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
            
            //Sets the entities look direction to the player
            else if (command.equals("look")) {
                
                ((Entity) entity).lookAt(player.getModel().getWorldTranslation().add(0,.3f,0), new Vector3f(0,1,0));
                
            }
            
            //Sets the rotation of an entity
            else if (command.equals("setrotation")) {
                
                float xRot = Float.valueOf(args[1]);
                float yRot = Float.valueOf(args[2]);
                float zRot = Float.valueOf(args[3]);
                ((Entity) entity).setLocalRotation(new Quaternion(0,0,0,1));
                ((Entity) entity).rotate(xRot,yRot,zRot);
                
            }            
            
            //Clears the rotation of the entity
            else if (command.equals("clearrotation")) {
                
                ((Entity) entity).getModel().setLocalRotation(new Quaternion(0,0,0,1));
                
            }
            
            //Changes the scale of an entity
            else if (command.equals("scale")) {
                float scale = Float.valueOf(args[1]);
                entity.scale(scale);
            }
            
            //Drops the item in the players hand
            else if (command.equals("drop")) {
            
                player.dropItem();
            
            }
            
            //Puts a string into the players inventory
            else if (command.equals("give")) {
                
                try {
                    int amount = Integer.valueOf(args[2]);
                    player.getInventory().put(args[1], amount);
                }
                
                catch(Exception e) {
                    player.getInventory().put(args[1], 1);
                }
            
            }
            
            //Takes a string from the players inventory
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
            
            //Sends a message to the players Alert Box
            else if (command.equals("chat")) {
            
                String[] a     = ((String) commands.get(i)).split(" ", 2);
                player.getHud().addAlert(((Entity) entity).getName(), a[1]);
            
            }
            
            //Sends a delayed message to the players alert box
            else if (command.equals("delaychat")) {
            
                String[] a     = ((String) commands.get(i)).split(" ", 2);
                player.getHud().addAlert(((Entity) entity).getName(), a[1]);
            
            }
            
            //Moves the player or an entity
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
            
            //Hides an entity or entities by moving them below ground
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
            
            //removes an enitty from the map
            else if (command.equals("remove")) {
                
                entity.setLocalTranslation(0,-15,0);
                entity.removeFromParent();
                
                if(entity instanceof Torch)
                    ((Torch) entity).Extinguish();
                
            }
            
            //Equips an entity to the left side of the player
            else if (command.equals("equipleft")) {
                player.attachChild(entity);
                //((SimpleApplication)stateManager.getApplication()).getGuiNode().attachChild(entity);
                entity.setLocalTranslation(.25f,.6f,.1f);
            }
            
            //Equips an entity to the right side of a player
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
            
            //Shows an entity or entities by moving them to their original location
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
            
            //Runs the entities idle animation
            else if (command.equals("idle")) {
            
                ((Living) entity).idle();
            
            }
            
            //Animates the entities attack
            else if (command.equals("animateattack")) {
                
                ((Fighter) entity).attack();
            
            }
            
            //Causes the script holding entity to die
            else if (command.equals("die")) {
            
                if (entity instanceof It) {
                    ((It) entity).die();
                }
                
                else {
                    ((Living) entity).die();
                }    
                    
            } 
            
            //Clears the entities animations
            else if (command.equals("noanim")) {
            
                ((Animated) entity).getAnimControl().clearChannels();    
            
            }
            
            //If not on the list the command is unknown
            else {
            
                System.out.println("Unknown comand: " + command);
            
            }
            
        }
        
    }
  
  //Determines the truth value of a tag
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
