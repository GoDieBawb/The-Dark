package mygame.util;

import java.io.File;

public class FileWalker {

    private String filePath;
    
    public String walk(String path, String fileName) {

        File root       = new File( path );
        File[] list     = root.listFiles();
        
        if (list == null) return filePath;

        for ( File f : list ) {
            
            String[] cutPath     = f.getAbsolutePath().split("/");
            String[] stripFile   = cutPath[cutPath.length-1].split("\\.");
            String   currentFile = stripFile[0];
            
            if ( f.isDirectory() ) {
                walk( f.getAbsolutePath(), fileName );
            }
            
            else {
                
                if (currentFile.toLowerCase().equalsIgnoreCase(fileName)) {
                    
                    if (f.getAbsolutePath().contains("Empty/"))
                        continue;
                    
                    filePath = f.getAbsolutePath();
                    break;
                }
                
            }
            
        }
        
        return filePath;
        
    }
    
}
