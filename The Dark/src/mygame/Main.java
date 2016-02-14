package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        //AppSettings settings = new AppSettings(true);
        //settings.setFullscreen(true);
        //app.setSettings(settings);
        app.start();
    }

    // Game Starts with Attaching the Game Manager
    @Override
    public void simpleInitApp() {
        setDisplayStatView(false);
        setShowSettings(false);
        setDisplayFps(false);
        getFlyByCamera().setEnabled(false);
        getStateManager().attach(new GameManager());
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

}
