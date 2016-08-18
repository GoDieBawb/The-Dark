/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import java.util.ArrayList;
import mygame.GameManager;
import org.lwjgl.opengl.Display;
import tonegod.gui.controls.windows.AlertBox;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bawb
 */
public class Hud {
    
    private AlertBox                  infoText;
    private final AppStateManager     stateManager;
    private final Screen              screen;
    private String[]                  currentMessage;
    private final ArrayList<String[]> messages;
    private BitmapText                crossHair;
    
    //Constructs the Heads Up Display
    public Hud(AppStateManager stateManager) {
        this.stateManager = stateManager;
        screen            = stateManager.getState(GameManager.class).getUtilityManager().getGuiManager().getScreen();
        messages          = new ArrayList();
        createInfoText();
        createCrossHair();
    }
    
    //Creates the Crosshair for the gun
    private void createCrossHair() {
        BitmapFont guiFont = stateManager.getApplication().getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        crossHair          = new BitmapText(guiFont, false);
        crossHair.setName("CrossHair");
        crossHair.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        crossHair.setLocalTranslation(Display.getWidth() / 2 - crossHair.getLineWidth()/2, Display.getHeight() / 2 + crossHair.getLineHeight()/2, 0);
        crossHair.setText("+");
    }
    
    //Attach the Crosshair
    public void attachCrossHair() {
        ((SimpleApplication) stateManager.getApplication()).getGuiNode().attachChild(crossHair);
    }
    
    //Detach the CrossHair
    public void detachCrossHair() {
        ((SimpleApplication) stateManager.getApplication()).getGuiNode().detachChild(crossHair);
    }
    
    //Creates the Alert Box that displays the text in the game
    private void createInfoText() {
        
        infoText = new AlertBox(screen, Vector2f.ZERO) {
            
            @Override
            public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {
                
                if (messages.isEmpty())
                    hideWithEffect();
                
                else
                    checkAlert();
                
            }
            
        };
       
        infoText.setMaterial(stateManager.getApplication().getAssetManager().loadMaterial("Materials/AlertBox.j3m"));
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
    
    //Add a Message to the messages list
    public void addAlert(String title, String text) {
        
        String[] message = {title, text};
        messages.add(message);
        checkAlert();
        
    }
    
    //Check to see if there is an alert in the list if so, show it
    public void checkAlert() {
        
        if (messages.isEmpty())
            return;
        
        if (!infoText.getIsVisible()) {
            showAlert();
        }
            
        if (messages.size() > 0) {
            infoText.getButtonOk().setText("Next");
            infoText.setWindowTitle(currentMessage[0] + " (" + messages.size() + ")" );
        }

        else {
            infoText.getButtonOk().setText("Ok");
        }
    
    }
    
    //Show the message and then remove it from the messages list
    private void showAlert() {
        String[] message = messages.get(0);
        currentMessage   = message;
        String   title   = message[0];
        String   text    = message[1];
        infoText.setWindowTitle(title);
        infoText.setMsg(text);
        infoText.show();
        messages.remove(message);
    }
    
    //Return the Alert Box
    public AlertBox getInfoText() {
        return infoText;
    }
    
    public void update(float tpf) {
        
    }
    
}
