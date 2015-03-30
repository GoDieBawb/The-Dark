package mygame.menu;

import com.jme3.app.state.AppStateManager;
import mygame.GameManager;
import mygame.util.InteractionManager;

/**
 *
 * @author Bawb
 */
public class MenuControlListener {
    
    private InteractionManager im;
    private boolean            interact;
    private MenuManager        menuManager;
    
    public MenuControlListener(AppStateManager stateManager, MenuManager menuManager) {
        GameManager gm    = stateManager.getState(GameManager.class);
        im                = gm.getUtilityManager().getInteractionManager();
        this.menuManager  = menuManager;
    }
    
    private void updateKeys() {
    
        if (im.getIsPressed("Interact")) {
            interact = true;
        }
            
        else if (interact) {  
            menuManager.startGame();
            interact = false;   
        }
                
    }
    
    public void update() {
        updateKeys();
    }
    
}
