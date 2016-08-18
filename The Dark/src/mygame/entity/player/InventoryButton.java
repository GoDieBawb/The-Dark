/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.player;

import com.jme3.input.event.MouseButtonEvent;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.ElementManager;
import tonegod.gui.listeners.MouseButtonListener;

/**
 *
 * @author root
 */
public class InventoryButton extends Window implements MouseButtonListener {
    
    public InventoryButton(ElementManager screen) {
        super(screen);
    }

    @Override
    public void onMouseLeftPressed(MouseButtonEvent evt) {
        
    }

    @Override
    public void onMouseLeftReleased(MouseButtonEvent evt) {
        

			onButtonMouseLeftUp(evt, true);
		
		evt.setConsumed();
	
    }

    @Override
    public void onMouseRightPressed(MouseButtonEvent evt) {
        
    }

    @Override
    public void onMouseRightReleased(MouseButtonEvent evt) {
        
    }
    
    
    public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {}
    
}
