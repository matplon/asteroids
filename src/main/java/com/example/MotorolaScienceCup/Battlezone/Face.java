package com.example.MotorolaScienceCup.Battlezone;

import java.util.ArrayList;

public class Face {

    private ArrayList<Integer> indexes;

    public Face(ArrayList<Integer> indexes) {
        this.indexes = indexes;

    }

    public ArrayList<Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(ArrayList<Integer> indexes) {
        this.indexes = indexes;
    }

    @Override
    public String toString() {
        return "Face{" +
                "indexes=" + indexes +
                '}';
    }
}
