package com.example.asteroids;

import javafx.scene.shape.Polygon;

public class Player extends Polygon {
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

    public Player(double centerX, double centerY, double radius, double angle){
        super(centerX+(4.0/3)*radius*Math.cos(Math.toRadians(angle)), centerY-(4.0/3)*radius*Math.sin(Math.toRadians(angle)),   // Nose of the ship
                centerX-radius*((2.0/3)*Math.cos(Math.toRadians(angle))+Math.sin(Math.toRadians(angle))),   // Rear left X
                centerY+radius*((2.0/3)*Math.sin(Math.toRadians(angle))-Math.cos(Math.toRadians(angle))),   // Rear left Y
                centerX-radius*((2.0/3)*Math.cos(Math.toRadians(angle))-Math.sin(Math.toRadians(angle))),   // Rear right X
                centerY+radius*((2.0/3)*Math.sin(Math.toRadians(angle))+Math.cos(Math.toRadians(angle)))    // Rear right Y
        );
        this.centerX = centerX;
        this.centerY = centerY;
        this.angle = angle;
        this.rotation = 0;
        this.radius = radius;
        this.velocity = new Vector(0, 0, angle);
    }

    public double getCenterX(){
        double x1 = getPoints().get(0);
        double x2 = getPoints().get(2);
        double x3 = getPoints().get(4);
        return (x1+x2+x3)/3;
    }

    public double getCenterY(){
        double y1 = getPoints().get(1);
        double y2 = getPoints().get(3);
        double y3 = getPoints().get(5);
        return (y1+y2+y3)/3;
    }

    public void updatePosition(double FPS){
        if(isThrusting){
            velocity.setX(velocity.getX()+THRUST*Math.cos(Math.toRadians(angle))/FPS);  // Update X component of velocity
            velocity.setY(velocity.getY()-THRUST*Math.sin(Math.toRadians(angle))/FPS);  // Update Y component of velocity
        }
        else{
            velocity.setX(velocity.getX()-FRICTION*velocity.getX()/FPS);    // Apply friction to X component of velocity
            velocity.setY(velocity.getY()-FRICTION*velocity.getY()/FPS);    // Apply friction to Y component of velocity
        }
        setTranslateX(getTranslateX()+velocity.getX()); // Update the vertices' X coordinates
        setTranslateY(getTranslateY()+velocity.getY()); // Update the vertices' Y coordinates
        centerX = getCenterX();
        centerY = getCenterY();
        rotate();   // Apply rotation to the ship
    }

    public void accelerate(){
        isThrusting = true;
    }

    public void stopAcceleration(){
        isThrusting = false;
    }

    public void setRotationRight(double FPS){
        rotation = -ROTATION_SPEED/FPS;
    }

    public void setRotationLeft(double FPS){
        rotation = ROTATION_SPEED/FPS;
    }

    public void stopRotation(){
        rotation = 0;
    }

    public void rotate(){
        angle += rotation;
        getPoints().setAll(centerX+(4.0/3)*radius*Math.cos(Math.toRadians(angle)), centerY-(4.0/3)*radius*Math.sin(Math.toRadians(angle)),  // Rotate the nose of the ship
                centerX-radius*((2.0/3)*Math.cos(Math.toRadians(angle))+Math.sin(Math.toRadians(angle))),   // Rotate the rear of the ship
                centerY+radius*((2.0/3)*Math.sin(Math.toRadians(angle))-Math.cos(Math.toRadians(angle))),
                centerX-radius*((2.0/3)*Math.cos(Math.toRadians(angle))-Math.sin(Math.toRadians(angle))),
                centerY+radius*((2.0/3)*Math.sin(Math.toRadians(angle))+Math.cos(Math.toRadians(angle))));
    }
}
