/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.script.handler;

import com.jme3.app.state.AppStateManager;
import mygame.entity.Entity;
import mygame.entity.Scriptable;
import mygame.util.script.TagParser;

/**
 *
 * @author root
 */
public class LogicHandler extends Handler {
    
    private boolean go;
    private boolean met;
    
    public LogicHandler(AppStateManager stateManager, TagParser parser) {
        super(stateManager, parser);
    }
    
    @Override
    public boolean handle(String rawCommand, Entity entity) {
        
        boolean handled = true;
        
        String[] args     = rawCommand.split(" ");
        String   command  = args[0];        
        
        //Starts an if logic statement
        switch (command) {
            
            case "if":
                //If the condition returns true then go
                if (ifCheck(entity, args)) {
                    go  = true;
                    met = true;
                }
                
                //If not the condition is not met and the commands should not go
                else {
                    go  = false;
                    met = false;
                }   
                
                break;
                
            case "elseif":
                //If previous condition was not met else if comes into action
                if (!met) {
                    
                    //Check the condiition if true the script can go
                    if (ifCheck(entity, args)) {
                        
                        go  = true;
                        met = true;
                        
                    }
                    
                }
                
                //If the previous condition was met else if does not come into play
                else {
                    
                    go  = false;
                    
                }
                break;
                
            case "else":
                go = !go;
                break;
                
            case "end":
                go = true
                        ;
                break;
                
            default:
                handled = false;
                break;
                
        }
        
        return handled;
        
    }

    public void setMet(boolean newVal) {
        met = newVal;
    }
    
    public void setGo(boolean newVal) {
        go = newVal;
    }
    
    public boolean isGo() {
        return go;
    }
  
    //Determines the truth value of a tag
    private boolean ifCheck(Scriptable entity, String[] args) {
      
        Object comp1 = null;
        Object comp2 = null;
        String comp  = null;
      
        try {
            comp1      = parser.parseTag(stateManager, args[1], entity);
            comp2      = parser.parseTag(stateManager, args[3], entity);
            comp       = args[2];
        }
        
        catch (Exception e) {
        }
      
        boolean truthVal  = false;
      
        if (comp == null) {
            
            if (comp1 instanceof Boolean) {
              truthVal = (Boolean) comp1;
            }
            
            else {
              truthVal = false;
            }
            
            return truthVal;
            
        }
      
        if (!comp1.getClass().getSimpleName().equals(comp2.getClass().getSimpleName())) {
            System.out.println("Warning: Attempted to Compare Unlike Classes");
            truthVal = false;      
        }      
      
        switch (comp) {
            
            case "&&":
                if (comp1 instanceof Boolean && comp2 instanceof Boolean) {
                    
                    Boolean a = (Boolean) comp1;
                    Boolean b = (Boolean) comp2;
                    
                    if (a&&b) {
                        truthVal = true;
                    }
                    
                    else{
                        truthVal = false;
                    }
                    
                }
                
                else {
                    
                    truthVal = false;
                    
                }
                
                return truthVal;
                
            case "!!":
                if (comp1 instanceof Boolean && comp2 instanceof Boolean) {
                    
                    Boolean a = (Boolean) comp1;
                    Boolean b = (Boolean) comp2;
                    
                    if (!a && !b) {
                        truthVal = true;
                    }
                    else{
                        truthVal = false;
                    }
                    
                }
                
                else {
                    
                    truthVal = false;
                    
                }
                
                return truthVal;
                
            case "!&":
                if (comp1 instanceof Boolean && comp2 instanceof Boolean) {
                    
                    Boolean a = (Boolean) comp1;
                    Boolean b = (Boolean) comp2;
                    
                    if (a && !b) {
                        truthVal = false;
                    }
                    else{
                        truthVal = true;
                    }
                    
                    return truthVal;
                    
                }
                
                else {  
                    truthVal = true;
                }
                
                return truthVal;
                
            case "==":
                if (comp1.equals(comp2)) {
                    truthVal = true;
                }
                else {
                    truthVal = false;
                }     
                break;
                
            default:
                System.out.println("Unknown Operator: " + comp);
                break;
                
        }
      
    return truthVal;
    
    }    
    
}
