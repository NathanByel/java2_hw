package com.java2.lesson_1;

import java.util.Random;

public class NameGenerator {
    private String[] names = ("Александр,Алексей,Андрей,Арсений,Артем,Владимир,Владислав,Георгий,Глеб,Даниил,Денис," +
            "Дмитрий,Егор,Иван,Илья,Кирилл,Константин,Максим,Марк,Матвей,Михаил,Никита,Николай,Павел,Роман,Сергей," +
            "Степан,Тимофей,Федор,Ярослав").split(",");

    private Random rand = new Random();

    public String getRandomName() {
        return names[rand.nextInt(names.length)];
    }
}
