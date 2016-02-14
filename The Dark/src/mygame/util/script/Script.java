/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.script;

import com.jme3.app.state.AppStateManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.player.Player;


/**
 *
 * @author Bawb
 */

public class Script {
    
    private final Entity          entity;
    private final ScriptManager   scriptManager;
    private float                 proximity;
    private boolean               inProx;
    private boolean               enteredProx;
    private final Player          player;
    private final LinkedHashMap   map;
    
    //Contstructs the Script
    public Script(Entity entity, AppStateManager stateManager, LinkedHashMap map) {
        this.entity   = entity;
        this.map      = map;     
        player        = stateManager.getState(GameManager.class).getEntityManager().getPlayerManager().getPlayer();
        scriptManager =  stateManager.getState(GameManager.class).getUtilityManager().getScriptManager();
    }
    
    //Initializes the Proximity and Start Actions
    public void initialize() {
        setProximity();
        startAction();
    }
    
    //If there is a proximity map in the script get the Distance
    private void setProximity() {
        
        try {
            Map<Object, Object> pm = (Map<Object, Object>) map.get("Proximity");
            proximity              = (Integer) pm.get("Distance");
        }
        
        catch (Exception e){
        }
        
    }
    
    //If there is a start action do it on initialize
    public void startAction() {

        ArrayList startScript;
        

        try {
            Map<Object, Object> sm = (Map<Object, Object>)  map.get("Start");
            startScript            = (ArrayList)  sm.get("Script");
            scriptManager.getScriptParser().parse(startScript, entity);
        }
        
        catch (Exception e) {
        }

    }
  
    //This Action is Run When Hit is Called. Currently Only With Gun
    public void hitAction() {
        
        if (map.get("Hit") != null) {

            Map<Object, Object> hm  = (Map<Object, Object>)  map.get("Hit");
            ArrayList hitScript     = (ArrayList) hm.get("Script");
            scriptManager.getScriptParser().parse(hitScript, entity);

        }

    }
  
    //Run if the players distance is within the proximity or leaves it
    private void proximityAction() {

        float distance  = player.getLocalTranslation().distance(entity.getLocalTranslation());
        Map<Object, Object> pm = (Map<Object, Object>)  map.get("Proximity");

        if (proximity > distance) {
            inProx = true;
        }
            
        if (proximity < distance) {
            inProx = false;
        }

        //If in prox but has not yet run. Run Proximity Action
        if (inProx && !enteredProx) {

            enteredProx           = true;
            ArrayList enterScript = (ArrayList) pm.get("Enter");
            scriptManager.getScriptParser().parse(enterScript, entity);

        }

        //If left prox but has not run. Run leave proximity action
        if (!inProx && enteredProx) {

            player.getHud().getInfoText().hide();
            enteredProx          = false;
            ArrayList exitScript = (ArrayList) pm.get("Exit");
            scriptManager.getScriptParser().parse(exitScript, entity);

        }

    }
  
    //Returns whether the player is within the entities proximity
    public boolean InProx() {
        return inProx;
    }
    
    //Run when the player interacts within the entities proximity
    private void checkAction() {

        try {
            
            if (inProx) {

                Map<Object, Object> im   = (Map<Object, Object>)  map.get("Interact");
                ArrayList interactScript = (ArrayList) im.get("Script");
                scriptManager.getScriptParser().parse(interactScript, entity);
                player.setHasChecked(false);

            }
            
        }
                
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
  
    //This script is contanstly run on a loop.
    private void loopAction() {

        Map<Object, Object> wm          = (Map<Object, Object>)  map.get("While");
        ArrayList whileScript           = (ArrayList) wm.get("Script");
        scriptManager.getScriptParser().parse(whileScript, entity);

    }    
    
    //Run on loop and checks for player interaction and distance from entity
    public void checkForTriggers() {
        
        if (player.hasChecked()) {
            checkAction();
        }
        
        try {
            proximityAction();
            loopAction();
        }
        
        catch(Exception e) {
        
        }
        
    }    
    
}
