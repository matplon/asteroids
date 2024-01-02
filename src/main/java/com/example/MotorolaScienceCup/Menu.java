package com.example.MotorolaScienceCup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Menu extends Application {
    public static AnchorPane root;
    public static Stage stage = new Stage();
    public static Scene scene;
    @Override
    public void start(Stage stage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
