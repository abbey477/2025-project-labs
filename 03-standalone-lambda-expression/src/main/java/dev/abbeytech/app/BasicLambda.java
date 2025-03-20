package dev.abbeytech.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
@Component
@Order(1)
public class BasicLambda implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("=== Tutorial 1: Basic Lambda Syntax ===\n");

        // Example 1: No parameters
        Runnable noParams = () -> System.out.println("Lambda with no parameters");
        noParams.run();

        // Example 2: One parameter (parentheses optional)
        Consumer<String> oneParam = message -> System.out.println("Message: " + message);
        oneParam.accept("Hello Lambda!");

        // Example 3: Multiple parameters
        BinaryOperation addition = (int a, int b) -> a + b;
        System.out.println("5 + 3 = " + addition.apply(5, 3));

        // Example 4: Type inference
        BinaryOperation subtraction = (a, b) -> a - b;
        System.out.println("5 - 3 = " + subtraction.apply(5, 3));

        // Example 5: Block body with return statement
        BinaryOperation multiplication = (a, b) -> {
            System.out.println("Multiplying " + a + " and " + b);
            return a * b;
        };
        System.out.println("5 * 3 = " + multiplication.apply(5, 3));

        // Using standard functional interfaces
        Predicate<Integer> isEven = num -> num % 2 == 0;
        System.out.println("Is 4 even? " + isEven.test(4));

        Function<String, Integer> stringLength = str -> str.length();
        System.out.println("Length of 'Lambda': " + stringLength.apply("Lambda"));

        Supplier<String> currentTime = () -> java.time.LocalTime.now().toString();
        System.out.println("Current time: " + currentTime.get());
    }

    // Custom functional interface
    @FunctionalInterface
    interface BinaryOperation {
        int apply(int a, int b);
    }
}