package com.basketbandit.rizumu;

import com.basketbandit.rizumu.engine.Engine;
import javafx.application.Application;
import javafx.stage.Stage;

public class Rizumu extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.setProperty("sun.java2d.opengl", "true"); // OpenGL
        System.setProperty("sun.java2d.d3d", "false"); // DirectX
        new Engine().start();
    }
}
