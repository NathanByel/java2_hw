package com.java2.lesson_5.GenericTest;

import java.util.Arrays;

public class Main {
    private static final int size = 10000000;
    private static Float[] arr1;
    private static float[] arr2;

    public static void main(String[] args) throws InterruptedException {
        arr1 = new Float[size];
        arr2 = new float[size];
        Arrays.fill(arr1, 1f);
        Arrays.fill(arr2, 1f);

        System.out.print("Считаем с дженериком... ");
        long time = System.currentTimeMillis();
        MultiThreadCalcGeneric<Float> ss = new MultiThreadCalcGeneric<>(arr1, 4, (index, oldVal) -> {
            return (float) (oldVal * Math.sin(0.2f + index / 5f) * Math.cos(0.2f + index / 5f) * Math.cos(0.4f + index / 2f));
        });
        ss.startCalcAndWait();
        System.out.println("Время: " + (System.currentTimeMillis() - time));

        System.out.print("Считаем с примитивами... ");
        time = System.currentTimeMillis();
        MultiThreadCalcGeneric dd = new MultiThreadCalcGeneric(arr2, 4, (index, oldVal) -> {
            return (float) (oldVal * Math.sin(0.2f + index / 5f) * Math.cos(0.2f + index / 5f) * Math.cos(0.4f + index / 2f));
        });
        dd.startCalcAndWait();
        System.out.println("Время: " + (System.currentTimeMillis() - time));
    }
}
