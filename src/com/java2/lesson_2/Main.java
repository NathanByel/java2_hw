package com.java2.lesson_2;
/*
    1. Напишите метод, на вход которого подается двумерный строковый массив размером 4х4,
       при подаче массива другого размера необходимо бросить исключение MyArraySizeException.

    2. Далее метод должен пройтись по всем элементам массива, преобразовать в int, и просуммировать.
       Если в каком-то элементе массива преобразование не удалось (например, в ячейке лежит символ или текст
       вместо числа), должно быть брошено исключение MyArrayDataException – с детализацией, в какой именно ячейке
       лежат неверные данные.

    3. В методе main() вызвать полученный метод, обработать возможные исключения MySizeArrayException и
       MyArrayDataException и вывести результат расчета.
*/

public class Main {

    private static int convertAndSum(String[][] arr) throws MyArraySizeException, MyArrayDataException {
        int sum = 0;

        if(arr.length != 4) {
            throw new MyArraySizeException();
        }

        for(int i = 0; i < arr.length; i++) {
            if (arr[i].length != 4) {
                throw new MyArraySizeException();
            }

            for (int j = 0; j < arr[i].length; j++) {
                try {
                    sum += Integer.parseInt(arr[i][j]);
                } catch (NumberFormatException e) {
                    throw new MyArrayDataException(i, j, e);
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        // Good
        String[][] array1 = {
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"}
        };

        try {
            System.out.println( "Try \"array1\"" );
            System.out.println( "Result: " + convertAndSum(array1) );
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println();

        // Wrong data
        String[][] array2 = {
                {"1", "2", "3", "4"},
                {"1", "2", "A", "4"},
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"}
        };

        try {
            System.out.println( "Try \"array2\"" );
            System.out.println( "Result: " + convertAndSum(array2) );
        } catch (Exception e) {
            System.out.println( e.toString() );
        }
        System.out.println();

        // Wrong size
        String[][] array3 = {
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"}
        };

        try {
            System.out.println( "Try \"array3\"" );
            System.out.println( "Result: " + convertAndSum(array3) );
        } catch (Exception e) {
            System.out.println( e.toString() );
        }
        System.out.println();

        // Wrong size
        String[][] array4 = {
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4"},
                {"1", "2", "3", "4", "5"}
        };

        try {
            System.out.println( "Try \"array4\"" );
            System.out.println( "Result: " + convertAndSum(array4) );
        } catch (Exception e) {
            System.out.println( e.toString() );
        }
    }
}
