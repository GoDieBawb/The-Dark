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
    
    public Script(Entity entity, AppStateManager stateManager, LinkedHashMap map) {
        this.entity   = entity;
        this.map      = map;     
        player        = stateManager.getState(GameManager.class).getEntityManager().getPlayerManager().getPlayer();
        scriptManager =  stateManager.getState(GameManager.class).getUtilityManager().getScriptManager();
    }
    
    public void initialize() {
        setProximity();
        startAction();
    }
    
    private void setProximity() {
        
        try {
            Map<Object, Object> pm = (Map<Object, Object>) map.get("Proximity");
            proximity              = (Integer) pm.get("Distance");
        }
        
        catch (Exception e){
        }
        
    }
    
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
  
    public void hitAction() {
        
        if (map.get("Hit") != null) {

            Map<Object, Object> hm  = (Map<Object, Object>)  map.get("Hit");
            ArrayList hitScript     = (ArrayList) hm.get("Script");
            scriptManager.getScriptParser().parse(hitScript, entity);

        }

    }
  
    private void proximityAction() {

        float distance  = player.getLocalTranslation().distance(entity.getLocalTranslation());
        Map<Object, Object> pm = (Map<Object, Object>)  map.get("Proximity");

        if (proximity > distance) {
            inProx = true;
        }
            
        if (proximity < distance) {
            inProx = false;
        }

        if (inProx && !enteredProx) {

            enteredProx           = true;
            ArrayList enterScript = (ArrayList) pm.get("Enter");
            scriptManager.getScriptParser().parse(enterScript, entity);

        }

        if (!inProx && enteredProx) {

            player.getHud().getInfoText().hide();
            enteredProx          = false;
            ArrayList exitScript = (ArrayList) pm.get("Exit");
            scriptManager.getScriptParser().parse(exitScript, entity);

        }

    }
  
    public boolean InProx() {
        return inProx;
    }
    
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
        
        }
        
    }
  
    private void loopAction() {

        Map<Object, Object> wm          = (Map<Object, Object>)  map.get("While");
        ArrayList whileScript           = (ArrayList) wm.get("Script");
        scriptManager.getScriptParser().parse(whileScript, entity);

    }    
    
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
