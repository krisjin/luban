package org.luban.func;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 *
 */
public class Java8Func {
    public static void main(String[] args) {
        test1();
    }


    public static void test1() {

//        IntStream.range(0, 10).forEach(i -> {
//            if (i % 2 == 1) System.out.println(i);
//        });
//
//        IntStream.range(0, 10).filter(i -> i % 2 == 1).forEach(System.out::println);

        OptionalInt reduced1 = IntStream.range(0, 10).reduce((a, b) -> a + b);
        System.out.println(reduced1.getAsInt());
//
//        int reduced2 = IntStream.range(0, 10).reduce(7, (a, b) -> a + b);
//        System.out.println(reduced2);
    }


    static class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static void test6(List<Person> persons) {
        Integer ageSum = persons.parallelStream().reduce(0, (sum, p) -> {
            System.out.format("accumulator: sum=%s; person=%s; thread=%s\n", sum, p, Thread.currentThread().getName());
            return sum += p.age;
        }, (sum1, sum2) -> {
            System.out.format("combiner: sum1=%s; sum2=%s; thread=%s\n", sum1, sum2, Thread.currentThread().getName());
            return sum1 + sum2;
        });

        System.out.println(ageSum);
    }

    private static void test3(List<String> strings) {
        strings.parallelStream().filter(s -> {
            System.out.format("filter:  %s [%s]\n", s, Thread.currentThread().getName());
            return true;
        }).map(s -> {
            System.out.format("map:     %s [%s]\n", s, Thread.currentThread().getName());
            return s.toUpperCase();
        }).sorted((s1, s2) -> {
            System.out.format("sort:    %s <> %s [%s]\n", s1, s2, Thread.currentThread().getName());
            return s1.compareTo(s2);
        }).forEach(s -> System.out.format("forEach: %s [%s]\n", s, Thread.currentThread().getName()));
    }

    private static void test2(List<Person> persons) {
        Person result = persons.stream().reduce(new Person("", 0), (p1, p2) -> {
            p1.age += p2.age;
            p1.name += p2.name;
            return p1;
        });

        System.out.format("name=%s; age=%s", result.name, result.age);
    }


    private static void test4(List<Person> persons) {
        Integer ageSum = persons.stream().reduce(0, (sum, p) -> {
            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
            return sum += p.age;
        }, (sum1, sum2) -> {
            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
            return sum1 + sum2;
        });

        System.out.println(ageSum);
    }
}
