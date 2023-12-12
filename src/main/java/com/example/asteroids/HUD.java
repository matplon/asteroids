package com.example.asteroids;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class HUD {
    private static int points;
    private static Text pointsText;
    private static Text highScore;
    private static List<BetterPolygon> hearts;

    private static final int pointsTextX = 15;
    private static final int pointsTextY = 15;
    private static final int fontSize = 50;
    private static List<Double> pointsHeart;

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
        pointsText.setFont(Font.font("Times New Roman", fontSize));
        pointsText.setLayoutX(pointsTextX);
        pointsText.setLayoutY(pointsTextY);
        pointsText.setStroke(Color.RED);

        highScore = new Text(previousHighScore + "");
        highScore.setFont(Font.font("Times New Roman", fontSize));
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
        Main.root.getChildren().remove(hearts.get(hearts.size() - 1));
        hearts.remove(hearts.size() - 1);
    }

    public static int getPoints(){
        return points;
    }


}
