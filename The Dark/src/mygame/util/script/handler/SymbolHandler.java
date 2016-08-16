package mygame.util.script.handler;

import com.jme3.app.state.AppStateManager;
import mygame.entity.Entity;
import mygame.util.script.TagParser;

/**
 *
 * @author root
 */
public class SymbolHandler extends Handler {
    
    public SymbolHandler(AppStateManager stateManager, TagParser parser) {
        super(stateManager, parser);
    }
    
    @Override
    public boolean handle(String rawCommand, Entity entity) {
        
        boolean  handled = true;
        String[] args    =  rawCommand.split(" ");
        String   command = args[0];
            
        switch (command) {

            case "boolean":
                boolean boolSym = false;
                if (args[2].contains("true"))
                    boolSym = true;
                entity.getScript().getSymTab().put(args[1], boolSym);
                break;

            case "int":

                int intSym = Integer.valueOf(args[2]);
                entity.getScript().getSymTab().put(args[1], intSym);
                break;

            case "float":
                float floatSym = Float.valueOf(args[2]);
                entity.getScript().getSymTab().put(args[1], floatSym);
                break;

            case "String":
                String stringSym = args[2];
                entity.getScript().getSymTab().put(args[1], stringSym);
                break;

            default:
                handled = false;
                break;

        }
        
        return handled;
        
    }
    
}
