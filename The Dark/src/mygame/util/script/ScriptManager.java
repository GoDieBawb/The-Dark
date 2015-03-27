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
    
    private CommandParser scriptParser;
    
    public ScriptManager(AppStateManager stateManager) {
        scriptParser = new CommandParser(stateManager);
    }
    
    public CommandParser getScriptParser() {
        return scriptParser;
    }
    
}
