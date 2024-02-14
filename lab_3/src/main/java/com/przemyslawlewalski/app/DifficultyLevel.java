package com.przemyslawlewalski.app;

public class DifficultyLevel {
    private final String name;
    private final int startValue;
    private final int endValue;

    public DifficultyLevel(String name, int startValue, int endValue) {
        this.name = name;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public String getName() {
        return name;
    }

    public int getStartValue() {
        return startValue;
    }

    public int getEndValue() {
        return endValue;
    }

    @Override
    public String toString() {
        return String.valueOf(endValue);
    }
}