package com.example.MotorolaScienceCup.Asteroids;

import com.example.MotorolaScienceCup.BetterPolygon;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class HUD {
    public final static int baseRadius = 40;
    private static int points;
    private static Text pointsText;
    private static Text highScore;
    private static List<BetterPolygon> hearts;

    private static final int pointsTextX = 15;
    private static final int pointsTextY = 100;
    private static final int fontSize = 50;
    private static final String fontStyle = "Times New Roman";
    private static List<Double> pointsHeart;

    private static Font font = new Font(fontStyle, fontSize);

    public static void addHeart() {
        BetterPolygon heart = new Particle(pointsHeart, 0, 0, 0, 0);
        heart.setStroke(Color.RED);
        if(hearts.size() > 0)
            heart.setLayoutX(hearts.get(hearts.size()-1).getLayoutX() + hearts.get(hearts.size() - 1).getRadius() + 5);
        else
            heart.setLayoutX(pointsTextX);
        heart.setLayoutY(pointsTextY + fontSize + 10);
        hearts.add(heart);
        Main.root.getChildren().add(heart);
    }

    public static void init(int previousHighScore, List<Double> pointsHeartCoordinates) {

        points = 0;
        pointsHeart = pointsHeartCoordinates;

        pointsText = new Text(points + "");
        pointsText.setFont(font);
        pointsText.setLayoutX(pointsTextX);
        pointsText.setLayoutY(pointsTextY);
        pointsText.setStroke(Color.RED);

        highScore = new Text(previousHighScore + "");
        highScore.setFont(font);
        highScore.setTextAlignment(TextAlignment.CENTER);
        highScore.setLayoutX(Main.WIDTH / 2);
        highScore.setLayoutY(pointsTextY);
        highScore.setStroke(Color.RED);

        hearts = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            addHeart();
        }

        Main.root.getChildren().addAll(pointsText, highScore);
    }

    public static void addPoints(int pointsAdd) {
        points += pointsAdd;
        pointsText.setText(points + "");
    }

    public static void removeHeart() {
        if(!hearts.isEmpty()) {
            Main.root.getChildren().remove(hearts.get(hearts.size() - 1));
            hearts.remove(hearts.size() - 1);
        }

    }

    public static int getPoints(){
        return points;
    }
    public static void gameOver() {
        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(font);
        gameOverText.setStroke(Color.RED);
        gameOverText.setX(Main.WIDTH/2 - gameOverText.getLayoutBounds().getWidth()/2);
        gameOverText.setY(Main.HEIGHT/2);
        Main.root.getChildren().add(gameOverText);

        AnchorPane newRoot = new AnchorPane();
        Button restart = new Button("Restart");
        restart.setLayoutX(400);
        restart.setLayoutY(700);
        restart.setFont(font);

        restart.setOnAction(actionEvent -> {

        });

        Button menu = new Button("Menu");
        menu.setLayoutX(1300);
        menu.setLayoutY(700);
        menu.setFont(font);

        newRoot.getChildren().addAll(restart, menu);
        Main.scene = new Scene(newRoot, Main.WIDTH, Main.HEIGHT);
        Main.stage.setScene(Main.scene);
    }


    public static void drawMap(BetterPolygon base){
        base.scale(baseRadius /base.getRadius());

    }
}
