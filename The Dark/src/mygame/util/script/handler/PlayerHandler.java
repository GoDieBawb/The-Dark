package mygame.util.script.handler;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.item.Item;
import mygame.entity.item.Lamp;
import mygame.entity.player.Player;
import mygame.util.script.TagParser;

/**
 *
 * @author root
 */
public class PlayerHandler extends CommandHandler {
    
    
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
            
            case "equip":
                {
                    Item item = (Item) entity;
                    if (!player.hasLeft()) {
                        item.equip(player, true);
                    }
                    
                    else if (!player.hasRight()) {
                        item.equip(player, false);
                    }
                    
                    else {
                        
                        entity.setLocalTranslation(0,-15,0);
                        entity.removeFromParent();
                        player.getHud().addAlert(item.getName(), "Could not equip. Item " + item.getName() + " was placed in your inventory");
                        if(entity instanceof Lamp) {
                            ((Lamp) entity).extinguish();
                        }
                        
                    }
                    
                    break;
                }
            
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
                
            case "flag":
                try {
                    int amount = Integer.valueOf(args[2]);
                    player.getFlagList().put(args[1], amount);
                }
                
                catch(Exception e) {
                    player.getFlagList().put(args[1], 1);
                }   break;
            
            case "give":
                try {
                    Item item = (Item) entity;
                    int amount = Integer.valueOf(args[2]);
                    item.setAmount(amount);
                    player.getInventory().put(args[1], item);
                }
                
                catch(Exception e) {
                    Item item = (Item) entity;
                    item.setAmount(1);
                    player.getInventory().put(args[1], item);
                }   break;
                
            case "take":
                
                try {
                    int amount    = Integer.valueOf(args[2]);
                    int newAmount = ((Integer) player.getFlagList().get(args[1])) - amount;
                    player.getFlagList().put(args[1], newAmount);
                }
                
                catch(Exception e) {
                    player.getFlagList().remove(args[1]);
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
