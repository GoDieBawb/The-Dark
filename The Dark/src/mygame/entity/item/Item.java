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
    private   boolean         isEquipped;
    private   int             amount;
    private   boolean         isLeft;
    protected Player          player;
    
    public Item(AppStateManager stateManager) {
        this.stateManager = stateManager;
        player            = stateManager.getState(GameManager.class).getEntityManager().getPlayerManager().getPlayer();
    }
    
    public void equip(Player player, boolean isLeft) {
        
        isEquipped  = true;
        this.isLeft = isLeft;
        
        if (isLeft)
            player.setLeftEquip(this);
        
        else
            player.setRightEquip(this);
        
    }
    
    public boolean isEquipped() {
        return isEquipped;
    }
    
    public boolean isLeft() {
        return isLeft;
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
        isEquipped = false;
    }
    
    public abstract void use();
    
}
