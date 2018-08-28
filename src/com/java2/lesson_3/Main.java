package com.java2.lesson_3;

import java.util.*;

/*
    1. Создать массив с набором слов (10-20 слов, среди которых должны встречаться повторяющиеся).
       Найти и вывести список уникальных слов, из которых состоит массив (дубликаты не считаем).
       Посчитать, сколько раз встречается каждое слово.

    2. Написать простой класс ТелефонныйСправочник, который хранит в себе список фамилий и телефонных номеров.
       В этот телефонный справочник с помощью метода add() можно добавлять записи. С помощью метода get()
       искать номер телефона по фамилии. Следует учесть, что под одной фамилией может быть несколько телефонов
       (в случае однофамильцев), тогда при запросе такой фамилии должны выводиться все телефоны.

    Желательно как можно меньше добавлять своего, чего нет в задании (т.е. не надо в телефонную запись добавлять
    еще дополнительные поля (имя, отчество, адрес), делать взаимодействие с пользователем через консоль и т.д.).
    Консоль желательно не использовать (в том числе Scanner), тестировать просто из метода main(), прописывая add() и get().
*/
public class Main {

    public static void main(String[] args) {
        // Task 1
        System.out.println("Task 1");
        task1();

        // Task 2
        System.out.println("\r\nTask 2");
        task2();
    }

    private static void task1() {
        String[] array = {
                "Александр",
                "Владимир", //
                "Владимир", //
                "Георгий",
                "Даниил",
                "Егор",
                "Иван",
                "Кирилл",
                "Максим",   //
                "Максим",   //
                "Никита",
                "Павел",
                "Роман",
                "Сергей",
                "Тимофей",  //
                "Тимофей",  //
                "Тимофей",  //
                "Федор",
                "Ярослав"
        };
        printArrayInfo(array);
    }

    private static void printArrayInfo(String[] array) {
        if(array == null) {
            return;
        }
        Map<String, Integer> printMap = new TreeMap<>();
        Integer num;
        for (String s : array) {
            // <- Если перенести сюда "Integer num;" то на каждой итерации
            // будет новый объект Integer создаваться или Java машина оптимизирует это?
            num = printMap.get(s);
            num = (num == null) ? 1 : (num + 1);
            printMap.put(s, num);
        }

        for (Map.Entry<String, Integer> item : printMap.entrySet()) {
            System.out.printf("Встречается %d - %s\r\n", item.getValue(), item.getKey());
        }
    }

    private static void task2() {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.add("Иванов", "1111");
        phoneBook.add("Иванов", "1111");
        phoneBook.add("Иванов", "2222");

        phoneBook.add("Петров", "2222");

        phoneBook.add("Сидоров", "5555");
        phoneBook.add("Сидоров", "4444");

        /* Не много не понятно по реализации, номера должны выводится в методе get внутри класса
           или get должен возвращать что-то, а выводиться вне класса.
           По этому сделал 2 варианта, через printInfo и get.

            String[] numbers = phoneBook.get("Иванов");
            if(numbers != null) {
                for (String number : numbers) {
                    System.out.println("Иванов: " + number);
                }
            } else {
                System.out.println("Иванов: не найдено");
            }
        */
        phoneBook.printInfo("Иванов");
        phoneBook.printInfo("Петров");
        phoneBook.printInfo("Сидоров");
        phoneBook.printInfo("Васильев");
    }
}
