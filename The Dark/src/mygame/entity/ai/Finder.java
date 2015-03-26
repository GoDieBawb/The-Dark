/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.ai;

import com.jme3.app.state.AppStateManager;


/**
 *
 * @author Bawb
 */
public interface Finder {
    
    public void createFinderControl(AppStateManager stateManager);
    
    public FinderControl getFinderControl();
    
}
