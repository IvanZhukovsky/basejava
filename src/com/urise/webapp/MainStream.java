package com.urise.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (acc, x) -> acc * 10 + x);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        int number = integers.stream()
                .reduce(0, (a, b) -> a + b) % 2;

        return integers.stream()
                .filter(integer -> integer % 2 == number)
                .collect(Collectors.toList());

    }

}
