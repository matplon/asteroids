package com.example.MotorolaScienceCup;

import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;

import javax.sound.sampled.AudioInputStream;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class Util {
    public static List<Double> getMapPoints(String filepath) { // Convert .svg file to a list of coordinates
        InputStream in = Sound.class.getResourceAsStream(filepath);
        InputStream of = new BufferedInputStream(in);
        List<Double> list = new ArrayList<>();
        Scanner scanner = new Scanner(of);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            String leftRemoved = nextLine.replaceAll("^\\s+", "");  // Remove whitespaces from the sides
            nextLine = leftRemoved.replaceAll("\\s+$", "");
            if (nextLine.startsWith("d=")) {    // Find the path line in the .svg file
                Character charAt = nextLine.charAt(0);
                int indexesToRemoveLeft = 0;
                while (charAt != 'M') {
                    indexesToRemoveLeft++;
                    charAt = nextLine.charAt(indexesToRemoveLeft);
                }
                int indexesToRemoveRight = 0;
                charAt = nextLine.charAt(nextLine.length() - indexesToRemoveRight - 1);
                while (!Character.isDigit(charAt)) {
                    indexesToRemoveRight++;
                    charAt = nextLine.charAt(nextLine.length() - indexesToRemoveRight - 1);
                }
                String tempString = nextLine.substring(indexesToRemoveLeft, nextLine.length() - indexesToRemoveRight);
                nextLine = convertPath(tempString);
                tempString = nextLine.substring(3);
                nextLine = tempString;
                ArrayList<ArrayList<Double>> listOfLines = new ArrayList<>();
                String[] paths = nextLine.split(" M ");
                for (int i = 0; i < paths.length; i++) {
                    String line = paths[i];
                    ArrayList<Double> cords = new ArrayList<>();
                    String[] points = line.split(" ");
                    for (int j = 0; j < points.length; j++) {
                        String[] finalPoints = points[j].split(",");
                        cords.add(Double.parseDouble(finalPoints[0]));
                        cords.add(Double.parseDouble(finalPoints[1]));
                    }
                    listOfLines.add(cords);
                }
                if(listOfLines.size() == 1) return SVGconverter(filepath);
                return getPolygonPoints(listOfLines);
            }
        }
        return list;
    }

    public static List<Double> SVGconverter(String filepath) {// Convert .svg file to a list of coordinates
        InputStream in = Sound.class.getResourceAsStream(filepath);
        InputStream of = new BufferedInputStream(in);

        filepath = "src/main/resources/com/example/MotorolaScienceCup/"+filepath;
        List<Double> list = new ArrayList<>();
        Scanner scanner = new Scanner(of);
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

    public static String convertPath(String svgPath) {
        String[] tokens = svgPath.split("[,\\s]+");
        StringBuilder stringBuilder = new StringBuilder();
        String lastX = null, lastY = null;
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("M")) {
                stringBuilder.append(" ").append(tokens[i]);
                int indexAfterCommand = 0;
                while (i + 1 + indexAfterCommand < tokens.length && isNumeric(tokens[i + 1 + indexAfterCommand])) {
                    if (indexAfterCommand % 2 == 0) {
                        lastX = tokens[i + 1 + indexAfterCommand];
                        stringBuilder.append(" ").append(lastX);
                    } else {
                        lastY = tokens[i + 1 + indexAfterCommand];
                        stringBuilder.append(",").append(lastY);
                    }
                    indexAfterCommand++;
                }
                i += indexAfterCommand;
            } else if (tokens[i].equals("H")) {
                stringBuilder.append(" ").append(tokens[i + 1]).append(",").append(lastY);
                lastX = tokens[i+1];
                i++;
            }
            else if(tokens[i].equals("V")){
                stringBuilder.append(" ").append(lastX).append(",").append(tokens[i+1]);
                lastY = tokens[i+1];
                i++;
            }
            else if(tokens[i].equals("Z")){
                stringBuilder.append(" ").append(tokens[i]);
            }
        }
        return stringBuilder.toString();
    }

    public static List<Double> getPolygonPoints(ArrayList<ArrayList<Double>> lines){
        List<Double> points = new ArrayList<>();
        if(Math.round(lines.get(0).get(lines.get(0).size() - 2)) == Math.round(lines.get(lines.size() - 1).get(0)) && Math.round(lines.get(0).get(lines.get(0).size() - 1)) == Math.round(lines.get(lines.size() - 1).get(1))){
            for (int i = lines.size()-1; i >= 0; i--) {
                if(i == lines.size()-1){
                    points.add(lines.get(i).get(0));
                    points.add(lines.get(i).get(1));
                }
                for (int j = 2; j < lines.get(i).size(); j+=2) {
                    points.add(lines.get(i).get(j));
                    points.add(lines.get(i).get(j+1));
                }
            }
        }
        else if(Math.round(lines.get(0).get(0)) == Math.round(lines.get(1).get(0)) && Math.round(lines.get(0).get(1)) == Math.round(lines.get(1).get(1))){
            for (int i = 0; i < lines.size(); i++) {
                if(i == 0){
                    points.add(lines.get(i).get(0));
                    points.add(lines.get(i).get(1));
                }
                for (int j = 2; j < lines.get(i).size(); j+=2) {
                    points.add(lines.get(i).get(j));
                    points.add(lines.get(i).get(j+1));
                }
            }
        }
        else if(Math.round(lines.get(0).get(0)) == Math.round(lines.get(1).get(lines.get(1).size() - 2)) && Math.round(lines.get(0).get(1)) == Math.round(lines.get(1).get(lines.get(1).size() - 1))){
            for (int i = lines.size()-1; i >= 0; i--) {
                if(i == lines.size()-1){
                    points.add(lines.get(i).get(0));
                    points.add(lines.get(i).get(1));
                }
                for (int j = 2; j < lines.get(i).size(); j+=2) {
                    points.add(lines.get(i).get(j));
                    points.add(lines.get(i).get(j+1));
                }
            }
        }
        return points;
    }

    public static ArrayList<ArrayList<Double>> SVGconverterForLines(String filepath) { // Convert .svg file to a list of coordinates
        InputStream in = Sound.class.getResourceAsStream(filepath);
        InputStream of = new BufferedInputStream(in);

        filepath = "src/main/resources/com/example/MotorolaScienceCup/"+filepath;
        ArrayList<ArrayList<Double>> list = new ArrayList<>();
        Scanner scanner = new Scanner(of);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            String leftRemoved = nextLine.replaceAll("^\\s+", "");  // Remove whitespaces from the sides
            nextLine = leftRemoved.replaceAll("\\s+$", "");
            if (nextLine.startsWith("d=")) {    // Find the path line in the .svg file
                nextLine = nextLine.substring(5, nextLine.length() - 5);
                String[] paths = nextLine.split(" M ");
                for (int i = 0; i < paths.length; i++) {
                    String line = paths[i];
                    ArrayList<Double> cords = new ArrayList<>();
                    String[] points = line.split(" ");
                    for (int j = 0; j < points.length; j++) {
                        String[] finalPoints = points[j].split(",");
                        cords.add(Double.parseDouble(finalPoints[0]));
                        cords.add(Double.parseDouble(finalPoints[1]));
                    }
                    list.add(cords);
                }

            }
        }
        return list;
    }

    public static List<Double> transformPointsToList(String filePath) {
        StringBuilder content = new StringBuilder();
        InputStream in = Sound.class.getResourceAsStream(filePath);
        InputStream of = new BufferedInputStream(in);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(of))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n"); // Append each line with a newline character
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getPointsFromString(content.toString());
    }

    public static List<Double> getPointsFromString(String points) {
        List<Double> coordinates = new ArrayList<>();
        // Remove square brackets and split by comma
        String[] pointStrings = points.replaceAll("\\[|\\]", "").split(",\\s+");
        for (String pointString : pointStrings) {
            // Split each point by comma, convert to double, and add to the list
            String[] coordinatesString = pointString.split(",");
            for (String coordinate : coordinatesString) {
                coordinates.add(Double.parseDouble(coordinate.trim()));
            }
        }
        return coordinates;
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
