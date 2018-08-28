package com.java2.lesson_3;

import java.util.*;

/**
 * Записи хранятся в TreeMap коллекции.
 * Ключ - фамилия.
 * Данные - HashSet коллекция, так как у одной фамилии может быть несколько номеров.
 */
public class PhoneBook {
    private Map<String, HashSet<String>> peoples = new TreeMap<>();

    public void add(String surname, String phoneNumber) {
        if( (surname == null) || (phoneNumber == null) ) {
            throw new IllegalArgumentException("Surname or Phone number can't be null");
        }
        if( (surname.length() == 0) || (phoneNumber.length() == 0) ) {
            throw new IllegalArgumentException("Surname or Phone number can't be empty");
        }
        HashSet<String> numbersList;      // Почему Idea подчеркивает numbersList везде?
        numbersList = peoples.get(surname);
        if (numbersList == null) {
            numbersList = new HashSet<>();
            numbersList.add(phoneNumber);
            peoples.put(surname, numbersList);
        } else {
            numbersList.add(phoneNumber);
        }
    }

    /**
     * Возвращает строковый массив номеров для указанной фамилии.
     * Или null если фамилия не найдена.
     */
    public String[] get(String surname) {
        if(surname == null) {
            return null;
        }
        HashSet<String> res = peoples.get(surname);
        return (res == null) ? null : res.toArray(new String[0]);
    }

    /**
     * Печатает все найденные номера по указанной фамилии,
     * или, что фамилия не найдена.
     */
    public void printInfo(String surname) {
        String[] numbers = get(surname);
        if (numbers != null) {
            for (String number : numbers) {
                System.out.printf("%s: %s\r\n", surname, number);
            }
        } else {
            System.out.printf("%s: не найдено\r\n", surname);
        }
        System.out.println();
    }
}
