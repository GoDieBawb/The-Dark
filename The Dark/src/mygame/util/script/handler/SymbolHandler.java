package mygame.util.script.handler;

import com.jme3.app.state.AppStateManager;
import mygame.entity.Entity;
import mygame.util.script.TagParser;

/**
 *
 * @author root
 */
public class SymbolHandler extends CommandHandler {
    
    public SymbolHandler(AppStateManager stateManager, TagParser parser) {
        super(stateManager, parser);
    }
    
    @Override
    public boolean handle(String rawCommand, Entity entity) {
        
        boolean  handled = true;
        String[] args    = rawCommand.split(" ");
        String   command = args[0];
            
        switch (command) {

            case "boolean":
                
                boolean boolSym = false;
                
                if (args[2].contains("<") && args[2].contains(">")) {
                    boolSym = (Boolean) parser.parseTag(stateManager, args[2], entity);
                }
                else if (args[2].contains("true")) {
                    boolSym = true;
                }
                      
                entity.getScript().getSymTab().put(args[1], boolSym);
                
                break;

            case "int":
                
                int intSym;
                
                if (args[2].contains("<") && args[2].contains(">")) {
                    intSym = (Integer) parser.parseTag(stateManager, args[2], entity);
                }
                else {
                    intSym = Integer.valueOf(args[2].split("#")[1]);
                }
                
                entity.getScript().getSymTab().put(args[1], intSym);
                break;

            case "float":
                
                float floatSym;
                
                if (args[2].contains("<") && args[2].contains(">")) {
                    floatSym = (Float) parser.parseTag(stateManager, args[2], entity);
                }                
                else {
                    floatSym = Float.valueOf(args[2].split("#")[1]);
                }
                entity.getScript().getSymTab().put(args[1], floatSym);
                break;

            case "String":
                
                String stringSym;
                
                if (args[2].contains("<") && args[2].contains(">")) {
                    stringSym = (String) parser.parseTag(stateManager, args[2], entity);
                }
                else {
                    stringSym = args[2];
                }
                
                entity.getScript().getSymTab().put(args[1], stringSym);
                break;

            case "Object":
                Object obj = parser.parseTag(stateManager, args[2], entity);
                entity.getScript().getSymTab().put(args[1], obj);
                break;
                
            default:
                handled = false;
                break;

        }
        
        return handled;
        
    }
    
}
