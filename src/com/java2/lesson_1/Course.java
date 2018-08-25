package com.java2.lesson_1;

import java.util.Arrays;

public class Course {
    private static final int MIN_BARRIER_HEIGHT = 10;
    private static final int MAX_BARRIER_HEIGHT = 30;
    private static final int MIN_NO_BARRIER_LEN = 2;    // Минимальное расстояние между препятствиями
    private static final int MAX_NO_BARRIER_LEN = 5;    // Максимальное расстояние между препятствиями
    private int[] barriers;

    private int getRand(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public Course(int length) {
        barriers = new int[length];
        Arrays.fill(barriers, 0);

        int len = getRand(MIN_NO_BARRIER_LEN, MAX_NO_BARRIER_LEN);
        for (int i = 0; i < barriers.length; i++) {
            if(i > len) {
                barriers[i] = getRand(MIN_BARRIER_HEIGHT, MAX_BARRIER_HEIGHT);
                len = i + getRand(MIN_NO_BARRIER_LEN, MAX_NO_BARRIER_LEN);
            }
        }
    }

    public void printInfo() {
        System.out.println("Полоса препятствий:\n\r" + Arrays.toString(barriers) + "\n\r");
    }

    public void doIt(Team team) {
        for (int i = 0; i < barriers.length; i++) {
            team.jump(barriers[i]);
        }
    }
}
