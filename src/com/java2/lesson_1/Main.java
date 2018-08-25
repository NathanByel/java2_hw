package com.java2.lesson_1;

/*
    1. Разобраться с имеющимся кодом;
    2. Добавить класс Team, который будет содержать название команды, массив из четырех участников
       (в конструкторе можно сразу указывать всех участников), метод для вывода информации о членах команды,
       прошедших дистанцию, метод вывода информации обо всех членах команды.
    3. Добавить класс Course (полоса препятствий), в котором будут находиться массив препятствий и метод,
       который будет просить команду пройти всю полосу.

    В итоге должно быть что-то вроде:
    public static void main(String[] args) {
        Course c = new Course(...);     // Создаем полосу препятствий
        Team team = new Team(...);      // Создаем команду
        c.doIt(team);                   // Просим команду пройти полосу
        team.showResults();             // Показываем результаты
    }
*/
public class Main {

    private static int getRand(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static void main(String[] args) {
        NameGenerator nameGen = new NameGenerator();

	    Team teamA = new Team("Команда А", new Person[]{
	            new Person( nameGen.getRandomName(), getRand(18, 35), getRand(10, 30) ),
                new Person( nameGen.getRandomName(), getRand(18, 35), getRand(10, 30) ),
                new Person( nameGen.getRandomName(), getRand(18, 35), getRand(10, 30) ),
                new Person( nameGen.getRandomName(), getRand(18, 35), getRand(10, 30) )
	        } );

        Course course = new Course(50);
        course.printInfo();

        course.doIt(teamA);
        teamA.printInfo();
    }
}
