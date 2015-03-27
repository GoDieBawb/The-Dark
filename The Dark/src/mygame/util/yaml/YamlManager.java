/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.util.yaml;

import com.jme3.app.state.AppStateManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Bob
 */
public class YamlManager {
    
    public YamlManager(AppStateManager stateManager) {
        stateManager.getApplication().getAssetManager().registerLoader(YamlLoader.class, "yml");
    }
    
    public HashMap loadYamlAsset(String path, AppStateManager stateManager) {
    
        HashMap map = (HashMap) stateManager.getApplication().getAssetManager().loadAsset(path);
        return  map;
        
    }
    
    public HashMap loadYaml(String path) {
        
        HashMap map;
        File    file;
        Yaml    yaml = new Yaml();
        
        try {
            file = new File(path);
            FileInputStream fi = new FileInputStream(file);
            Object obj = yaml.load(fi);
            map = (LinkedHashMap) obj;
        }
        
        catch(FileNotFoundException fnf) {
            return null;
        }
        
        return map;
       
    }
    
    public void saveYaml(String path, HashMap map) {
        
        DumperOptions options = new DumperOptions();
        File file             = new File(path);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setAllowUnicode(true);
        Yaml yaml = new Yaml(options);         
        
        try {
        
            FileWriter fw = new FileWriter(file);
            yaml.dump(map, fw);
            fw.close();
            
        }
        
        catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
}
