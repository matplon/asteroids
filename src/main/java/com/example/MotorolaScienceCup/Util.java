package com.example.MotorolaScienceCup;

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
                    String subString = nextLine.substring(5, nextLine.length() - 3);    // Remove unnecessary characters from the sides
                    String[] li = subString.split(" "); // Remove spaces
                    double previousX = 0, previousY = 0;
                    for (int i = 0; i<li.length; i++) {
                        if(li[i].contains("H")){
                            previousX = Double.parseDouble(li[i+1]);
                            list.add(previousX);
                            list.add(previousY);
                            i++;
                        }
                        else if(li[i].contains("V")){
                            previousY = Double.parseDouble(li[i+1]);
                            list.add(previousX);
                            list.add(previousY);
                            i++;
                        }
                        else{
                            String[] lili = li[i].split(",");   // Remove commas
                            previousX = Double.parseDouble(lili[0]);
                            list.add(previousX);
                            previousY = Double.parseDouble(lili[1]);
                            list.add(previousY);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
