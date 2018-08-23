package com.java2.lesson_1;

public class Team {
    private String name;
    private Person[] persons;

    public Team(String name, Person[] persons ) {
        if(persons.length > 4) {
            throw new IllegalArgumentException("Max 4 person in team");
        } else if(persons.length == 0) {
            throw new IllegalArgumentException("Min 1 person in team");
        }

        this.persons = persons;
        this.name = name;
    }

    public void printInfo() {
        System.out.println("Команда \"" + name + "\" результаты:");
        for (Person person: persons) {
            person.showInfo();
            System.out.println();
        }
    }

    public void jump(int height) {
        if(height > 0) {
            for (Person person : persons) {
                person.jump(height);
            }
        }
    }
}
