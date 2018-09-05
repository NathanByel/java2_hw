package com.java2.lesson_5;

import java.util.Arrays;

/*
1. Необходимо написать два метода, которые делают следующее:
    1) Создают одномерный длинный массив, например:
        static final int size = 10000000;
        static final int h = size / 2;
        float[] arr = new float[size];
    2) Заполняют этот массив единицами;
    3) Засекают время выполнения: long a = System.currentTimeMillis();
    4) Проходят по всему массиву и для каждой ячейки считают новое значение по формуле:
        arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
    5) Проверяется время окончания метода System.currentTimeMillis();
    6) В консоль выводится время работы: System.out.println(System.currentTimeMillis() - a);

    Отличие первого метода от второго:
    Первый просто бежит по массиву и вычисляет значения.
    Второй разбивает массив на два массива, в двух потоках высчитывает новые значения и потом
    склеивает эти массивы обратно в один.

    Пример деления одного массива на два:
    System.arraycopy(arr, 0, a1, 0, h);
    System.arraycopy(arr, h, a2, 0, h);

    Пример обратной склейки:
    System.arraycopy(a1, 0, arr, 0, h);
    System.arraycopy(a2, 0, arr, h, h);

    Примечание:
    System.arraycopy() копирует данные из одного массива в другой:
    System.arraycopy(массив-источник, откуда начинаем брать данные из массива-источника, массив-назначение,
    откуда начинаем записывать данные в массив-назначение, сколько ячеек копируем)
    По замерам времени:
    Для первого метода надо считать время только на цикл расчета:
    for (int i = 0; i < size; i++) {
        arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
    }
    Для второго метода замеряете время разбивки массива на 2, просчета каждого из двух массивов и склейки.
*/
public class Main {
    private static final int size = 10000000;
    private static float[] arr1;
    private static float[] arr2;
    private static int nrOfProcessors;

    public static void main(String[] args) throws InterruptedException {
        /*
            Расчет ведется на всех доступных процессорах/ядрах.
         */
        nrOfProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Доступно процессоров/ядер: " + nrOfProcessors);

        arr1 = new float[size];
        arr2 = new float[size];
        Arrays.fill(arr1, 1f);
        Arrays.fill(arr2, 1f);

        calcWithoutThreads();
        calcWithThreads();

        System.out.println( "Массивы " + (validateArrays(arr1, arr2) ? "": "НЕ") + "равны.");
    }

    private static void calcWithoutThreads() {
        System.out.print("Считаем в одном потоке, в цикле... ");
        long time = System.currentTimeMillis();
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = (float) (arr1[i] * Math.sin(0.2f + i / 5f) * Math.cos(0.2f + i / 5f) * Math.cos(0.4f + i / 2f));
        }
        System.out.println("Время: " + (System.currentTimeMillis() - time));
    }

    private static void calcWithThreads() throws InterruptedException {
        System.out.print("Считаем в " + nrOfProcessors + " потоках... ");
        long time = System.currentTimeMillis();
        MultiThreadCalc mTCalc = new MultiThreadCalc(arr2, nrOfProcessors);
        mTCalc.startCalcAndWait();
        System.out.println("Время: " + (System.currentTimeMillis() - time));
    }

    private static boolean validateArrays(float[] arr1, float[] arr2) {
        if( (arr1 == null) || (arr2 == null) || (arr1.length != arr2.length) || (arr1.length == 0) ) {
            return false;
        }

        boolean equal = true;
        for (int i = 0; i < arr1.length; i++) {
            if(arr1[i] != arr2[i]) {
                System.out.printf("Index: %d. arr1(%f) != arr2(%f)\n\r", i, arr1[i], arr2[i]);
                equal = false;
            }
        }
        return equal;
    }
}
