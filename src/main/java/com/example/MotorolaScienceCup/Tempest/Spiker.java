package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Util;
import com.example.MotorolaScienceCup.Vector;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.List;

public class Spiker extends Enemy {
    private static final String filepath = "asteroidVar1.svg";
    static Color spikerColor = Color.GREEN;
    private BetterPolygon defTanker = BetterPolygon.rotate(new BetterPolygon(Util.SVGconverter(filepath)), 180);
    private List<Double> defPoints;
    private boolean goingDown = false;
    public boolean isDead = false;
    private Polyline line = new Polyline();

    public Spiker(Panel startPanel) {
        super(startPanel);
        double panelToHorizontalAngle = Math.toDegrees(Math.atan((startPanel.getSmallSide().getPoints().getLast() - startPanel.getSmallSide().getPoints().get(1))
                / (startPanel.getSmallSide().getPoints().get(2) - startPanel.getSmallSide().getPoints().getFirst())));
        if (Double.toString(panelToHorizontalAngle).equals("-0.0")) panelToHorizontalAngle = 180;
        else if (startPanel.isBottomPanel()) {
            panelToHorizontalAngle += 180;
        }
        defPoints = BetterPolygon.rotate(new BetterPolygon(defTanker.getPoints()), panelToHorizontalAngle).getPoints();

        double x = (currentPanel.getSmallSide().getPoints().getFirst() + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().getLast()) / 2;
        pointer.moveTo(x, y);
        acceleration = getPointerAcceleration();
        getPoints().setAll(defPoints);

        currentPanel.addSPiker(this);
        setStroke(spikerColor);

        Main.root.getChildren().add(line);
        line.setStroke(spikerColor);
        line.setEffect(new Glow(Main.glowV));
    }

    public boolean move() {
        if (h < maxH && !reachedTheEdge && !isDead) {
            moveUp();
            drawLine();
        } else if (frameOfMovement >= FRAMES_PER_MOVE && !destroyed && !goingDown) {
            h = 0;
            frameOfMovement = 0;
            acceleration = getPointerAcceleration();
            goingDown = true;
            bulletTimer++;
        } else if (!destroyed && reachedTheEdge && frameOfMovement < FRAMES_PER_MOVE) {
            Enemy enemy = new Enemy(currentPanel);
            while (enemy.frameOfMovement != FRAMES_PER_MOVE - frameOfMovement) {
                enemy.moveUp();
            }
            h = enemy.h;
            frameOfMovement++;
            updatePointer();
            updatePoints();
        } else if (frameOfMovement >= 360) {
            return false;
        }
        bulletTimer--;
        if (bulletTimer <= 0) shoot();
        return true;
    }

    @Override
    protected void uniqueDestroyMethod() {
        if (!isDead) {
            isDead = true;
        } else {
            currentPanel.getSpikers().remove(this);
        }
        if(Main.root.getChildren().contains(this)) Main.root.getChildren().remove(this);
    }

    public void switchToTanker() {
        Tanker tanker = new Tanker(currentPanel);
        Main.root.getChildren().add(tanker);
        isDead = true;
        destroy();
    }

    private void drawLine() {
        double xStart = (currentPanel.getSmallSide().getPoints().get(0) + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double yStart = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().get(3)) / 2;

        double xEnd = pointer.getCenterX();
        double yEnd = pointer.getCenterY();

        line.getPoints().setAll(xStart, yStart, xEnd, yEnd);
    }

    public boolean destroyLine(Player.Bullet bullet, double magnitude) {
        if (line != null && bullet.intersects(line.getLayoutBounds())) {
            Vector vector = new Vector(magnitude, currentPanel.getAngle());
            List<Double> points = line.getPoints();
            line.getPoints().setAll(points.get(0), points.get(1), points.get(2) - vector.getX(), points.get(3) - vector.getY());
            if (vector.getMagnitude() > Math.sqrt(Math.pow(points.get(0) - points.get(2), 2) + Math.pow(points.get(1) - points.get(3), 2))) {
                Main.root.getChildren().remove(line);
                destroy();
            }
            return true;
        }
        return false;
    }
}