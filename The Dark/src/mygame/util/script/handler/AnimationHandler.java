/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.script.handler;

import com.jme3.app.state.AppStateManager;
import mygame.entity.Entity;
import mygame.entity.animation.Animated;
import mygame.entity.animation.Fighter;
import mygame.entity.animation.Living;
import mygame.entity.npc.It;
import mygame.util.script.TagParser;

/**
 *
 * @author root
 */
public class AnimationHandler extends CommandHandler {
    
    public AnimationHandler(AppStateManager stateManager, TagParser parser) {
        super(stateManager, parser);
    }
    
    @Override
    public boolean handle(String rawCommand, Entity entity) {
        
        boolean  handled = true;
        String[] args    =  rawCommand.split(" ");
        String   command = args[0];
            
        switch (command) {
            
            //Runs the entities idle animation
            case"idle":
                
                try {
                    ((Animated) entity).idle();
                }
                
                catch (ClassCastException e) {
                    System.out.println("Error Loading: " + entity.getName());
                    
                    if (entity.getUserData("Type") == null) {
                        System.out.println("This Special Entity has no Type User Data");
                    }
                    
                    else {
                        System.out.println("User Data Type Detected: " + entity.getUserData("Type"));
                        System.out.println("Has this special entity been constructed?");
                    }
                    
                }
                
                break;

            //Animates the entities attack
            case "animateattack":
                
                ((Fighter) entity).attack();
                break;
            
            //Causes the script holding entity to die
            case "die":
            
                if (entity instanceof It) {
                    ((It) entity).die();
                }
                
                else {
                    ((Living) entity).die();
                }
                
                break;
            
            //Clears the entities animations
            case "noanim":
            
                ((Animated) entity).getAnimControl().clearChannels();    
                break;
            
            default:
                handled = false;
                break;
            
        }

        return handled;
        
    }    
    
}
