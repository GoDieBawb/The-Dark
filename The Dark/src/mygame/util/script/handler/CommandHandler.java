/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.script.handler;

import com.jme3.app.state.AppStateManager;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.util.script.TagParser;

/**
 *
 * @author root
 */
public abstract class CommandHandler {
    
    protected final AppStateManager stateManager;
    protected final GameManager     gm;
    protected final TagParser       parser;
    
    public CommandHandler(AppStateManager stateManager, TagParser parser) {
        this.stateManager = stateManager;
        this.parser       = parser;
        gm                = stateManager.getState(GameManager.class);
    }
    
    public abstract boolean handle(String rawCommand, Entity entity);
    
}
