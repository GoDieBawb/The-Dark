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
    
    //On Construct Register the Yaml Loader
    public YamlManager(AppStateManager stateManager) {
        stateManager.getApplication().getAssetManager().registerLoader(YamlLoader.class, "yml");
    }
    
    //This Method is Used to Load a Script From the Compiled Assets jar
    public HashMap loadYamlAsset(String path, AppStateManager stateManager) {
    
        HashMap map = (HashMap) stateManager.getApplication().getAssetManager().loadAsset(path);
        return  map;
        
    }
    
    //This Method is used to load an external script
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
    
    //Will Save a Hashmap to a YAML file at the given path
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
