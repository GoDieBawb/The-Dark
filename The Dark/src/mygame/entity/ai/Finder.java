package mygame.entity.ai;

import com.jme3.app.state.AppStateManager;


/**
 *
 * @author Bawb
 */
public interface Finder {
    
    //Creates the Finder Control
    public void createFinderControl(AppStateManager stateManager);
    
    //Returns the current objects finder control
    public FinderControl getFinderControl();
    
    //Sets the movement speed at which the finder will run
    public void setMoveSpeed();
    
    //Returns the finders movement speed
    public int getMoveSpeed();
    
}
