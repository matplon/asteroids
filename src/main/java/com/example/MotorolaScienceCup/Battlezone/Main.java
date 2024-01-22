package com.example.MotorolaScienceCup.Battlezone;


import com.example.MotorolaScienceCup.Menu;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main {

    static int WIDTH = Menu.WIDTH;
    static int HEIGHT = Menu.HEIGHT;

    static String cubePath = "Cube.txt";
    static double H_FOV = 90;

    public static AnchorPane root = new AnchorPane();
    public static Scene scene = new Scene(root,WIDTH,HEIGHT);

    public static void init(){

        Camera camera = new Camera(5,-2,-10);
        Object3D obj = Util.convertOBJ(cubePath);
        System.out.println("BRUH");
        Object3D obj1 = Util.generateOBJ(0,0,0,obj.getPoints3D(),obj.getFaces3D());
        for (int i = 0; i < obj1.getPoints3D().size(); i++) {
            System.out.println(obj1.getPoints3D().get(i).toString() + "0");
        }
        obj1.rotY(45);
        obj1.displayObject();




        Menu.stage.setScene(scene);
    }


}
