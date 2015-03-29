/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import mygame.GameManager;
import tonegod.gui.controls.windows.AlertBox;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bawb
 */
public class Hud {
    
    private AlertBox        infoText;
    private AppStateManager stateManager;
    private Screen          screen;
    private boolean         hasAlert;
    private String          delayedMessage;
    private String          delayedTitle;
    private int             alertDelay;
    private long            delayStart;
    
    public Hud(AppStateManager stateManager) {
        this.stateManager = stateManager;
        this.screen       = stateManager.getState(GameManager.class).getUtilityManager().getGuiManager().getScreen();
        createInfoText();
    }
    
    private void createInfoText() {
        
        infoText = new AlertBox(screen, Vector2f.ZERO) {
            
            @Override
            public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {
                
                hideWithEffect();
                
            }
            
        };
        
        infoText.setMaterial(stateManager.getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        infoText.getButtonOk().setFont("Interface/Impact.fnt");
        infoText.setWindowTitle("Welcome");
        infoText.setMsg("Welcome to Townyville.");
        infoText.setButtonOkText("Ok");
        infoText.setLockToParentBounds(true);
        infoText.setClippingLayer(infoText);
        //infoText.setDimensions(new Vector2f(getScreen().getWidth()/10, getScreen().getHeight()/10));
        infoText.setIsResizable(false);
        screen.addElement(infoText);
        infoText.hide();
        
    }
        
    public void showAlert(String title, String text){
        infoText.showWithEffect();
        infoText.setWindowTitle(title);
        infoText.setMsg(text);
    }
    
    public void delayAlert(String speaker, String text, int delay){
        hasAlert = true;
        delayStart = System.currentTimeMillis() / 1000;
        alertDelay = delay;
        delayedTitle = speaker;
        delayedMessage = text;
    }    
    
    public AlertBox getInfoText() {
        return infoText;
    }
    
    public void update(float tpf) {
        
        if (hasAlert) {
            
            if (System.currentTimeMillis()/1000 - delayStart > alertDelay) {
                showAlert(delayedTitle, delayedMessage);
                hasAlert = false;
            }
            
        }
    }
    
}
