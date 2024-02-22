package com.example.MotorolaScienceCup.Tempest;

import com.example.MotorolaScienceCup.BetterPolygon;
import com.example.MotorolaScienceCup.Particle;
import com.example.MotorolaScienceCup.Vector;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;

import static com.example.MotorolaScienceCup.Tempest.Main.root;


public class Panel {
    private Polyline smallSide;
    private Polyline bigSide;
    private Polyline leftSide;
    private Polyline rightSide;
    private Panel leftPanel;
    private Panel rightPanel;

    private List<Player.Bullet> playerBullets = new ArrayList<>();
    private List<Enemy.Bullet> enemyBullets = new ArrayList<>();
    private List<Flipper> flippers = new ArrayList<>();
    private List<Tanker> tankers = new ArrayList<>();
    private List<Spiker> spikers = new ArrayList<>();

    private double length;
    private double angle;
    private Color color;

    private Polyline spikerLine;

    public Polyline getSpikerLine() {
        return spikerLine;
    }

    public void setSpikerLine(Polyline spikerLine) {
        this.spikerLine = spikerLine;
    }

    public Panel() {
    }

    public void scalePanel(double scale){
        double angle = Math.toDegrees(Math.atan2((smallSide.getPoints().get(1) + smallSide.getPoints().get(3)) / 2 - Graphics.mapCenterY,
                (smallSide.getPoints().get(0) + smallSide.getPoints().get(2)) /2 - Graphics.mapCenterX));
        Vector vector = new Vector(scale, angle);
        for (int i = 0; i < smallSide.getPoints().size(); i+=2) {
            smallSide.getPoints().set(i, smallSide.getPoints().get(i) + vector.getX());
            smallSide.getPoints().set(i+1, smallSide.getPoints().get(i+1) + vector.getY());
        }

        angle = Math.toDegrees(Math.atan2((bigSide.getPoints().get(1) + bigSide.getPoints().get(3)) / 2 - Graphics.mapCenterY,
                (bigSide.getPoints().get(0) + bigSide.getPoints().get(2)) /2 - Graphics.mapCenterX));
        vector = new Vector(scale, angle);
        for (int i = 0; i < bigSide.getPoints().size(); i+=2) {
            bigSide.getPoints().set(i, bigSide.getPoints().get(i) + vector.getX());
            bigSide.getPoints().set(i+1, bigSide.getPoints().get(i+1) + vector.getY());
        }

        angle = Math.toDegrees(Math.atan2((rightSide.getPoints().get(1) + rightSide.getPoints().get(3)) / 2 - Graphics.mapCenterY,
                (rightSide.getPoints().get(0) + rightSide.getPoints().get(2)) /2 - Graphics.mapCenterX));
        vector = new Vector(scale, angle);
        for (int i = 0; i < rightSide.getPoints().size(); i+=2) {
            rightSide.getPoints().set(i, rightSide.getPoints().get(i) + vector.getX());
            rightSide.getPoints().set(i+1, rightSide.getPoints().get(i+1) + vector.getY());
        }

        angle = Math.toDegrees(Math.atan2((leftSide.getPoints().get(1) + leftSide.getPoints().get(3)) / 2 - Graphics.mapCenterY,
                (leftSide.getPoints().get(0) + leftSide.getPoints().get(2)) /2 - Graphics.mapCenterX));
        vector = new Vector(scale, angle);
        for (int i = 0; i < leftSide.getPoints().size(); i+=2) {
            leftSide.getPoints().set(i, leftSide.getPoints().get(i) + vector.getX());
            leftSide.getPoints().set(i+1, leftSide.getPoints().get(i+1) + vector.getY());
        }
    }

    private void updatePlayerBullets(){
        List<Player.Bullet> bulletsToDestroy = new ArrayList<>();
        List<Flipper> flippersToDestroy = new ArrayList<>();
        List<Tanker> tankersToDestroy = new ArrayList<>();
        List<Spiker> spikersToDestroy = new ArrayList<>();

        for (Player.Bullet bullet : playerBullets){
            bullet.move();
            if(checkEdgeFlipper(bullet)){
                bulletsToDestroy.add(bullet);
                continue;
            }
            for (Flipper flipper : flippers){
                if(bullet.intersects(flipper.getLayoutBounds()) && !bulletsToDestroy.contains(bullet)){
                    flippersToDestroy.add(flipper);
                    bulletsToDestroy.add(bullet);
                }
            }
            for (Tanker tanker : tankers){
                if(bullet.intersects(tanker.getLayoutBounds()) && !bulletsToDestroy.contains(bullet)){
                    tankersToDestroy.add(tanker);
                    bulletsToDestroy.add(bullet);
                }
            }
            for (Spiker spiker : spikers){
                if(bullet.intersects(spiker.getLayoutBounds()) && !bulletsToDestroy.contains(bullet) && !spikers.get(0).isDead){
                    spikersToDestroy.add(spiker);
                    bulletsToDestroy.add(bullet);
                }
            }
            if (!spikers.isEmpty() && !bulletsToDestroy.contains(bullet) && spikers.get(0).isDead){
                if (spikers.get(0).destroyLine(bullet, bullet.getRadius() * 2)) bulletsToDestroy.add(bullet);
            }
            if(bullet.ifOutside() && !bulletsToDestroy.contains(bullet)) bulletsToDestroy.add(bullet);
            for (Flipper flipper : flippersToDestroy){
                flipper.destroy();
            }
            for(Tanker tanker : tankersToDestroy){
                tanker.destroy();
            }
            for (Spiker spiker : spikersToDestroy){
                spiker.destroy();
            }
        }
        for (Player.Bullet bullet : bulletsToDestroy){
            bullet.remove();
        }

    }

    private boolean checkEdgeFlipper(Player.Bullet bullet){
        Flipper toDestroy = null;
        for(Flipper flipper : flippers){
            if(flipper.reachedTheEdge){
                toDestroy = flipper;
                break;
            }
        }
        if(toDestroy != null){
            toDestroy.destroy();
            return true;
        }
        return false;
    }

    private void updateEnemyBullets(){
        List<Enemy.Bullet> bulletsToDestroy = new ArrayList<>();
        for(Enemy.Bullet bullet : enemyBullets){
            bullet.move();
            if (bullet.checkIfOutside()) {
                bulletsToDestroy.add(bullet);
                if(Main.player.getCurrentPanel().equals(this)) Main.gameOver();
            }
        }
        for (Enemy.Bullet bullet : bulletsToDestroy){
            bullet.remove();
            enemyBullets.remove(bullet);
        }
    }

    public void update(){
        List<Tanker> tankersToDestroy = new ArrayList<>();
        List<Spiker> spikersToDestroy = new ArrayList<>();
        for(Flipper flipper : flippers)
            flipper.move();
        for(Tanker tanker : tankers){
            if(!tanker.move()) tankersToDestroy.add(tanker);
        }
        for(Spiker spiker : spikers){
            if(!spiker.move()) spikersToDestroy.add(spiker);
        }
        updatePlayerBullets();
        updateEnemyBullets();
        for (Tanker tanker : tankersToDestroy){
            tanker.destroy();
        }
        for (Spiker spiker : spikersToDestroy){
            spiker.switchToTanker();
        }
    }

    private void checkForHits() {
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

    public void addEnemyBullet(Enemy.Bullet bullet){
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