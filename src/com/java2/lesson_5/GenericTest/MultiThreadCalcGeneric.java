package com.java2.lesson_5.GenericTest;

import java.util.function.BiFunction;

public class MultiThreadCalcGeneric<T> {
    private Thread[] threads;

    public MultiThreadCalcGeneric(T[] inArray, T[] outArray, int numThreads, BiFunction<Integer, T, T> calcFunction) {
        threads = new Thread[numThreads];

        int sizePerThread = inArray.length / numThreads;
        int sizeForLastThread = inArray.length - sizePerThread * (numThreads - 1);

        int pieceOffset = 0;
        for (int i = 0; i < numThreads; i++) {
            int pieceSize = (i == (numThreads-1)) ? sizeForLastThread : sizePerThread;
            int fPieceOffset = pieceOffset;

            threads[i] = new Thread(() -> {
                int maxIndex = fPieceOffset + pieceSize;
                for (int index = fPieceOffset; index < maxIndex; index++) {
                    outArray[index] = calcFunction.apply(index, inArray[index]);
                }
            });
            pieceOffset += pieceSize;
        }
    }

    public MultiThreadCalcGeneric(float[] inArray, float[] outArray, int numThreads, MTCalcFunc calcFunction) {
        threads = new Thread[numThreads];

        int sizePerThread = inArray.length / numThreads;
        int sizeForLastThread = inArray.length - sizePerThread * (numThreads - 1);

        int pieceOffset = 0;
        for (int i = 0; i < numThreads; i++) {
            int pieceSize = (i == (numThreads-1)) ? sizeForLastThread : sizePerThread;
            int fPieceOffset = pieceOffset;

            threads[i] = new Thread(() -> {
                int maxIndex = fPieceOffset + pieceSize;
                for (int index = fPieceOffset; index < maxIndex; index++) {
                    outArray[index] = calcFunction.apply(index, inArray[index]);
                }
            });
            pieceOffset += pieceSize;
        }
    }

    public MultiThreadCalcGeneric(T[] array, int numThreads, BiFunction<Integer, T, T> calcFunction) {
        this(array, array, numThreads, calcFunction);
    }

    public MultiThreadCalcGeneric(float[] array, int numThreads, MTCalcFunc calcFunction) {
        this(array, array, numThreads, calcFunction);
    }

    public void startCalcAndWait() throws InterruptedException {
        for (Thread th: threads) th.start();
        for (Thread th: threads) th.join();
    }
}
