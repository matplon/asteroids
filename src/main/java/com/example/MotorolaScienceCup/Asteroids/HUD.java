package com.example.MotorolaScienceCup.Asteroids;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Menu;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Sound;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.example.MotorolaScienceCup.Asteroids.Main.HEIGHT;
import static com.example.MotorolaScienceCup.Asteroids.Main.WIDTH;

public class HUD {

    public final static int baseRadius = 40;
    private static int points;
    private static Text pointsText;
    private static Text highScore;
    private static List<BetterPolygon> hearts;

    public static final int pointsTextX = 15;
    public static final int pointsTextY = HEIGHT/7;
    private static final int fontSize = 50;
    private static final String fontStyle = "Public Pixel";
    private static List<Double> pointsHeart;

    private static Font font = new Font(fontStyle, fontSize);

    public static void addHeart() {
        BetterPolygon heart = new Particle(pointsHeart, 0, 0, 0, 0);
        heart.setStroke(Color.RED);
        if(hearts.size() > 0)
            heart.setLayoutX(hearts.get(hearts.size()-1).getLayoutX() + hearts.get(hearts.size() - 1).getRadius() + 50);
        else
            heart.setLayoutX(WIDTH/100);
        heart.setLayoutY(pointsTextY - fontSize - 40 );
        heart.scale(2);
        hearts.add(heart);
        Main.root.getChildren().add(heart);
    }

    public static void init(int previousHighScore, List<Double> pointsHeartCoordinates) {

        points = 0;
        pointsHeart = pointsHeartCoordinates;

        pointsText = new Text(points+"");
        pointsText.setFont(font);
        pointsText.setLayoutX(WIDTH/2 - pointsText.getLayoutBounds().getWidth()/2);
        pointsText.setLayoutY(pointsTextY/2);
        pointsText.setFill(Color.RED);

        highScore = new Text("HiScore  "+previousHighScore);
        highScore.setFont(font);
        highScore.setTextAlignment(TextAlignment.CENTER);
        highScore.setLayoutX(pointsTextX+highScore.getLayoutBounds().getWidth()/10);
        highScore.setLayoutY(pointsTextY);
        highScore.setFill(Color.RED);

        Text back = new Text("←");
        back.setFill(Color.RED);
        back.setFont(Font.font("Public Pixel", 40));
        back.setLayoutX(WIDTH/30);
        back.setLayoutY(HEIGHT/20);
        back.setOnMouseClicked(mouseEvent -> {
            try {
                Main.timeline.stop();
                if(Enemy.clip!=null){
                    Enemy.clip.close();
                    Enemy.clip = null;
                }
                if(Main.playerEngine!=null){
                    Main.playerEngine.close();
                    Main.playerEngine = null;
                }
                Main.resetData();
                Menu.resetMenu();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
        Main.root.getChildren().add(back);

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
        Main.resetData();
        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(Menu.font);
        gameOverText.setFill(Color.GREEN);

        gameOverText.setX(WIDTH/2 - gameOverText.getLayoutBounds().getWidth()/2);
        gameOverText.setY(HEIGHT/2);



        AnchorPane newRoot = new AnchorPane();
        newRoot.getChildren().add(gameOverText);
        Button restart = new Button("Restart");
        restart.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))));
        restart.setFont(Menu.font);
        restart.setLayoutX(WIDTH/2 - 275 - restart.getWidth()/2);
        restart.setLayoutY(HEIGHT/2 + 100);


        Button menu = new Button("Menu");
        menu.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))));
        menu.setFont(Menu.font);
        menu.setLayoutX(WIDTH/2 + 75 + menu.getWidth()/2);
        menu.setLayoutY(HEIGHT/2 + 100);
        menu.setOnAction(actionEvent -> {
            try {
                Menu.resetMenu();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
        restart.setOnAction(actionEvent -> {
            try {
                Main.init();
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        newRoot.getChildren().addAll(restart, menu);
        Scene newScene = new Scene(newRoot, WIDTH, HEIGHT);
        newScene.setFill(Color.BLACK);
        newRoot.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))));
        Menu.stage.setScene(newScene);
        InputStream in = Sound.class.getResourceAsStream("highscore.txt");
        InputStream of = new BufferedInputStream(in);

        Scanner scanner = null;
        scanner = new Scanner(of);
       
        if (scanner.hasNextLine()) {

            int highscore = Integer.parseInt(scanner.nextLine());
           
            /*if (HUD.getPoints() > highscore) {
                Writer writer = null;
                try {
                    writer = new FileWriter(new File("highscore.txt"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    writer.write(HUD.getPoints() + "");
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else {
           
            Writer writer = null;
            try {
                writer = new FileWriter(new File("highscore.txt"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                writer.write(HUD.getPoints() + "");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }*/

    }}


    public static void drawMap(BetterPolygon base){
        base.scale(baseRadius /base.getRadius());

    }
}
