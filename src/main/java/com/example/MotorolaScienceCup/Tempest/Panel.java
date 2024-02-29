package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Vector;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

import static com.example.MotorolaScienceCup.Tempest.Main.*;


public class Panel {
    public Polyline smallSide;
    public Polyline bigSide;
    public Polyline leftSide;
    public Polyline rightSide;
    public Panel leftPanel;
    public Panel rightPanel;

    public List<Player.Bullet> playerBullets = new ArrayList<>();
    private List<Enemy.Bullet> enemyBullets = new ArrayList<>();
    private List<Flipper> flippers = new ArrayList<>();
    private List<Tanker> tankers = new ArrayList<>();
    private List<Spiker> spikers = new ArrayList<>();
    private List<Fuseball> fuseBalls = new ArrayList<>();
    public Polyline spikerLine;
    public double spikerLinePercentage;

    private double length;
    private double angle;
    private Color color;
    private int index;

    public Panel() {
    }

    private void updatePlayerBullets(boolean nextLevel) {
        List<Player.Bullet> bulletsToDestroy = new ArrayList<>();
        List<Flipper> flippersToDestroy = new ArrayList<>();
        List<Tanker> tankersToDestroy = new ArrayList<>();
        List<Spiker> spikersToDestroy = new ArrayList<>();
        List<Fuseball> fuseballsToDestroy = new ArrayList<>();

        for (Player.Bullet bullet : playerBullets) {
            if (checkEdgeFlipper() && bullet.h == length) {
                Main.addPoints(FLIPPER_POINTS);
                bulletsToDestroy.add(bullet);
            }
            if (!bullet.move(nextLevel)) {
                bulletsToDestroy.add(bullet);
            }
            for (Flipper flipper : flippers) {
                if (bullet.intersects(flipper.getLayoutBounds()) && !bulletsToDestroy.contains(bullet)) {
                    Main.addPoints(FLIPPER_POINTS);
                    flippersToDestroy.add(flipper);
                    bulletsToDestroy.add(bullet);
                }
            }
            for (Tanker tanker : tankers) {
                if (bullet.intersects(tanker.getLayoutBounds()) && !bulletsToDestroy.contains(bullet)) {
                    Main.addPoints(TANKER_POINTS);
                    tankersToDestroy.add(tanker);
                    bulletsToDestroy.add(bullet);
                }
            }
            for (Spiker spiker : spikers) {
                if (!spikers.isEmpty() && bullet.intersects(spiker.getLayoutBounds()) && !bulletsToDestroy.contains(bullet)) {
                    Main.addPoints(SPIKER_POINTS);
                    spikersToDestroy.add(spiker);
                    bulletsToDestroy.add(bullet);
                }
            }
            for (Fuseball fuseBall : fuseBalls) {
                if (bullet.intersects(fuseBall.getLayoutBounds()) && !bulletsToDestroy.contains(bullet)) {
                    Main.addPoints(FUSEBALL_POINTS);
                    fuseballsToDestroy.add(fuseBall);
                    bulletsToDestroy.add(bullet);
                }
            }
            if (spikers.isEmpty() && !bulletsToDestroy.contains(bullet)) {
                if (destroyLine(bullet, bullet.getRadius() * 3.5)) {
                    Main.addPoints(SPIKELINE_POINTS);
                    bulletsToDestroy.add(bullet);
                }
            }
            for (Flipper flipper : flippersToDestroy) {
                flipper.destroy();
            }
            for (Tanker tanker : tankersToDestroy) {
                tanker.destroy();
            }
            for (Spiker spiker : spikersToDestroy) {
                spiker.destroy();
            }
        }
        for (Player.Bullet bullet : bulletsToDestroy) {
            bullet.remove();
        }
    }

    private boolean destroyLine(Player.Bullet bullet, double magnitude) {
        if (spikerLine != null) {
            double spikerLineLength = Math.sqrt(Math.pow(spikerLine.getPoints().get(0) - spikerLine.getPoints().get(2), 2) +
                    Math.pow(spikerLine.getPoints().get(1) - spikerLine.getPoints().get(3), 2));
            if (bullet.h <= spikerLineLength) {
                Vector vector = new Vector(magnitude, getAngle());
                List<Double> points = spikerLine.getPoints();
                spikerLine.getPoints().setAll(points.get(0), points.get(1), points.get(2) - vector.getX(), points.get(3) - vector.getY());
                if (vector.getMagnitude() >= Math.sqrt(Math.pow(points.get(0) - points.get(2), 2) + Math.pow(points.get(1) - points.get(3), 2))) {
                    Main.root.getChildren().remove(spikerLine);
                    spikerLine = null;
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkEdgeFlipper() {
        for (int i = 0; i < flippers.size(); i++) {
            if (flippers.get(i).reachedTheEdge) {
                flippers.get(i).destroy();
                return true;
            }
        }
        if(leftPanel != null){
            for (int i = 0; i < leftPanel.getFlippers().size(); i++) {
                if (leftPanel.getFlippers().get(i).reachedTheEdge) {
                    leftPanel.getFlippers().get(i).destroy();
                    return true;
                }
            }
        }
        if(rightPanel != null){
            for (int i = 0; i < rightPanel.getFlippers().size(); i++) {
                if (rightPanel.getFlippers().get(i).reachedTheEdge) {
                    rightPanel.getFlippers().get(i).destroy();
                    return true;
                }
            }
        }
        return false;
    }

    private void updateEnemyBullets() {
        List<Enemy.Bullet> bulletsToDestroy = new ArrayList<>();
        for (Enemy.Bullet bullet : enemyBullets) {
            bullet.move();
            if (bullet.checkIfOutside()) {
                bulletsToDestroy.add(bullet);
                if (Main.player.getCurrentPanel().equals(this)) Main.gameOver(false);
            }
        }
        for (Enemy.Bullet bullet : bulletsToDestroy) {
            bullet.remove();
            enemyBullets.remove(bullet);
        }
    }

    public void update(boolean nextLevel) {
        List<Flipper> flippersToRemove = new ArrayList<>();
        List<Tanker> tankersToDestroy = new ArrayList<>();
        List<Spiker> spikersToDestroy = new ArrayList<>();
        List<Fuseball> fuseBallsToRemove = new ArrayList<>();
        for (Flipper flipper : flippers){
            if (!flipper.move()) flippersToRemove.add(flipper);
            if(flipper.reachedTheEdge){
                if(this == player.currentPanel){
                    Main.gameOver(false);
                }
            }
        }
        for (Tanker tanker : tankers) {
            if (!tanker.move()) tankersToDestroy.add(tanker);
        }
        for (Spiker spiker : spikers) {
            if (!spiker.move()) {
                spikersToDestroy.add(spiker);
            }
        }
        for (Fuseball fuseBall : fuseBalls) {
            if (fuseBall.currentPanel != this) {
                fuseBallsToRemove.add(fuseBall);
            }
            if (!fuseBall.move()) {
                Main.gameOver(false);
            }
        }
        updatePlayerBullets(nextLevel);
        updateEnemyBullets();
        if(nextLevel && spikerLine != null){
            drawNewSpikerLine();
        }
        if(nextLevel && checkIfWon()){
            Main.newLevel();
        }
        for (Flipper flipper : flippersToRemove) {
            flippers.remove(flipper);
        }
        for (Tanker tanker : tankersToDestroy) {
            tanker.switchToFlipper();
        }
        for (Spiker spiker : spikersToDestroy) {
            spiker.switchToTanker();
        }
        for (Fuseball fuseBall : fuseBallsToRemove) {
            fuseBalls.remove(fuseBall);
        }
    }

    public void drawNewSpikerLine() {
        double newStartX = (Main.panels.get(Main.tempPanels.indexOf(this)).getSmallSide().getPoints().get(0) +
                Main.panels.get(Main.tempPanels.indexOf(this)).getSmallSide().getPoints().get(2)) / 2;
        double newStartY = (Main.panels.get(Main.tempPanels.indexOf(this)).getSmallSide().getPoints().get(1) +
                Main.panels.get(Main.tempPanels.indexOf(this)).getSmallSide().getPoints().get(3)) / 2;

        double lineLength = Math.sqrt(Math.pow(spikerLine.getPoints().get(2) - spikerLine.getPoints().get(0), 2) +
                Math.pow(spikerLine.getPoints().get(3) - spikerLine.getPoints().get(1), 2));
        spikerLinePercentage = lineLength / Main.panels.get(Main.tempPanels.indexOf(this)).getLength();

        double magnitude = spikerLinePercentage * panels.get(tempPanels.indexOf(this)).getLength();

        double lineAngle = panels.get(tempPanels.indexOf(this)).getAngle();
        Vector vector = new Vector(magnitude, lineAngle);
        spikerLine = new Polyline();
        spikerLine.getPoints().setAll(newStartX, newStartY, newStartX + vector.getX(), newStartY+vector.getY());
        spikerLine.setStroke(Spiker.spikerColor);
        root.getChildren().add(spikerLine);
        if(Shape.intersect(spikerLine, player).getLayoutBounds().getWidth() > 0){
            Main.gameOver(true);
            return;
        }
        List<Player.Bullet> bulletsToDestroy = new ArrayList<>();
        for (Player.Bullet bullet1 : playerBullets){
            if(spikerLine != null){
                if(Shape.intersect(spikerLine, bullet1).getLayoutBounds().getWidth() > 0){
                    vector = new Vector(bullet1.getRadius() * 3, lineAngle);
                    List<Double> points = spikerLine.getPoints();
                    spikerLine.getPoints().setAll(points.get(0), points.get(1), points.get(2) - vector.getX(), points.get(3) - vector.getY());
                    if (vector.getMagnitude() >= Math.sqrt(Math.pow(points.get(0) - points.get(2), 2) + Math.pow(points.get(1) - points.get(3), 2))) {
                        Main.root.getChildren().remove(spikerLine);
                        spikerLine = null;
                    }
                    bulletsToDestroy.add(bullet1);
                }
            }
        }
        for (Player.Bullet bullet1 : bulletsToDestroy) bullet1.remove();
    }

    public boolean checkIfWon(){
        if(player.intersects(Main.panels.get(Main.tempPanels.indexOf(this)).getSmallSide().getLayoutBounds())) return true;
        return false;
    }

    public void addFlipper(Flipper flipper) {
        flippers.add(flipper);
    }

    public void addTanker(Tanker tanker) {
        tankers.add(tanker);
    }

    public void addSPiker(Spiker spiker) {
        spikers.add(spiker);
    }

    public void addPlayerBullet(Player.Bullet bullet) {
        playerBullets.add(bullet);
    }

    public void addEnemyBullet(Enemy.Bullet bullet) {
        enemyBullets.add(bullet);
    }

    public void changeColorSmallSide(Color color) {
        smallSide.setStroke(color);
        smallSide.setFill(color);
        this.color = Color.YELLOW;
    }

    public void changeColorBigSide(Color color) {
        bigSide.setStroke(color);
        bigSide.setFill(color);
        this.color = Color.YELLOW;
    }

    public void changeColorRightSide(Color color) {
        rightSide.setStroke(color);
        rightSide.setFill(color);
        this.color = Color.YELLOW;
    }

    public Color getColor() {
        return color;
    }

    public void changeColorLeftSide(Color color) {
        leftSide.setStroke(color);
        leftSide.setFill(color);
        this.color = Color.YELLOW;
    }

    public void changeColor(Color color) {
        changeColorBigSide(color);
        changeColorLeftSide(color);
        changeColorRightSide(color);
    }

    public boolean isBottomPanel() {
        double midYRight = (rightSide.getPoints().get(1) + rightSide.getPoints().get(3)) / 2;
        double midYLeft = (leftSide.getPoints().get(1) + leftSide.getPoints().get(3)) / 2;

        return (midYRight > Graphics.mapCenterY || midYLeft > Graphics.mapCenterY);
    }

    public void setSmallSide(Polyline smallSide) {
        this.smallSide = smallSide;
    }

    public void setBigSide(Polyline bigSide) {
        this.bigSide = bigSide;
    }

    public void setLeftSide(Polyline leftSide) {
        this.leftSide = leftSide;
    }

    public void setRightSide(Polyline rightSide) {
        this.rightSide = rightSide;
    }

    public void setLeftPanel(Panel leftPanel) {
        this.leftPanel = leftPanel;
    }

    public void setRightPanel(Panel rightPanel) {
        this.rightPanel = rightPanel;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Polyline getSmallSide() {
        return smallSide;
    }

    public Polyline getBigSide() {
        return bigSide;
    }

    public Polyline getLeftSide() {
        return leftSide;
    }

    public Polyline getRightSide() {
        return rightSide;
    }

    public Panel getLeftPanel() {
        return leftPanel;
    }

    public Panel getRightPanel() {
        return rightPanel;
    }

    public List<Player.Bullet> getPlayerBullets() {
        return playerBullets;
    }

    public List<Flipper> getFlippers() {
        return flippers;
    }

    public List<Tanker> getTankers() {
        return tankers;
    }

    public List<Spiker> getSpikers() {
        return spikers;
    }

    public List<Fuseball> getFuseBalls() {
        return fuseBalls;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "Panel{" +
                "smallSide=" + smallSide +
                ", bigSide=" + bigSide +
                ", leftSide=" + leftSide +
                ", rightSide=" + rightSide +
                ", leftPanel=" + Main.panels.indexOf(leftPanel) +
                ", rightPanel=" + Main.panels.indexOf(rightPanel) +
                '}';
    }
}