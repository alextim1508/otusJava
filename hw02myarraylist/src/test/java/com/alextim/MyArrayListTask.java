package com.alextim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

public class MyArrayListTask {

    private static int SIZE = 1_000;
    private static Integer mas[] = new Integer[SIZE];

    @BeforeAll
    public static void setUp() {
        Random random = new Random();
        for(int i=0; i<mas.length; i++)
            mas[i] = random.nextInt();
    }

    @Test
    public void addAllTest() {
        List<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        boolean res = Collections.addAll(list, mas);
        Assertions.assertTrue(res);

        for(int i =3; i<mas.length;i++)
            Assertions.assertEquals(mas[i], list.get(i+3));
    }

    @Test
    public void copyTest() {
        List<Integer> src = new MyArrayList<>();
        src.addAll(Arrays.asList(mas));
        List<Integer> dest = new MyArrayList<>();
        dest.addAll(Arrays.asList(new Integer[SIZE]));

        Collections.copy(dest, src);

        for(int i =0; i<mas.length;i++)
            Assertions.assertEquals(dest.get(i), src.get(i));
    }

    @Test
    public void sortTest() {
        List<Integer> scr = new MyArrayList<>();
        scr.addAll(Arrays.asList(mas));

        Collections.sort(scr, Integer::compareTo);

        for(int i=0; i<mas.length-1; i++)
            Assertions.assertTrue(scr.get(i) <= scr.get(i+1));
    }
}
