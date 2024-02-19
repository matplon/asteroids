package com.example.MotorolaScienceCup.Battlezone;

public class Vertex {
    private double x;
    private double y;
    private double z;

    private double w;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;

    }

    public Vertex(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

    }

    public double[] toArray(){
        double[] array1 = new double[4];
        array1[0] = this.getX();
        array1[1] = this.getY();
        array1[2] = this.getZ();
        array1[3] = this.getW();
        return array1;
    }

    public Vertex getVertDif(Vertex vertex){
        double x = this.getX() - vertex.getX();
        double y = this.getY() - vertex.getY();
        double z = this.getZ() - vertex.getZ();
        return new Vertex(x,y,z);
    }

    public Vertex getVertSum(Vertex vertex){
        double x = this.getX() + vertex.getX();
        double y = this.getY() + vertex.getY();
        double z = this.getZ() + vertex.getZ();
        return new Vertex(x,y,z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getW(){
        return w;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }
}
