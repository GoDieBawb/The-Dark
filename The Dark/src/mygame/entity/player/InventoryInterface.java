/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.material.Material;
import mygame.GameManager;
import mygame.entity.item.Item;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author root
 */
public class InventoryInterface {
    
    private final AppStateManager stateManager;
    private final Screen          screen;
    private final Window          inventoryWindow;
    private final Player          player;
    private boolean               isVisible;
    
    //Constructs the Heads Up Display
    public InventoryInterface(AppStateManager stateManager, Player player) {
        this.stateManager = stateManager;
        screen            = stateManager.getState(GameManager.class).getUtilityManager().getGuiManager().getScreen();
        this.player       = player;
        inventoryWindow   = new Window(screen);
        inventoryWindow.setWidth(screen.getWidth()*.75f);
        inventoryWindow.setHeight(screen.getHeight()*.75f);
        inventoryWindow.setX(screen.getWidth()/2 - inventoryWindow.getWidth()/2); 
        inventoryWindow.setY(screen.getHeight()/2 - inventoryWindow.getHeight()/2);
        inventoryWindow.getDragBar().removeFromParent();
        inventoryWindow.setIsResizable(false);
        inventoryWindow.hideWindow();
        screen.addElement(inventoryWindow);
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public void show() {
        isVisible = true;
        player.getCameraManager().setEnabled(false);
        ((SimpleApplication) stateManager.getApplication()).getFlyByCamera().setDragToRotate(true);
        initElements();
        inventoryWindow.showWindow();
    }
    
    public void hide() {
        isVisible = false;
        player.getCameraManager().setEnabled(true);
        inventoryWindow.hideWindow();
        ((SimpleApplication) stateManager.getApplication()).getFlyByCamera().setDragToRotate(false);
    }
    
    private void initElements() {
    
        inventoryWindow.getContentArea().detachAllChildren();
        
        final Material eqMat = stateManager.getApplication().getAssetManager().loadMaterial("Materials/AlertBox.j3m");
        final Material unMat = stateManager.getApplication().getAssetManager().loadMaterial("Materials/UnMat.j3m");
        float          yMult = 2;
        float          xMult = 0;
        float          w     = inventoryWindow.getWidth()/5;
        float          h     = inventoryWindow.getHeight()/5;
        
        for (int i = 0; i < player.getInventory().size(); i++) {
        
            final String currentItem = (String) player.getInventory().keySet().toArray()[i];
            final Item item          = player.getInventory().get(currentItem);
            
            ButtonAdapter a = new ButtonAdapter(screen) {
                
                @Override
                public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean toggled) {
                    
                    if (player.hasLeft()) {
                        
                        if (player.getLeftEquip().equals(item)) {
                            item.unequip(player);
                            setMaterial(unMat);
                            initElements();
                            return;
                        }
                        
                    }
                    
                    else if (!item.isEquipped()) {
                        item.equip(player, true);
                        setMaterial(eqMat);
                        initElements();
                        return;
                    }
                    
                    if (player.hasRight()) {
                        
                        if (player.getRightEquip().equals(item)) {
                            item.unequip(player);
                            setMaterial(unMat);
                            initElements();
                        }
                        
                    }

                    else if (!item.isEquipped()) {
                        item.equip(player, false);
                        setMaterial(eqMat);
                        initElements();
                    }
                              
                }
                
            };

            boolean met = false; 
            
            if (player.hasLeft()) {
                
                if (player.getLeftEquip().equals(item)) {
                    a.setMaterial(eqMat);
                    met = true;
                }
                
            }
                
            if (player.hasRight()) {
                
                if (player.getRightEquip().equals(item)) {
                    a.setMaterial(eqMat);
                    met = true;
                }
                
            }
                
            if (!met) {
                a.setMaterial(unMat);
            }
            
            float x = w*xMult;
            float y = h*yMult;
            
            String labelText = currentItem;
            
            if (item.getAmount() > 1) {
                labelText = currentItem + " x" + item.getAmount();
            }    
            
            a.setText(labelText);
            a.setTextAlign(BitmapFont.Align.Center);
            
            a.setDimensions(w, h);
            a.setIsResizable(false);   
            
            inventoryWindow.getContentArea().addChild(a);
            
            a.setPosition(x, y);
            
            if (xMult%4 == 0 && x != 0) {
                xMult = 0;
                yMult--;
            }
            
            else {
                xMult++;
            }
            
        }
        
    }
    
}
