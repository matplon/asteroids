package com.example.MotorolaScienceCup.Asteroids;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {
    public static List<Double> SVGconverter(String filepath) { // Convert .svg file to a list of coordinates
        List<Double> list = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filepath));
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                String leftRemoved = nextLine.replaceAll("^\\s+", "");  // Remove whitespaces from the sides
                nextLine = leftRemoved.replaceAll("\\s+$", "");
                if (nextLine.startsWith("d=")) {    // Find the path line in the .svg file
                    String subString = nextLine.substring(5, nextLine.length() - 3);// Remove unnecessary characters from the sides
                    String[] li = subString.split(" "); // Remove spaces
                    for (String s : li) {
                        String[] lili = s.split(",");   // Remove commas
                        list.add(Double.parseDouble(lili[0]));
                        list.add(Double.parseDouble(lili[1]));
                    }
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
