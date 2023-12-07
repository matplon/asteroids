package com.example.asteroids;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class HUDUtil {
    private static int points;
    private static Text pointsText;
    private static Text highScore;
    private static List<Polygon> hearts;
    private static Polygon heart;

    public static void init(int previousHighScore) {
        points = 0;

        pointsText = new Text(points + "");
        pointsText.setFont(Font.font("Times New Roman", 15));
        pointsText.setTextAlignment(TextAlignment.CENTER);

        highScore = new Text();
//lol
    }


    public static void addPoints(int pointsAdd) {
        points += pointsAdd;
        pointsText.setText(points + "");
    }

    public static void removeHeart() {
        hearts.remove(hearts.size() - 1);
    }

    public static void addHeart() {
        hearts.add(heart);
    }


}
