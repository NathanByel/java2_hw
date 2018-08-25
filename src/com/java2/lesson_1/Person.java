package com.java2.lesson_1;

public class Person {
    private String name;
    private int age;
    private int maxBarrierHeight;
    private int totalBarriers;
    private int successBarriers;
    private int brokenBarriers;

    public Person(String name, int age, int maxBarrierHeight) {
        this.name = name;
        this.age  = age;
        this.maxBarrierHeight = maxBarrierHeight;
        newRun();
    }

    public void newRun() {
        totalBarriers = 0;
        successBarriers = 0;
        brokenBarriers = 0;
    }

    public boolean jump(int height) {
        totalBarriers++;
        if (height <= maxBarrierHeight) {
            successBarriers++;
            return true;
        } else {
            brokenBarriers++;
            return false;
        }
    }

    public void showInfo() {
        System.out.println( "Спортсмен\n\r" +
                            "Имя             " + name             + "\n\r" +
                            "Возраст         " + age              + "\n\r" +
                            "Высота прыжка   " + maxBarrierHeight + "\n\r" +
                            "Всего преодолел " + totalBarriers    + "\n\r" +
                            "Успешно         " + successBarriers  + "\n\r" +
                            "Сломал          " + brokenBarriers );
    }
}
