package dev.abbeytech.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Component
@Order(2)
public class MethodReferences implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("=== Tutorial 2: Method References ===\n");

        // Example 1: Static method reference
        Function<String, Integer> parseIntLambda = s -> Integer.parseInt(s);
        Function<String, Integer> parseIntRef = Integer::parseInt;

        System.out.println("Lambda: " + parseIntLambda.apply("123"));
        System.out.println("Method reference: " + parseIntRef.apply("456"));

        // Example 2: Instance method reference of a specific object
        String greeting = "Hello, World!";
        Supplier<Integer> lengthLambda = () -> greeting.length();
        Supplier<Integer> lengthRef = greeting::length;

        System.out.println("Lambda: " + lengthLambda.get());
        System.out.println("Method reference: " + lengthRef.get());

        // Example 3: Instance method reference of an arbitrary object of a particular type
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        // Using lambda
        System.out.println("Printing with lambda:");
        names.forEach(name -> System.out.println(name));

        // Using method reference
        System.out.println("Printing with method reference:");
        names.forEach(System.out::println);

        // Example 4: Constructor reference
        Supplier<StringBuilder> builderLambda = () -> new StringBuilder();
        Supplier<StringBuilder> builderRef = StringBuilder::new;

        System.out.println("Lambda: " + builderLambda.get().append("Created with lambda"));
        System.out.println("Method reference: " + builderRef.get().append("Created with method reference"));

        // Example 5: Constructor with arguments
        Function<String, Person> personLambda = name -> new Person(name);
        Function<String, Person> personRef = Person::new;

        System.out.println("Lambda: " + personLambda.apply("John").getName());
        System.out.println("Method reference: " + personRef.apply("Jane").getName());

        // Example 6: Custom Person Creator with bifunction
        BiFunction<String, Integer, Person> personCreatorLambda = (name, age) -> new Person(name, age);
        BiFunction<String, Integer, Person> personCreatorRef = Person::new;

        Person person1 = personCreatorLambda.apply("John", 30);
        Person person2 = personCreatorRef.apply("Jane", 25);

        System.out.println("Person created with lambda: " + person1);
        System.out.println("Person created with method reference: " + person2);
    }

    // Helper class
    static class Person {
        private final String name;
        private final int age;

        public Person(String name) {
            this(name, 0);
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }
}