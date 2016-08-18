/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.app.state.AppStateManager;
import mygame.entity.player.Player;

/**
 *
 * @author root
 */
public class Usable extends Item {

    //Constructs the Candle Entity
    public Usable(AppStateManager stateManager) {
        super(stateManager);
    }
    
    @Override
    public void equip(Player player, boolean isLeft) {
        super.equip(player, isLeft);
        attachChild(this);
    }
    
    @Override
    public void unequip(Player player) {
        super.unequip(player);
    }
    
    @Override
    public void use() {
    
    }
    
}
