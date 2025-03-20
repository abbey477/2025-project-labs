package dev.abbeytech.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
@Component
@Order(5)
public class EffectiveLambdaPatterns implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("=== Tutorial 5: Effective Patterns with Lambdas ===\n");

        // Pattern 1: Strategy Pattern with Lambdas
        System.out.println("Strategy Pattern Example:");
        TextProcessor processor = new TextProcessor();

        String text = "Hello, this is a sample text with UPPERCASE and lowercase letters.";

        // Different processing strategies
        System.out.println("Original: " + text);
        processor.processText(text, String::toUpperCase);
        processor.processText(text, String::toLowerCase);
        processor.processText(text, s -> s.replaceAll("[aeiou]", "*"));
        processor.processText(text, s -> new StringBuilder(s).reverse().toString());

        // Pattern 2: Builder Pattern with Lambdas
        System.out.println("\nBuilder Pattern Example:");
        Person person = new PersonBuilder()
                .with(builder -> {
                    builder.name = "John Doe";
                    builder.age = 30;
                    builder.email = "john.doe@example.com";
                })
                .build();

        System.out.println("Created person: " + person);

        // Pattern 3: Decorator Pattern with Lambdas
        System.out.println("\nDecorator Pattern Example:");
        Function<Integer, Integer> baseFun = n -> n + 1;

        // Decorate with logging
        Function<Integer, Integer> loggingDecorator = decorate(baseFun,
                input -> System.out.println("Input: " + input),
                output -> System.out.println("Output: " + output));

        // Decorate with validation
        Function<Integer, Integer> validatingDecorator = decorateWithValidation(baseFun,
                input -> input >= 0,
                "Input must be non-negative");

        System.out.println("Using logging decorator:");
        loggingDecorator.apply(5);

        System.out.println("\nUsing validation decorator:");
        try {
            System.out.println("Result with valid input: " + validatingDecorator.apply(5));
            System.out.println("Result with invalid input: " + validatingDecorator.apply(-5));
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }

        // Pattern 4: Command Pattern with Lambdas
        System.out.println("\nCommand Pattern Example:");
        CommandExecutor executor = new CommandExecutor();

        // Add commands
        executor.addCommand("print", () -> System.out.println("Executing print command"));
        executor.addCommand("save", () -> System.out.println("Executing save command"));
        executor.addCommand("exit", () -> System.out.println("Executing exit command"));

        // Execute commands
        executor.executeCommand("print");
        executor.executeCommand("save");
        executor.executeCommand("unknown");

        // Pattern 5: Chain of Responsibility with Lambdas
        System.out.println("\nChain of Responsibility Pattern Example:");

        // Create request handlers
        RequestHandler authHandler = request -> {
            if (request.contains("unauthorized")) {
                return "Authentication failed";
            }
            return null; // Pass to next handler
        };

        RequestHandler validationHandler = request -> {
            if (request.contains("invalid")) {
                return "Validation failed";
            }
            return null; // Pass to next handler
        };

        RequestHandler processingHandler = request -> {
            return "Request processed: " + request;
        };

        // Create the chain
        RequestHandler chain = createChain(
                authHandler,
                validationHandler,
                processingHandler
        );

        // Process requests
        System.out.println(chain.handle("valid request"));
        System.out.println(chain.handle("unauthorized request"));
        System.out.println(chain.handle("invalid data"));
    }

    // Strategy Pattern example
    static class TextProcessor {
        public void processText(String text, Function<String, String> strategy) {
            String result = strategy.apply(text);
            System.out.println("Processed: " + result);
        }
    }

    // Builder Pattern example
    static class Person {
        private final String name;
        private final int age;
        private final String email;

        public Person(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + ", email='" + email + "'}";
        }
    }

    static class PersonBuilder {
        String name;
        int age;
        String email;

        public PersonBuilder with(Consumer<PersonBuilder> builderFunction) {
            builderFunction.accept(this);
            return this;
        }

        public Person build() {
            return new Person(name, age, email);
        }
    }

    // Decorator Pattern example - logging decorator
    static <T, R> Function<T, R> decorate(
            Function<T, R> function,
            Consumer<T> beforeFunction,
            Consumer<R> afterFunction) {

        return input -> {
            beforeFunction.accept(input);
            R result = function.apply(input);
            afterFunction.accept(result);
            return result;
        };
    }

    // Decorator Pattern example - validation decorator
    static <T, R> Function<T, R> decorateWithValidation(
            Function<T, R> function,
            Predicate<T> validator,
            String errorMessage) {

        return input -> {
            if (!validator.test(input)) {
                throw new IllegalArgumentException(errorMessage);
            }
            return function.apply(input);
        };
    }

    // Command Pattern example
    static class CommandExecutor {
        private final java.util.Map<String, Runnable> commands = new java.util.HashMap<>();

        public void addCommand(String name, Runnable command) {
            commands.put(name, command);
        }

        public void executeCommand(String name) {
            Runnable command = commands.get(name);
            if (command != null) {
                command.run();
            } else {
                System.out.println("Command not found: " + name);
            }
        }
    }

    // Chain of Responsibility Pattern example
    @FunctionalInterface
    interface RequestHandler {
        String handle(String request);
    }

    static RequestHandler createChain(RequestHandler... handlers) {
        return request -> {
            for (RequestHandler handler : handlers) {
                String result = handler.handle(request);
                if (result != null) {
                    return result;
                }
            }
            return "Unhandled request: " + request;
        };
    }
}