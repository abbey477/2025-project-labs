package dev.abbeytech.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@Component
@Order(4)
public class FunctionalComposition implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("=== Tutorial 4: Functional Interfaces and Composition ===\n");

        // Example 1: Function composition with andThen
        Function<Integer, Integer> multiplyByTwo = x -> x * 2;
        Function<Integer, Integer> addTen = x -> x + 10;

        Function<Integer, Integer> multiplyByTwoThenAddTen = multiplyByTwo.andThen(addTen);
        Function<Integer, Integer> addTenThenMultiplyByTwo = multiplyByTwo.compose(addTen);

        System.out.println("multiplyByTwo(5) = " + multiplyByTwo.apply(5));
        System.out.println("addTen(5) = " + addTen.apply(5));
        System.out.println("multiplyByTwoThenAddTen(5) = " + multiplyByTwoThenAddTen.apply(5));
        System.out.println("addTenThenMultiplyByTwo(5) = " + addTenThenMultiplyByTwo.apply(5));

        // Example 2: Predicate composition
        Predicate<Integer> isEven = x -> x % 2 == 0;
        Predicate<Integer> isPositive = x -> x > 0;

        Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);
        Predicate<Integer> isEvenOrPositive = isEven.or(isPositive);
        Predicate<Integer> isNotEven = isEven.negate();

        System.out.println("\nPredicates with number 4:");
        System.out.println("isEven(4) = " + isEven.test(4));
        System.out.println("isPositive(4) = " + isPositive.test(4));
        System.out.println("isEvenAndPositive(4) = " + isEvenAndPositive.test(4));
        System.out.println("isEvenOrPositive(4) = " + isEvenOrPositive.test(4));
        System.out.println("isNotEven(4) = " + isNotEven.test(4));

        System.out.println("\nPredicates with number -3:");
        System.out.println("isEven(-3) = " + isEven.test(-3));
        System.out.println("isPositive(-3) = " + isPositive.test(-3));
        System.out.println("isEvenAndPositive(-3) = " + isEvenAndPositive.test(-3));
        System.out.println("isEvenOrPositive(-3) = " + isEvenOrPositive.test(-3));
        System.out.println("isNotEven(-3) = " + isNotEven.test(-3));

        // Example 3: Complex example - Processing objects through multiple transformations
        List<String> names = Arrays.asList("John", "Alice", "Bob", "Carol", "");

        // Step 1: Filter out empty names
        Predicate<String> isNotEmpty = s -> !s.isEmpty();

        // Step 2: Convert to uppercase
        Function<String, String> toUpperCase = String::toUpperCase;

        // Step 3: Add greeting
        Function<String, String> addGreeting = s -> "Hello, " + s + "!";

        // Chain everything together
        Function<String, String> processName = toUpperCase.andThen(addGreeting);

        System.out.println("\nProcessed names:");
        names.stream()
                .filter(isNotEmpty)
                .map(processName)
                .forEach(System.out::println);

        // Example 4: Custom higher-order function
        System.out.println("\nNumber transformation using higher-order function:");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // Higher-order function that returns a function to apply a specific operation
        Function<String, Function<Integer, Integer>> operationFactory = operation -> {
            switch (operation) {
                case "square": return x -> x * x;
                case "cube": return x -> x * x * x;
                case "double": return x -> x * 2;
                default: return x -> x;
            }
        };

        // Apply different operations using the factory
        System.out.println("Original numbers: " + numbers);

        Function<Integer, Integer> square = operationFactory.apply("square");
        List<Integer> squaredNumbers = numbers.stream()
                .map(square)
                .toList();
        System.out.println("Squared numbers: " + squaredNumbers);

        Function<Integer, Integer> cube = operationFactory.apply("cube");
        List<Integer> cubedNumbers = numbers.stream()
                .map(cube)
                .toList();
        System.out.println("Cubed numbers: " + cubedNumbers);

        Function<Integer, Integer> doubleIt = operationFactory.apply("double");
        List<Integer> doubledNumbers = numbers.stream()
                .map(doubleIt)
                .toList();
        System.out.println("Doubled numbers: " + doubledNumbers);
    }
}