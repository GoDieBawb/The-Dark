/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.entity.item;

import com.jme3.app.state.AppStateManager;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.player.Player;

/**
 *
 * @author root
 */
public abstract class Item extends Entity {
    
    protected AppStateManager stateManager;
    private   boolean         isActing;
    private   int             amount;
    protected Player          player;
    
    public Item(AppStateManager stateManager) {
        this.stateManager = stateManager;
        player            = stateManager.getState(GameManager.class).getEntityManager().getPlayerManager().getPlayer();
    }
    
    public void equip(Player player, boolean isLeft) {
        
        if (isLeft)
            player.setLeftEquip(this);
        
        else
            player.setRightEquip(this);
        
    }
    
    public void setAmount(int newVal) {
        amount = newVal;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setIsActing(boolean newVal) {
        isActing = newVal;
    }
    
    public boolean isActing() {
        return isActing;
    }
    
    public void unequip(Player player) {
    
    }
    
    public abstract void use();
    
}
