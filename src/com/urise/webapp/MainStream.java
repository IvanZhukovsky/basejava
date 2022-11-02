package com.urise.webapp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MainStream {
    public static void main(String[] args) {

        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        list.add(4);
        list.add(1);
        list.add(5);
        list.add(6);

        for (Integer integer : oddOrEven(list)) {
            System.out.println(integer);
        }


    }

    private static int minValue(int[] values) {
        return IntStream.of(values).distinct().sorted().reduce(0, (acc, x) -> acc * 10 + x);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        return integers.stream().collect(
                () -> {
                    List<Integer> chet = new ArrayList<>();
                    List<Integer> neChet = new ArrayList<>();
                    int summa = 0;
                    for (Integer integer : integers) {
                        if (integer % 2 == 0) {
                            chet.add(integer);
                        } else neChet.add(integer);
                        summa += integer;
                    }
                    if (summa % 2 == 0) {
                        return chet;
                    } else
                        return neChet;
                },
                (list2, integer) -> {

                },
                (list1, list2) -> {

                });
    }
}
