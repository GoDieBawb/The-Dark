/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import mygame.GameManager;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bawb
 */
public class GuiManager {
    
    private Screen            screen;
    private SimpleApplication app;
    private AppStateManager   stateManager;
    
    public GuiManager(SimpleApplication app) {
        this.app     = app;
        stateManager = app.getStateManager();
        createScreen();
    }
    
    private void createScreen() {
        
        screen = new Screen(app, "tonegod/gui/style/atlasdef/style_map.gui.xml");
        screen.setUseTextureAtlas(true,"tonegod/gui/style/atlasdef/atlas.png");
        screen.setUseMultiTouch(true);
        app.getInputManager().setSimulateMouse(true);
        app.getGuiNode().addControl(screen);
        
    }
    
    public void clearScreen(SimpleApplication app) {
        screen.getElements().removeAll(screen.getElements());
        app.getStateManager().getState(GameManager.class).getEntityManager()
                .getPlayerManager().getPlayer().getHud().getInfoText().hide();
        app.getStateManager().getState(GameManager.class).getEntityManager()
                .getPlayerManager().getPlayer().getHud().getInfoText()
                    .getButtonOk().show();
    }
    
    public Screen getScreen() {
        return screen;
    }
    
}
