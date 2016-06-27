/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;
import mygame.GameManager;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bawb
 */
public class GuiManager {
    
    private Screen                  screen;
    private final SimpleApplication app;
    
    public GuiManager(SimpleApplication app) {
        this.app     = app;
        createScreen();
    }
    
    //Creates the Screen Used for the Players Heads Up Display
    private void createScreen() {
        
        screen = new Screen(app, "tonegod/gui/style/atlasdef/style_map.gui.xml");
        screen.setUseTextureAtlas(true,"tonegod/gui/style/atlasdef/atlas.png");
        app.getGuiNode().addControl(screen);
        
    }
    
    //Removes all the elements from the Screen
    public void clearScreen(SimpleApplication app) {
        screen.getElements().removeAll(screen.getElements());
        app.getStateManager().getState(GameManager.class).getEntityManager()
                .getPlayerManager().getPlayer().getHud().getInfoText().hide();
        app.getStateManager().getState(GameManager.class).getEntityManager()
                .getPlayerManager().getPlayer().getHud().getInfoText()
                    .getButtonOk().show();
    }
    
    //Returns the Screen
    public Screen getScreen() {
        return screen;
    }
    
}
