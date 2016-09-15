/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navtool;

import navtools.NavMeshEditor;

/**
 *
 * @author root
 */
public class Main extends NavMeshEditor {
    
    public static void main(String[] args) {
        NavMeshEditor app = new NavMeshEditor();
        app.start();
    }

    // Game Starts with Attaching the Game Manager
    @Override
    public void simpleInitApp() {
        super.simpleInitApp();
    }    
    
}
