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
                    Character charAt = nextLine.charAt(0);
                    int indexesToRemoveLeft = 0;
                    while (!Character.isDigit(charAt)) {
                        indexesToRemoveLeft++;
                        charAt = nextLine.charAt(indexesToRemoveLeft);
                    }
                    int indexesToRemoveRight = 0;
                    charAt = nextLine.charAt(nextLine.length() - indexesToRemoveRight - 1);
                    while (!Character.isDigit(charAt)) {
                        indexesToRemoveRight++;
                        charAt = nextLine.charAt(nextLine.length() - indexesToRemoveRight - 1);
                    }
                    String[] li = getStrings(nextLine, indexesToRemoveLeft, indexesToRemoveRight);
                    double previousX = 0, previousY = 0;
                    for (int i = 0; i < li.length; i++) {
                        if (li[i].contains("H")) {
                            previousX = Double.parseDouble(li[i + 1]);
                            list.add(previousX);
                            list.add(previousY);
                            i++;
                        } else if (li[i].contains("V")) {
                            previousY = Double.parseDouble(li[i + 1]);
                            list.add(previousX);
                            list.add(previousY);
                            i++;
                        } else if (!li[i].isEmpty()) {
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

    private static String[] getStrings(String nextLine, int indexesToRemoveLeft, int indexesToRemoveRight) {
        String subString = nextLine.substring(indexesToRemoveLeft, nextLine.length() - indexesToRemoveRight);    // Remove unnecessary characters from the sides
        if (subString.contains("Z")) {
            subString = subString.substring(0, subString.length() - 3);
        }
        String substringNew = subString;
        if (subString.contains("M")) {
            substringNew = subString.replaceAll("M", "");
        }
        subString = substringNew.replaceAll(" {2}", " ");
        String[] li = subString.split(" "); // Remove spaces
        return li;
    }
}
