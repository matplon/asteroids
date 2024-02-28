package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Util;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class Tanker extends Enemy {
    private final double maxHPercentageTillDestruction = 0.75;
    static Color tankerColor = Color.VIOLET;
    private static final String filepath = "asteroidVar1.svg";
    private BetterPolygon defTanker = BetterPolygon.rotate(new BetterPolygon(Util.SVGconverter(filepath)), 180);
    private List<Double> defPoints;

    public Tanker(Panel startPanel) {
        super(startPanel);
        double panelToHorizontalAngle = Math.toDegrees(Math.atan((startPanel.getSmallSide().getPoints().get(3) - startPanel.getSmallSide().getPoints().get(1))
                / (startPanel.getSmallSide().getPoints().get(2) - startPanel.getSmallSide().getPoints().get(0))));
        if (Double.toString(panelToHorizontalAngle).equals("-0.0")) panelToHorizontalAngle = 180;
        else if (startPanel.isBottomPanel()) {
            panelToHorizontalAngle += 180;
        }
        defPoints = BetterPolygon.rotate(new BetterPolygon(defTanker.getPoints()), panelToHorizontalAngle).getPoints();

        double x = (currentPanel.getSmallSide().getPoints().get(0) + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().get(3)) / 2;
        pointer.moveTo(x, y);
        acceleration = getPointerAcceleration();
        getPoints().setAll(defPoints);

        currentPanel.addTanker(this);
        setStroke(tankerColor);
    }

    public boolean move() {
        if (h < maxH) {
            moveUp();
        }
        if (!reachedTheEdge) {
            bulletTimer--;
            if (bulletTimer <= 0) shoot();
        }
        return h < maxH * maxHPercentageTillDestruction;
    }

    @Override
    protected void uniqueDestroyMethod() {
        Main.root.getChildren().remove(this);
        currentPanel.getTankers().remove(this);

        Flipper flipper1 = new Flipper(currentPanel.getLeftPanel());
        Flipper flipper2 = new Flipper(currentPanel.getRightPanel());

        double hPercentage = h / maxH;

        double targetH1 = currentPanel.getLeftPanel().getLength() * hPercentage;
        double targetH2 = currentPanel.getRightPanel().getLength() * hPercentage;

        while (Math.round(flipper1.h) != Math.round(targetH1)) {
            flipper1.moveUp();
        }
        while (Math.round(flipper2.h) != Math.round(targetH2)) {
            flipper2.moveUp();
        }

        Main.root.getChildren().addAll(flipper1, flipper2);
    }
}