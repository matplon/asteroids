package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Util;
import com.example.MotorolaScienceCup.Vector;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FuseBall extends Enemy{

    private static final String filepath = "flipper.svg";

    private BetterPolygon defFuseBall = BetterPolygon.rotate(new BetterPolygon(Util.SVGconverter(filepath)), 180);
    static Color fuseBallColor = Color.YELLOW;

    private List<Double> defPoints;
    private Polyline currentSide;

    private Vector velocity;
    public com.example.MotorolaScienceCup.Vector fuseBallAcceleration;

    boolean atTop;

    public FuseBall(Panel startPanel) {
        super(startPanel);
        double panelToHorizontalAngle = Math.toDegrees(Math.atan((startPanel.getSmallSide().getPoints().get(3) - startPanel.getSmallSide().getPoints().get(1))
                / (startPanel.getSmallSide().getPoints().get(2) - startPanel.getSmallSide().getPoints().get(0))));
        if (Double.toString(panelToHorizontalAngle).equals("-0.0")) panelToHorizontalAngle = 180;
        else if (startPanel.isBottomPanel()) {
            panelToHorizontalAngle += 180;
        }
        defPoints = BetterPolygon.rotate(new BetterPolygon(defFuseBall.getPoints()), panelToHorizontalAngle).getPoints();

        double x = (currentPanel.getSmallSide().getPoints().get(0) + currentPanel.getSmallSide().getPoints().get(2)) / 2;
        double y = (currentPanel.getSmallSide().getPoints().get(1) + currentPanel.getSmallSide().getPoints().get(3)) / 2;
        pointer.moveTo(x, y);
        acceleration = getPointerAcceleration();
        getPoints().setAll(getFuseBallPoints());

        this.moveTo(currentPanel.getLeftSide().getPoints().get(0), currentPanel.getLeftSide().getPoints().get(1));
        currentSide = currentPanel.getLeftSide();

        fuseBallAcceleration = new Vector();
        velocity = new Vector();

        startPanel.addFuseBall(this);
        setStroke(fuseBallColor);

    }

    private List<Double> getPointsOnSides() {
        double minX1 = currentPanel.getRightSide().getPoints().get(0);
        double minY1 = currentPanel.getRightSide().getPoints().get(1);
        double minX2 = currentPanel.getLeftSide().getPoints().get(0);
        double minY2 = currentPanel.getLeftSide().getPoints().get(1);
        double maxX1 = currentPanel.getRightSide().getPoints().get(2);
        double maxY1 = currentPanel.getRightSide().getPoints().get(3);
        double maxX2 = currentPanel.getLeftSide().getPoints().get(2);
        double maxY2 = currentPanel.getLeftSide().getPoints().get(3);

        double gradX1 = (maxX1 - minX1) / maxH;
        double gradY1 = (maxY1 - minY1) / maxH;
        double gradX2 = (maxX2 - minX2) / maxH;
        double gradY2 = (maxY2 - minY2) / maxH;

        double x1 = minX1 + gradX1 * h;
        double y1 = minY1 + gradY1 * h;
        double x2 = minX2 + gradX2 * h;
        double y2 = minY2 + gradY2 * h;

        return Arrays.asList(x1, y1, x2, y2);
    }

    private List<Double> getFuseBallPoints() {
        List<Double> top = getPointsOnSides();
        double v8 = top.get(0);
        double v9 = top.get(1);
        double v12 = top.get(2);
        double v13 = top.get(3);

        double topLength = Math.sqrt(Math.pow(v8 - v12, 2) + Math.pow(v9 - v13, 2));
        double defTopLength = Math.sqrt(Math.pow(defPoints.get(8) - defPoints.get(12), 2) + Math.pow(defPoints.get(9) - defPoints.get(13), 2));

        double defTopToCenterLengthX = defPoints.get(12) - defPoints.get(2);
        double ratio1 = defTopToCenterLengthX / defTopLength;
        double v2 = v12 - ratio1 * topLength;
        double v10 = v2;

        double defTopToCenterLengthY = defPoints.get(13) - defPoints.get(3);
        double ratio2 = defTopToCenterLengthY / defTopLength;
        double v3 = v13 - ratio2 * topLength;
        double v11 = v3;

        double defCenterToLeftCenterLengthX = defPoints.get(2) - defPoints.get(6);
        double ratio3 = defCenterToLeftCenterLengthX / defTopLength;
        double v6 = v2 - ratio3 * topLength;

        double defCenterToLeftCenterLengthY = defPoints.get(3) - defPoints.get(7);
        double ratio4 = defCenterToLeftCenterLengthY / defTopLength;
        double v7 = v3 - ratio4 * topLength;

        double defCenterToRightCenterLengthX = defPoints.get(2) - defPoints.get(14);
        double ratio5 = defCenterToRightCenterLengthX / defTopLength;
        double v14 = v2 - ratio5 * topLength;

        double defCenterToRightCenterLengthY = defPoints.get(3) - defPoints.get(15);
        double ratio6 = defCenterToRightCenterLengthY / defTopLength;
        double v15 = v3 - ratio6 * topLength;

        double defLeftCenterToLeftBottomLengthX = defPoints.get(6) - defPoints.get(4);
        double ratio7 = defLeftCenterToLeftBottomLengthX / defTopLength;
        double v4 = v6 - ratio7 * topLength;

        double defLeftCenterToLeftBottomLengthY = defPoints.get(7) - defPoints.get(5);
        double ratio8 = defLeftCenterToLeftBottomLengthY / defTopLength;
        double v5 = v7 - ratio8 * topLength;

        double defRightCenterToRightBottomLengthX = defPoints.get(14) - defPoints.get(0);
        double ratio9 = defRightCenterToRightBottomLengthX / defTopLength;
        double v0 = v14 - ratio9 * topLength;

        double defRightCenterToRightBottomLengthY = defPoints.get(15) - defPoints.get(1);
        double ratio10 = defRightCenterToRightBottomLengthY / defTopLength;
        double v1 = v15 - ratio10 * topLength;

        return Arrays.asList(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15);
    }
    public void moveUP() {
        atTop = false;
        double minX = currentSide.getPoints().get(0);
        double minY = currentSide.getPoints().get(1);
        double maxX = currentSide.getPoints().get(2);
        double maxY = currentSide.getPoints().get(3);

        double lengthX = maxX - minX;
        double lengthY = maxY - minY;

        double accX = 2 * lengthX / Math.pow(100, 2);
        double accY = 2 * lengthY / Math.pow(100, 2);

        fuseBallAcceleration.setX(accX);
        fuseBallAcceleration.setY(accY);

        if (Math.abs(getCenterX() - minX+ 2) < Math.abs(maxX - minX) &&
                Math.abs(getCenterY() - minY) + 2 < Math.abs(maxY - minY)) {
            moveTo(getCenterX() + velocity.getX(), getCenterY() + velocity.getY());
            velocity.setX(velocity.getX() + fuseBallAcceleration.getX());
            velocity.setY(velocity.getY() + fuseBallAcceleration.getY());
        }else {
            atTop = true;
        }
    }
    public void moveOnTop() {
        double x, y, lengthX, lengthY, accX, accY;
        if (atTop) {
            if (this.chooseDirection()) {
                if (currentSide == currentPanel.getLeftSide()) {
                    x = currentPanel.getLeftPanel().getLeftSide().getPoints().get(2);
                    y = currentPanel.getLeftPanel().getLeftSide().getPoints().get(3);

                    lengthX = x - currentSide.getPoints().get(2);
                    lengthY = y - currentSide.getPoints().get(3);

                    accX = 2 * lengthX / Math.pow(200, 2);
                    accY = 2 * lengthY / Math.pow(200, 2);

                    fuseBallAcceleration.setX(accX);
                    fuseBallAcceleration.setY(accY);

                    if (Math.abs(getCenterX() - currentSide.getPoints().get(2)) < Math.abs(lengthX)
                            && Math.abs(getCenterY() - currentSide.getPoints().get(3)) < Math.abs(lengthY)) {
                        System.out.println("lol"+ velocity.getX() +" " + velocity.getY());
                        moveTo(getCenterX() + velocity.getX(),
                                getCenterY() + velocity.getY());
                        velocity.setX(velocity.getX() + fuseBallAcceleration.getX());
                        velocity.setY(velocity.getY() + fuseBallAcceleration.getY());
                    }
                    currentSide = currentPanel.getLeftPanel().getLeftSide();
                    currentPanel = currentPanel.getLeftPanel();
                } else {
                    x = currentPanel.getLeftSide().getPoints().get(2);
                    y = currentPanel.getLeftSide().getPoints().get(3);

                    lengthX = x - currentSide.getPoints().get(2);
                    lengthY = y - currentSide.getPoints().get(3);

                    accX = 2 * lengthX / Math.pow(200, 2);
                    accY = 2 * lengthY / Math.pow(200, 2);

                    fuseBallAcceleration.setX(accX);
                    fuseBallAcceleration.setY(accY);

                    if (Math.abs(getCenterX() - currentSide.getPoints().get(2)) < Math.abs(lengthX) &&
                            Math.abs(getCenterY() - currentSide.getPoints().get(3)) < Math.abs(lengthY)) {
                        System.out.println("bruh" + velocity.getX() +" " + velocity.getY());
                        moveTo(getCenterX() + velocity.getX(),
                                getCenterY() + velocity.getY());
                        velocity.setX(velocity.getX() + fuseBallAcceleration.getX());
                        velocity.setY(velocity.getY() + fuseBallAcceleration.getY());
                    }
                    currentSide = currentPanel.getLeftSide();
                }
            } else if (!chooseDirection()) {

                if (currentSide == currentPanel.getRightSide()) {
                    x = currentPanel.getRightPanel().getRightSide().getPoints().get(2);
                    y = currentPanel.getRightPanel().getRightSide().getPoints().get(3);

                    lengthX = x - currentSide.getPoints().get(2);
                    lengthY = y - currentSide.getPoints().get(3);

                    accX = 2 * lengthX / Math.pow(200, 2);
                    accY = 2 * lengthY / Math.pow(200, 2);

                    fuseBallAcceleration.setX(accX);
                    fuseBallAcceleration.setY(accY);

                    if (Math.abs(getCenterX() - currentSide.getPoints().get(2)) < Math.abs(lengthX) &&
                            Math.abs(getCenterY() - currentSide.getPoints().get(3)) < Math.abs(lengthY)) {
                        System.out.println("nigga"+ velocity.getX() +" " + velocity.getY());
                        moveTo(getCenterX() + velocity.getX(),
                                getCenterY() + velocity.getY());
                        velocity.setX(velocity.getX() + fuseBallAcceleration.getX());
                        velocity.setY(velocity.getY() + fuseBallAcceleration.getY());
                    }
                    currentSide = currentPanel.getRightPanel().getRightSide();
                    currentPanel = currentPanel.getRightPanel();
                } else {
                    x = currentPanel.getRightSide().getPoints().get(2);
                    y = currentPanel.getRightSide().getPoints().get(3);

                    lengthX = x - currentSide.getPoints().get(2);
                    lengthY = y - currentSide.getPoints().get(3);

                    accX = 2 * lengthX / Math.pow(200, 2);
                    accY = 2 * lengthY / Math.pow(200, 2);

                    fuseBallAcceleration.setX(accX);
                    fuseBallAcceleration.setY(accY);

                    if (Math.abs(getCenterX() - currentSide.getPoints().get(2)) < Math.abs(lengthX) &&
                            Math.abs(getCenterY() - currentSide.getPoints().get(3)) < Math.abs(lengthY)) {
                        System.out.println("dick"+ velocity.getX() +" " + velocity.getY());
                        moveTo(getCenterX() + velocity.getX(),
                                getCenterY() + velocity.getY());
                        velocity.setX(velocity.getX() + fuseBallAcceleration.getX());
                        velocity.setY(velocity.getY() + fuseBallAcceleration.getY());
                    }
                    currentSide = currentPanel.getRightSide();
                }
            }
        }
    }
    private boolean chooseDirection() {
        int distLeft = 0;
        Panel nextPanel = currentPanel.getLeftPanel();
        while (!nextPanel.equals(Main.player.getCurrentPanel())) {
            distLeft++;
            nextPanel = nextPanel.getLeftPanel();
        }
        int distRight = 0;
        nextPanel = currentPanel.getRightPanel();
        while (!nextPanel.equals(Main.player.getCurrentPanel())) {
            distRight++;
            nextPanel = nextPanel.getRightPanel();
        }
        if (distLeft < distRight) return true;
        else if (distRight < distLeft) return false;
        else {
            Random random = new Random();
            int dirChooser = random.nextInt(2);
            return (dirChooser == 0);
        }
    }
    public Polyline getCurrentSide(){return currentSide;}
}
