package com.example.MotorolaScienceCup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Menu extends Application {
    public static AnchorPane root;
    public static Stage stage;
    public static Scene scene;
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("C:\\Users\\Computer Science\\src\\com\\company\\asteroids1\\src\\main\\java\\com\\example\\MotorolaScienceCup\\Main.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Shop.fxml"));

        root = FXMLLoader.load(getClass().getResource("Main.fxml"));

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
