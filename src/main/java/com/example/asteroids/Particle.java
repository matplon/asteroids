package com.example.asteroids;

import javafx.scene.shape.Polygon;

public class Particle extends Polygon {

    private final double ROTATION_SPEED = 360;    // Degrees/second
    private final double THRUST = 5;    // Pixels/second
    private final double FRICTION = 0.7;    // Coefficient of friction
    private double centerX;
    private double centerY;
    private double angle;
    private double rotation;
    private final double radius;
    private Vector velocity;
    private boolean isThrusting = false;
    private boolean isPlayer;

    public Particle(double centerX, double centerY, double radius, double angle, boolean isPlayer) {
        super(centerX + (4.0 / 3) * radius * Math.cos(Math.toRadians(angle)), centerY - (4.0 / 3) * radius * Math.sin(Math.toRadians(angle)),   // Nose of the ship
                centerX - radius * ((2.0 / 3) * Math.cos(Math.toRadians(angle)) + Math.sin(Math.toRadians(angle))),   // Rear left X
                centerY + radius * ((2.0 / 3) * Math.sin(Math.toRadians(angle)) - Math.cos(Math.toRadians(angle))),   // Rear left Y
                centerX - radius * ((2.0 / 3) * Math.cos(Math.toRadians(angle)) - Math.sin(Math.toRadians(angle))),   // Rear right X
                centerY + radius * ((2.0 / 3) * Math.sin(Math.toRadians(angle)) + Math.cos(Math.toRadians(angle)))    // Rear right Y
        );
        this.centerX = centerX;
        this.centerY = centerY;
        this.angle = angle;
        this.rotation = 0;
        this.radius = radius;
        this.velocity = new Vector(0, 0, angle);
        this.isPlayer = isPlayer;
    }

    public Particle(double centerX, double centerY, double radius, double rotation, double velocity, double angle, boolean isPlayer) {
        super(centerX + Math.cos(Math.toRadians(angle)) * radius, centerY + Math.sin(Math.toRadians(angle)) * radius
                , centerX + Math.cos( Math.toRadians(angle)) * radius, centerY - Math.sin(Math.toRadians(angle)) * radius,
                centerX - Math.cos(Math.toRadians(angle)) * radius, centerY - Math.sin(Math.toRadians(angle)) * radius,
                centerX - Math.cos(Math.toRadians(angle)) * radius, centerY + Math.sin(Math.toRadians(angle)) * radius
        );
        this.centerX = centerX;
        this.centerY = centerY;
        this.angle = angle;
        this.velocity = new Vector(velocity, angle);
        this.radius = radius;
        this.rotation = rotation;
        this.isPlayer = isPlayer;
    }

    public double getCenterX() {    // average of all X coordinates
        double sum = 0;
        for (int i = 0; i < getPoints().size(); i += 2) {
            sum += getPoints().get(i);
        }
        return sum / ((double) getPoints().size() / 2);
    }

    public double getCenterY() {    // average of all Y coordinates
        double sum = 0;
        for (int i = 1; i < getPoints().size(); i += 2) {
            sum += getPoints().get(i);
        }
        return sum / ((double) getPoints().size() / 2);
    }

    public void updatePosition(double FPS, double WINDOW_WIDTH, double WINDOW_HEIGHT) {
        updateAngle();   // Apply the rotation to the "angle" variable
        rotate(-rotation);   // Rotate the ship
        if (isThrusting) {
            velocity.setX(velocity.getX() + THRUST * Math.cos(Math.toRadians(angle)) / FPS);  // Update X component of velocity
            velocity.setY(velocity.getY() - THRUST * Math.sin(Math.toRadians(angle)) / FPS);  // Update Y component of velocity
        } else if (isPlayer) {
            velocity.setX(velocity.getX() - FRICTION * velocity.getX() / FPS);    // Apply friction to X component of velocity
            velocity.setY(velocity.getY() - FRICTION * velocity.getY() / FPS);    // Apply friction to Y component of velocity
        }
        for (int i = 0; i < getPoints().size(); i += 2) {
            getPoints().set(i, getPoints().get(i) + velocity.getX()); // Apply velocity to the X coordinate of the vertex
            getPoints().set(i + 1, getPoints().get(i + 1) + velocity.getY()); // Apply velocity to the Y coordinate of the vertex
        }

        centerX = getCenterX();
        centerY = getCenterY();

        edgeDetection(WINDOW_WIDTH, WINDOW_HEIGHT);

//        if (getCenterX() < 0 - radius) {
//            int indexOfMin = 0;
//            for (int i = 2; i < getPoints().size(); i += 2) {
//                if (getPoints().get(i) < getPoints().get(indexOfMin)) indexOfMin = i;
//            }
//            double xOfMin = getPoints().get(indexOfMin);
//            getPoints().set(indexOfMin, WINDOW_WIDTH);
//            for (int i = 0; i < getPoints().size(); i += 2) {
//                if (i != indexOfMin) getPoints().set(i, WINDOW_WIDTH + (Math.abs(xOfMin - getPoints().get(i))));
//            }
//            centerX = getCenterX();
//        } else if (centerX > WINDOW_WIDTH + radius) {
//            centerX = 0 - radius;
//            for (int i = 0; i < getPoints().size(); i += 2) {
//                getPoints().set(i, getPoints().get(i) - WINDOW_WIDTH);
//            }
//        }
//        if (centerY < 0 - radius) {
//            centerY = WINDOW_HEIGHT + radius;
//            int indexOfMin = 1;
//            for (int i = 3; i < getPoints().size(); i += 2) {
//                if (getPoints().get(i) < getPoints().get(indexOfMin)) indexOfMin = i;
//            }
//            double yOfMin = getPoints().get(indexOfMin);
//            getPoints().set(indexOfMin, WINDOW_HEIGHT);
//            for (int i = 1; i < getPoints().size(); i += 2) {
//                if (i != indexOfMin) getPoints().set(i, WINDOW_HEIGHT + (Math.abs(yOfMin - getPoints().get(i))));
//            }
//        } else if (centerY > WINDOW_HEIGHT + radius) {
//            centerY = 0 - radius;
//            for (int i = 1; i < getPoints().size(); i += 2) {
//                getPoints().set(i, getPoints().get(i) - WINDOW_HEIGHT);
//            }
//        }
    }

    private void edgeDetection(double WINDOW_WIDTH, double WINDOW_HEIGHT) {
        int offLeft = 0, offRight = 0, offTop = 0, offBottom = 0;   // Helper variables to determine if the particle is fully of the screen
        int indexOfXMin = 0, indexOfYMin = 1;   // indexOfMin - index of smallest X coordinate; indexOfYMin - index of smallest Y coordinate
        int indexOfXMax = 0, indexOfYMax = 1;   // indexOfMax - index of biggest X coordinate; indexOfYMax - index of biggest Y coordinate

        for (int i = 0; i < getPoints().size(); i += 2) {
            double x = getPoints().get(i);  // x of the current point
            double y = getPoints().get(i + 1);  // y of the current point

            // check if the point is any way off the screen
            if (getPoints().get(i) < 0) offLeft++;
            else if (getPoints().get(i) > WINDOW_WIDTH) offRight++;

            if (getPoints().get(i + 1) < 0) offTop++;
            else if (getPoints().get(i + 1) > WINDOW_HEIGHT) offBottom++;

            // check if the current point is a maximum or minimum
            indexOfXMin = (x < getPoints().get(indexOfXMin)) ? i : indexOfXMin;
            indexOfYMin = (y < getPoints().get(indexOfYMin)) ? i : indexOfYMin;
            indexOfXMax = (x > getPoints().get(indexOfXMax)) ? i : indexOfXMax;
            indexOfYMax = (y > getPoints().get(indexOfYMax)) ? i : indexOfYMax;
        }

        if (offLeft == getPoints().size() / 2) updateOffScreenPoints(indexOfXMin, WINDOW_WIDTH, false, 0);  // every point is to the left of screen
        else if (offRight == getPoints().size() / 2) updateOffScreenPoints(indexOfXMax, WINDOW_WIDTH, true, 0); // every point is to the right of the screen

        if (offTop == getPoints().size() / 2) updateOffScreenPoints(indexOfYMin, WINDOW_HEIGHT, false, 1);  // every point is over the screen
        if (offBottom == getPoints().size() / 2) updateOffScreenPoints(indexOfYMax, WINDOW_HEIGHT, true, 1);    // every point is below the screen
    }

    private void updateOffScreenPoints(int index, double value, boolean positive, int startIndex) { // positive ? the particle is to the right or below the screen
        double tempValue = getPoints().get(index);
        getPoints().set(index, positive ? 0 : value);

        for (int i = startIndex; i < getPoints().size(); i += 2) {  // calculate the new coordinates for every X or Y point
            if (i != index) {
                double newCoordinate = positive ? -(tempValue - getPoints().get(i)) : value + (Math.abs(tempValue - getPoints().get(i)));   // calculate the new coordinate
                getPoints().set(i, newCoordinate);
            }
        }
    }


    private void edgeDetection1(double WINDOW_WIDTH, double WINDOW_HEIGHT) {  // ten jezeli chcemy pelnego wyjscia poza ekran
        int offLeft = 0, offRight = 0, offTop = 0, offBottom = 0;   // Helper variables to determine if the particle is fully of the screen
        int indexOfXMin = 0, indexOfYMin = 1;   // indexOfMin - index of smallest X coordinate; indexOfYMin - index of smallest Y coordinate
        int indexOfXMax = 0, indexOfYMax = 1;   // indexOfMax - index of biggest X coordinate; indexOfYMax - index of biggest Y coordinate
        for (int i = 0; i < getPoints().size(); i += 2) { // calculate how many points of the screen and indexes
            if (getPoints().get(i) < 0) offLeft++;
            else if (getPoints().get(i) > WINDOW_WIDTH) offRight++;

            if (getPoints().get(i + 1) < 0) offTop++;
            else if (getPoints().get(i + 1) > WINDOW_HEIGHT) offBottom++;

            if (getPoints().get(i) < getPoints().get(indexOfXMin)) indexOfXMin = i;
            else if (getPoints().get(i) > getPoints().get(indexOfXMax)) indexOfXMax = i;

            if (getPoints().get(i + 1) < getPoints().get(indexOfYMin)) indexOfYMin = i + 1;
            else if (getPoints().get(i + 1) > getPoints().get(indexOfYMax)) indexOfYMax = i + 1;
        }
        if (offLeft == getPoints().size() / 2) {   // all points to the left of the screen
            double xOfMin = getPoints().get(indexOfXMin);
            getPoints().set(indexOfXMin, WINDOW_WIDTH);
            for (int i = 0; i < getPoints().size(); i += 2) {
                if (i != indexOfXMin) getPoints().set(i, WINDOW_WIDTH + (Math.abs(xOfMin - getPoints().get(i))));
            }
            centerX = getCenterX();
            centerY = getCenterY();
        } else if (offRight == getPoints().size() / 2) {  // all points to the right of the screen
            double xOfMax = getPoints().get(indexOfXMax);
            getPoints().set(indexOfXMax, 0.0);
            for (int i = 0; i < getPoints().size(); i += 2) {
                if (i != indexOfXMax) getPoints().set(i, -(Math.abs(xOfMax - getPoints().get(i))));
            }
            centerX = getCenterX();
        }
        if (offTop == getPoints().size() / 2) {    // all points to the top of the screen
            double yOfMin = getPoints().get(indexOfYMin);
            getPoints().set(indexOfYMin, WINDOW_HEIGHT);
            for (int i = 1; i < getPoints().size(); i += 2) {
                if (i != indexOfYMin) getPoints().set(i, WINDOW_HEIGHT + (Math.abs(yOfMin - getPoints().get(i))));
            }
            centerX = getCenterX();
            centerY = getCenterY();
        } else if (offBottom == getPoints().size() / 2) { // all points to the bottom of the screen
            double yOfMax = getPoints().get(indexOfYMax);
            getPoints().set(indexOfYMax, 0.0);
            for (int i = 1; i < getPoints().size(); i += 2) {
                if (i != indexOfYMax) getPoints().set(i, -(Math.abs(yOfMax - getPoints().get(i))));
            }
            centerX = getCenterX();
            centerY = getCenterY();
        }
    }

    private void rotate(double angle) {
        double centerX1 = getCenterX();
        double centerY1 = getCenterY();

        // Convert angle to radians
        double radianAngle = Math.toRadians(angle);

        // Apply rotation to each point
        for (int i = 0; i < getPoints().size(); i += 2) {
            double x = getPoints().get(i);
            double y = getPoints().get(i + 1);

            // Perform rotation
            double rotatedX = centerX1 + (x - centerX1) * Math.cos(radianAngle) - (y - centerY1) * Math.sin(radianAngle);
            double rotatedY = centerY1 + (x - centerX1) * Math.sin(radianAngle) + (y - centerY1) * Math.cos(radianAngle);

            // Update the coordinates
            getPoints().set(i, rotatedX);
            getPoints().set(i + 1, rotatedY);
        }
    }

    public void accelerate() {
        isThrusting = true;
    }

    public void stopAcceleration() {
        isThrusting = false;
    }

    public void setRotationRight(double FPS) {
        rotation = -ROTATION_SPEED / FPS;
    }

    public void setRotationLeft(double FPS) {
        rotation = ROTATION_SPEED / FPS;
    }

    public void stopRotation() {
        rotation = 0;
    }

    private void updateAngle() {
        angle += rotation;
    }
}