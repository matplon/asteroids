package com.example.MotorolaScienceCup.Tempest;


import com.example.MotorolaScienceCup.BetterPolygon;

public class HUD {
    final static int baseRadius = 40;
    public static void drawMap(BetterPolygon base){
        base.scale(baseRadius/base.getRadius());
        
    }
}
