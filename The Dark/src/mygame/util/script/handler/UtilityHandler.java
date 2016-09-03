/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.script.handler;

import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.player.Player;
import mygame.util.script.TagParser;

/**
 *
 * @author root
 */
public class UtilityHandler extends CommandHandler {
    
    public UtilityHandler(AppStateManager stateManager, TagParser parser) {
        super(stateManager, parser);
    }
    
    @Override
    public boolean handle(String rawCommand, Entity entity) {
        
        Player   player  = stateManager.getState(GameManager.class).getEntityManager().getPlayerManager().getPlayer();
        boolean  handled = true;
        String[] args    =  rawCommand.split(" ");
        String   command = args[0];
        
        //Debug command prints a message to the console
        switch (command) {
            
            case "playsound": {
                stateManager.getState(GameManager.class).getUtilityManager().getAudioManager().getSound(args[1]).playInstance();
                break;
            }
            
            case "debug":
                {
                    String[] a     = rawCommand.split(" ", 2);
                    String debugMessage = a[1];
                    System.out.println(debugMessage);
                    break;
                }
                
            case "debugtag":
                {
                    String[] a     = rawCommand.split(" ", 2);
                    System.out.println(parser.parseTag(stateManager, a[1], entity));
                    break;
                }
                
            case "changescene":
                
                //File Path for scene and boolean for teleporting
                String  scenePath;
                boolean teleport = false;
                
                //If args are 4 both Directory and Teleport Location
                switch (args.length) {
                    
                    case 4:
                        scenePath = "Scenes/" + args[1] + "/" + args[2] + ".j3o";
                        teleport = true;
                        break;
                    case 3:
                        //If last arg contains .location it cannot be a location tag
                        if (!args[args.length -1 ].contains(".location")) {
                            scenePath = "Scenes/" + args[1] + "/" + args[2] + ".j3o";
                        }
                        
                        //The final args must be location
                        else {
                            scenePath = "Scenes/" + args[1] + ".j3o";
                            teleport = true;
                        }   break;
                    default:
                        scenePath = "Scenes/" + args[1] + ".j3o";
                        break;
                        
                }   
                
                gm.getSceneManager().initScene(scenePath);
                
                //If teleport is true the player will warp to the final tag
                if (teleport)
                    player.getPhys().warp((Vector3f) (parser.parseTag(stateManager, args[args.length-1], entity)));
                
                break;
                
            default:
                handled = false;
                break;
                
        }
        
        return handled;
        
    }
    
}
