package mygame.util.script;

import com.jme3.app.state.AppStateManager;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    static        HashMap<Object, Object> GLOBAL_SYM_TAB;
    private final HashMap<String, Object> symTab;
            
    private final Entity          entity;
    private final ScriptManager   scriptManager;
    private float                 proximity;
    private boolean               inProx;
    private boolean               enteredProx;
    private final Player          player;
    private final LinkedHashMap   map;
    
    //Contstructs the Script
    public Script(Entity entity, AppStateManager stateManager, LinkedHashMap map) {
        System.out.println("Constructing Script For: " + entity.getName());
        this.entity   = entity;
        this.map      = map;     
        player        = stateManager.getState(GameManager.class).getEntityManager().getPlayerManager().getPlayer();
        scriptManager = stateManager.getState(GameManager.class).getUtilityManager().getScriptManager();
        symTab        = new HashMap();
    }
    
    //Initializes the Proximity and Start Actions
    public void initialize() {
        setFields();
        setProximity();
        startAction();
    }
    
    private void setFields() {
    
        if (map.get("Fields") == null)
            return;
        
        ArrayList fm = (ArrayList) map.get("Fields");
        scriptManager.getScriptParser().parse(fm, entity);
        
    }
    
    public Map<String, Object> getSymTab() {
        return symTab;
    }
    
    //If there is a proximity map in the script get the Distance
    private void setProximity() {
        
        if (map.get("Proximity") == null)
            return;        
        
        try {
            Map<Object, Object> pm = (Map<Object, Object>) map.get("Proximity");
            proximity              = (Integer) pm.get("Distance");
        }
        
        catch (Exception e){
            System.out.println("Proximity Error For Entity: " + entity.getName());
            e.printStackTrace();
        }
        
    }
    
    //If there is a start action do it on initialize
    public void startAction() {
        
        ArrayList startScript;
        if (map.get("Start") == null)
            return;
            
        try {
            Map<Object, Object> sm = (Map<Object, Object>)  map.get("Start");
            startScript            = (ArrayList)  sm.get("Script");
            scriptManager.getScriptParser().parse(startScript, entity);
        }
        
        catch (Exception e) {
            System.out.println("Start Error For Entity: " + entity.getName());
            e.printStackTrace();
        }

    }
  
    //This Action is Run When Hit is Called. Currently Only With Gun
    public void hitAction() {
        
        if (map.get("Hit") == null)
            return;

        try {
            Map<Object, Object> hm  = (Map<Object, Object>)  map.get("Hit");
            ArrayList hitScript     = (ArrayList) hm.get("Script");
            scriptManager.getScriptParser().parse(hitScript, entity);
        }

        catch (Exception e) {
            System.out.println("Hit Error For Entity: " + entity.getName());
            e.printStackTrace();
        }
        
    }
  
    //Run if the players distance is within the proximity or leaves it
    private void proximityAction() {

        if (map.get("Proximity") == null)
            return;
        
        try {
        
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

                enteredProx          = true;
                
                if (pm.get("Enter") == null)
                    return;
                
                ArrayList enterScript = (ArrayList) pm.get("Enter");
                scriptManager.getScriptParser().parse(enterScript, entity);

            }
            
            //If left prox but has not run. Run leave proximity action
            if (!inProx && enteredProx) {

                //player.getHud().getInfoText().hide();
                enteredProx          = false;
                
                if (pm.get("Exit") == null)
                    return;
                
                ArrayList exitScript = (ArrayList) pm.get("Exit");
                scriptManager.getScriptParser().parse(exitScript, entity);

            }

        }
        
        catch (Exception e) {
            System.out.println("Proximity Error For Entity: " + entity.getName());
            e.printStackTrace();
        }
            
    }
  
    //Returns whether the player is within the entities proximity
    public boolean InProx() {
        return inProx;
    }
    
    //Run when the player interacts within the entities proximity
    private void checkAction() {

        if (map.get("Interact") == null)
            return;
        
        try {
            
            if (inProx) {

                Map<Object, Object> im   = (Map<Object, Object>)  map.get("Interact");
                ArrayList interactScript = (ArrayList) im.get("Script");
                scriptManager.getScriptParser().parse(interactScript, entity);
                player.setHasChecked(false);

            }
            
        }
                
        catch(Exception e) {
            System.out.println("Interact Error For Entity: " + entity.getName());
            e.printStackTrace();
        }
        
    }
  
    //This script is contanstly run on a loop.
    private void loopAction() {

        if (map.get("While") == null)
            return;
        
        try {
            Map<Object, Object> wm          = (Map<Object, Object>)  map.get("While");
            ArrayList whileScript           = (ArrayList) wm.get("Script");
            scriptManager.getScriptParser().parse(whileScript, entity);
        }
        
        catch (Exception e) {
            System.out.println("Loop Error For Entity: " + entity.getName());
            e.printStackTrace();
        }
        
    }    
    
    //Run on loop and checks for player interaction and distance from entity
    public void checkForTriggers() {
        
        if (player.hasChecked()) {
            checkAction();
        }
        
        proximityAction();
        loopAction();
        
    }    
    
}
