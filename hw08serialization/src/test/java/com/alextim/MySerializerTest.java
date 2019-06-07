package com.alextim;

import com.google.gson.Gson;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class MySerializerTest {

    @Test
    public void test() throws IllegalAccessException {
        Person ivan = new Person("Ivan", 23, new Address("Spb", "Mira"));

        String json = MySerializer.toJson(ivan);
        System.out.println(json);

        Person ivanFromJson = new Gson().fromJson(json, Person.class);

        Assertions.assertEquals(ivanFromJson, ivan);
    }


    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Person {
        private final String name;
        private final int age;
        private final Address address;
        private final int[] array = new int[] {1, 2, 3};
        private final Rating[] ratings = new Rating[] {new Rating(1),new Rating(2), new Rating(3)};
        private List<Integer> list = Arrays.asList(1,2,3,4,5);
        private List<String> strings = Arrays.asList("1", "2", "3");
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Address {
        private final String city;
        private final String street;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Rating {
        private final int rating;
    }
}
