/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.script;

import com.jme3.app.state.AppStateManager;

/**
 *
 * @author Bawb
 */
public class ScriptManager {
    
    private final CommandParser scriptParser;
    
    //This Object Pretty Much Holds the Command Parser
    public ScriptManager(AppStateManager stateManager) {
        scriptParser = new CommandParser(stateManager);
    }
    
    //Gets the Script Parser
    public CommandParser getScriptParser() {
        return scriptParser;
    }
    
}
