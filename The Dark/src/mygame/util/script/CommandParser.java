package mygame.util.script;

import com.jme3.app.state.AppStateManager;
import java.util.ArrayList;
import mygame.GameManager;
import mygame.entity.Entity;
import mygame.entity.player.Player;
import mygame.util.script.handler.*;


/**
 *
 * @author Bob
 */
public class CommandParser {

    private final AppStateManager  stateManager;
    private final GameManager      gm;
    private final TagParser        parser;
    private final SymbolHandler    symbolHandler;
    private final AnimationHandler animationHandler;
    private final PlayerHandler    playerHandler;
    private final EntityHandler    entityHandler;
    private final UtilityHandler   utilityHandler;
    private final LogicHandler     logicHandler;
    
    public CommandParser(AppStateManager stateManager) {
        this.stateManager = stateManager;
        parser            = new TagParser();
        gm                = stateManager.getState(GameManager.class);
        symbolHandler     = new SymbolHandler(stateManager, parser);
        animationHandler  = new AnimationHandler(stateManager, parser);
        playerHandler     = new PlayerHandler(stateManager, parser);
        entityHandler     = new EntityHandler(stateManager, parser);
        utilityHandler    = new UtilityHandler(stateManager, parser);
        logicHandler      = new LogicHandler(stateManager, parser);
    }
    
    //Takes the current line from the script and executes the proper command
    public void parse(ArrayList commands, Entity entity) {
        
        Player  player  = gm.getEntityManager().getPlayerManager().getPlayer();
        
        //Start with assuming there is no condition to run the script
        logicHandler.setGo(true);
        logicHandler.setMet(false);
        
        for (int i = 0; i < commands.size(); i++) {
            
            if (player.isDead())
                break;
                
            String[] args     = ((String) commands.get(i)).split(" ");
            String   command  = args[0];
            
            //Logic Handler Determines Go Check
            if (logicHandler.handle((String) commands.get(i), entity))
                continue;
            
            //If not able to go continue onto the next command
            if(!logicHandler.isGo()) {
                continue;
            }
            
            //If Commands Go Unhandled They are Unknown
            if (symbolHandler.handle((String) commands.get(i), entity))
                continue;

            if (utilityHandler.handle((String) commands.get(i), entity))
                continue;
            
            if (entityHandler.handle((String) commands.get(i), entity))
                continue;
            
            if (playerHandler.handle((String) commands.get(i), entity))
                continue;
            
            if (animationHandler.handle((String) commands.get(i), entity))
                continue;
            
            System.out.println("Unknown comand: " + command);
            
        }
        
    }
    
}