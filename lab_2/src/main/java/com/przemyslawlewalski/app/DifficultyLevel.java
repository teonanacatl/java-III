package com.przemyslawlewalski.app;

public class DifficultyLevel {
    private String name;
    private int maxNumber;

    public DifficultyLevel(String name, int maxNumber) {
        this.name = name;
        this.maxNumber = maxNumber;
    }

    public String getName() {
        return name;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    @Override
    public String toString() {
        return String.valueOf(maxNumber);
    }
}
