package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;

import java.util.Arrays;
import java.util.List;

public class Tanker extends BetterPolygon {
    private static final String filepath = "flipper.svg";
//    private static final defTanker =
    static final List<Double> pointerPoints = Arrays.asList(0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0);
    private Panel currentPanel;
    private double h;
    private double maxH;
    private BetterPolygon pointer;
    private double frameOfMovement;
//    private final double acceleration;
    private boolean reachedTheEdge = false;

    public Tanker(Panel startPanel) {
        super(null);

    }
}
