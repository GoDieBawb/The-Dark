package mygame.util.script.handler;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.item.Item;
import mygame.entity.player.Player;
import mygame.util.script.TagParser;

/**
 *
 * @author root
 */
public class PlayerHandler extends Handler {
    
    
    public PlayerHandler(AppStateManager stateManager, TagParser parser) {
        super(stateManager, parser);
    }
    
    @Override
    public boolean handle(String rawCommand, Entity entity) {
        
        Player player    = stateManager.getState(GameManager.class).getEntityManager().getPlayerManager().getPlayer();
        boolean  handled = true;
        String[] args    =  rawCommand.split(" ");
        String   command = args[0];
        
        //Equips an entity to the left side of the player
        switch (command) {
            
            case "equipleft":
                {
                    Item item = (Item) entity;
                    player.setLeftEquip(item);
                    break;
                }
                
            case "equipright":
                {
                    Item item = (Item) entity;
                    player.setRightEquip(item);
                    item.equip(player, false);
                    break;
                }
                
            case "drop":
                player.dropItem();
                break;
                
            case "give":
                try {
                    int amount = Integer.valueOf(args[2]);
                    player.getInventory().put(args[1], amount);
                }
                
                catch(Exception e) {
                    player.getInventory().put(args[1], 1);
                }   break;
                
            case "take":
                
                try {
                    int amount    = Integer.valueOf(args[2]);
                    int newAmount = ((Integer) player.getInventory().get(args[1])) - amount;
                    player.getInventory().put(args[1], newAmount);
                }
                
                catch(Exception e) {
                    player.getInventory().remove(args[1]);
                }   break;
                
            case "chat":
                {
                    String[] a     = rawCommand.split(" ", 2);
                    player.getHud().addAlert(((Entity) entity).getName(), a[1]);
                    break;
                }
                
            case "delaychat":
                {
                    String[] a     = rawCommand.split(" ", 2);
                    player.getHud().addAlert(((Entity) entity).getName(), a[1]);
                    break;
                }
                
            case "finish":
                ((SimpleApplication) stateManager.getApplication()).getRootNode().detachAllChildren();
                player.getHud().addAlert("Finish", "Thanks for playing");
                break;
                
            case "fail":
                player.die();
                break;
                
            default:
                handled = false;
                break;
                
        }
        
        return handled;
        
    }    
    
}
