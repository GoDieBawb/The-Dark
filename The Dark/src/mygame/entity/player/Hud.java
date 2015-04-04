/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import java.util.ArrayList;
import mygame.GameManager;
import tonegod.gui.controls.windows.AlertBox;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bawb
 */
public class Hud {
    
    private AlertBox            infoText;
    private AppStateManager     stateManager;
    private Screen              screen;
    private String[]            currentMessage;
    private ArrayList<String[]> messages;
    
    public Hud(AppStateManager stateManager) {
        this.stateManager = stateManager;
        screen            = stateManager.getState(GameManager.class).getUtilityManager().getGuiManager().getScreen();
        messages          = new ArrayList();
        createInfoText();
    }
    
    private void createInfoText() {
        
        infoText = new AlertBox(screen, Vector2f.ZERO) {
            
            @Override
            public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {
                
                if (messages.isEmpty())
                    hideWithEffect();
                
                else
                    checkAlert();
                
            }
            
            @Override
            public void hide(){
                super.hide();
                System.out.println("Hiding");
            };
            
        };
       
        infoText.setMaterial(stateManager.getApplication().getAssetManager().loadMaterial("Materials/Paper.j3m"));
        infoText.getButtonOk().setFont("Interface/Impact.fnt");
        infoText.setWindowTitle("Welcome");
        infoText.setMsg("Welcome to Townyville.");
        infoText.setButtonOkText("Ok");
        infoText.setLockToParentBounds(true);
        infoText.setClippingLayer(infoText);
        infoText.setIsResizable(false);
        screen.addElement(infoText);
        infoText.hide();
        
    }
        
    public void addAlert(String title, String text) {
        
        System.out.println("Adding: " + title + " " + text);
        String[] message = {title, text};
        messages.add(message);
        checkAlert();
        
    }
    
    public void checkAlert() {
        
        if (messages.isEmpty())
            return;
        
        if (!infoText.getIsVisible()) {
            showAlert();
            infoText.getButtonOk().setText("Ok");
        }
        
        else {
            infoText.getButtonOk().setText("Next");
            infoText.setWindowTitle(currentMessage[0] + " (" + messages.size() + ")" );
        }
    
    }
    
    private void showAlert() {
        String[] message = messages.get(0);
        currentMessage   = message;
        String   title   = message[0];
        String   text    = message[1];
        infoText.setWindowTitle(title);
        infoText.setMsg(text);
        infoText.show();
        messages.remove(message);
        System.out.println("Showing: " + title + " " + text);
    }
    
    public AlertBox getInfoText() {
        return infoText;
    }
    
    public void update(float tpf) {
        
    }
    
}
