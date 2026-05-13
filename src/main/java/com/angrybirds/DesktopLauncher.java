package com.angrybirds;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Piggy Revenge - libGDX");
        config.setWindowedMode(AngryBirdsGame.V_WIDTH, AngryBirdsGame.V_HEIGHT);
        config.useVsync(true);
        config.setForegroundFPS(60);
        config.setResizable(true);
        new Lwjgl3Application(new AngryBirdsGame(), config);
    }
}
