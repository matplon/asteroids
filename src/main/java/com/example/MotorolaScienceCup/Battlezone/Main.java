package com.example.MotorolaScienceCup.Battlezone;


import com.example.MotorolaScienceCup.Menu;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main {

    static int WIDTH = Menu.WIDTH;
    static int HEIGHT = Menu.HEIGHT;

    static double H_FOV = 90;

    public static AnchorPane root = new AnchorPane();
    public static Scene scene = new Scene(root,WIDTH,HEIGHT);

    public static void init(){
        Menu.stage.setScene(scene);
    }
}
