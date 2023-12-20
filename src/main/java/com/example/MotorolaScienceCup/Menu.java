package com.example.MotorolaScienceCup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Menu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("C:\\Users\\Computer Science\\src\\com\\company\\asteroids1\\src\\main\\java\\com\\example\\MotorolaScienceCup\\Main.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        AnchorPane root = loader.load();



        Scene scene = new Scene(root, 800 ,800);
        stage.setScene(scene);
        stage.show();
    }
}
