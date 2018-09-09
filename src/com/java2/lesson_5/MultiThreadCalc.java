package com.java2.lesson_5;

/*
    Разделяет массив на равные части и создает столько же потоков для просчета.
    В последней части размещается остаток, если нельзя разделить на равные.
    Просчет ведется в том же массиве, со смещением для каждого потока.
 */
public class MultiThreadCalc {
    private Thread[] threads;

    public MultiThreadCalc(float[] array, int numThreads) {
        threads = new Thread[numThreads];

        int dataSizePerThread = array.length / numThreads;
        int dataSizeForLastThread = array.length - dataSizePerThread * (numThreads - 1);

        int dataOffset = 0;
        for (int i = 0; i < numThreads; i++) {
            int dataSize = (i == (numThreads-1)) ? dataSizeForLastThread : dataSizePerThread;
            int fDataOffset = dataOffset;

            threads[i] = new Thread(() -> {
                int maxIndex = fDataOffset + dataSize;
                for (int index = fDataOffset; index < maxIndex; index++) {
                    array[index] = (float) ( array[index] * Math.sin(0.2f + index / 5f) *
                                                            Math.cos(0.2f + index / 5f) *
                                                            Math.cos(0.4f + index / 2f) );
                }
            });
            dataOffset += dataSize;
        }
    }

    public void startCalcAndWait() throws InterruptedException {
        for (Thread th: threads) th.start();
        for (Thread th: threads) th.join();
    }
}
