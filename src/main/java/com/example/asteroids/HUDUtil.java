package com.example.asteroids;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class HUDUtil {
    private static int points;
    private static Text pointsText;
    private static Text highScore;
    private static List<Particle> hearts;

    private static final int pointsTextX = 15;
    private static final int pointsTextY = 15;
    private static final int fontSize = 50;

    public static void init(int previousHighScore, List<Double> pointsHeart) {

        points = 0;

        pointsText = new Text(points + "");
        pointsText.setFont(Font.font("Times New Roman", fontSize));
        pointsText.setLayoutX(pointsTextX);
        pointsText.setLayoutY(pointsTextY);
        pointsText.setStroke(Color.RED);

        highScore = new Text(previousHighScore + "");
        highScore.setFont(Font.font("Times New Roman", fontSize));
        highScore.setTextAlignment(TextAlignment.CENTER);
        highScore.setLayoutX(Main.WIDTH/2);
        highScore.setLayoutY(pointsTextY);
        highScore.setStroke(Color.RED);

        hearts = new ArrayList<>();

        Particle heart1 = new Particle(pointsHeart, 0, 0, 0, 0);
        heart1.setLayoutX(pointsTextX);
        heart1.setLayoutY(pointsTextY + fontSize +10);
        heart1.setStroke(Color.RED);
        hearts.add(heart1);

        Particle heart2 = new Particle(pointsHeart, 0, 0, 0, 0);
        heart2.setLayoutX(heart1.getLayoutX() + heart1.getRadius() + 5);
        heart2.setLayoutY(pointsTextY + fontSize +10);
        heart2.setStroke(Color.RED);
        hearts.add(heart2);

        Particle heart3 = new Particle(pointsHeart, 0, 0, 0, 0);
        heart3.setLayoutX(heart2.getLayoutX() + heart2.getRadius() + 5);
        heart3.setLayoutY(pointsTextY + fontSize +10);
        heart3.setStroke(Color.RED);
        hearts.add(heart3);

        Main.root.getChildren().addAll(pointsText, highScore);

        for (Particle particle:
             hearts) {
            Main.root.getChildren().add(particle);
        }

    }


    public static void addPoints(int pointsAdd) {
        points += pointsAdd;
        pointsText.setText(points + "");
    }

    public static void removeHeart() {
        hearts.remove(hearts.size() - 1);
    }

//    public static void addHeart() {
//        hearts.add(heart);
//    }


}
