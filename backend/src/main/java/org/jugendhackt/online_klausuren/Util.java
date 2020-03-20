package org.jugendhackt.online_klausuren;

import org.jugendhackt.online_klausuren.tasks.Task;

import java.util.Random;

public class Util {

    private static Random random = new Random();

    public static Task[] shuffle(Task[] oArray) {
        Task[] array = oArray.clone();
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
        return array;
    }

    private static void swap(Task[] array, int i1, int i2) {
        Task temp = array[i1];
        array[i1] = array[i2];
        array[i2] = temp;
    }
}
