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
    private boolean            interact, up, down;
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
        
        else if (im.getIsPressed("Up")) {
            up = true;
        }
        
        else  if (im.getIsPressed("Down")) {
            down = true;
        }
        
        else if (up) {
            menuManager.changeSelection(-1);
            up = false;
        }
        
        else if (down) {
            menuManager.changeSelection(1);
            down = false;
        }
        
        else if (interact) {  
            menuManager.makeSelection();
            interact = false;   
        }
                
    }
    
    public void update() {
        updateKeys();
    }
    
}
