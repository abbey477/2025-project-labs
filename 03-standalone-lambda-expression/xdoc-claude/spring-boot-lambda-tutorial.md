# Spring Boot Java 17 Standalone (No-Web) Maven Project with Lambda Tutorials

This guide will help you create a Spring Boot standalone application (without web components) using Java 17 and Maven, focusing on lambda expressions. We'll explore various lambda expression concepts through practical examples.

## Project Setup

### 1. Create Maven Project Structure

First, let's create a Maven project with the necessary Spring Boot dependencies:

```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=lambda-tutorial \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

### 2. Configure `pom.xml`

Replace the content of `pom.xml` with the following:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>lambda-tutorial</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version> <!-- Latest stable version compatible with Java 17 -->
    </parent>

    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- Spring Boot Starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <!-- Testing Dependencies -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### 3. Create Main Application Class

Create `src/main/java/com/example/Application.java`:

```java
package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {
            System.out.println("Spring Boot application started successfully!");
            System.out.println("This is a standalone (no-web) Spring Boot application using Java 17");
            System.out.println("Check the tutorial packages for lambda expression examples");
        };
    }
}
```

## Lambda Expression Tutorials

Let's create several tutorial classes to demonstrate different aspects of lambda expressions.

### Tutorial 1: Basic Lambda Syntax

Create `src/main/java/com/example/tutorial/BasicLambda.java`:

```java
package com.example.tutorial;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Component
@Order(1)
public class BasicLambda implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("\n=== Tutorial 1: Basic Lambda Syntax ===\n");
        
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
```

### Tutorial 2: Method References

Create `src/main/java/com/example/tutorial/MethodReferences.java`:

```java
package com.example.tutorial;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@Order(2)
public class MethodReferences implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("\n=== Tutorial 2: Method References ===\n");
        
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
```

### Tutorial 3: Stream API with Lambdas

Create `src/main/java/com/example/tutorial/StreamsWithLambda.java`:

```java
package com.example.tutorial;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@Order(3)
public class StreamsWithLambda implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("\n=== Tutorial 3: Stream API with Lambdas ===\n");
        
        // Create a list of persons
        List<Person> people = Arrays.asList(
            new Person("John", 30, "Engineer"),
            new Person("Alice", 25, "Designer"),
            new Person("Bob", 35, "Engineer"),
            new Person("Carol", 28, "Manager"),
            new Person("Dave", 40, "Engineer"),
            new Person("Eve", 32, "Designer")
        );
        
        // Example 1: Filter operation
        System.out.println("Engineers:");
        List<Person> engineers = people.stream()
                .filter(person -> "Engineer".equals(person.getJob()))
                .collect(Collectors.toList());
        engineers.forEach(System.out::println);
        
        // Example 2: Map operation
        System.out.println("\nNames of all people:");
        List<String> names = people.stream()
                .map(Person::getName)
                .collect(Collectors.toList());
        names.forEach(System.out::println);
        
        // Example 3: Combining filter and map
        System.out.println("\nNames of engineers:");
        people.stream()
                .filter(person -> "Engineer".equals(person.getJob()))
                .map(Person::getName)
                .forEach(System.out::println);
        
        // Example 4: Sorting
        System.out.println("\nPeople sorted by age:");
        people.stream()
                .sorted((p1, p2) -> Integer.compare(p1.getAge(), p2.getAge()))
                .forEach(System.out::println);
        
        // Using method reference for comparison
        System.out.println("\nPeople sorted by name:");
        people.stream()
                .sorted(java.util.Comparator.comparing(Person::getName))
                .forEach(System.out::println);
        
        // Example 5: Reduction operations
        // Count
        long engineerCount = people.stream()
                .filter(person -> "Engineer".equals(person.getJob()))
                .count();
        System.out.println("\nNumber of engineers: " + engineerCount);
        
        // Sum
        int totalAge = people.stream()
                .mapToInt(Person::getAge)
                .sum();
        System.out.println("Total age: " + totalAge);
        
        // Average
        OptionalDouble averageAge = people.stream()
                .mapToInt(Person::getAge)
                .average();
        System.out.println("Average age: " + averageAge.orElse(0));
        
        // Example 6: Collectors
        // Grouping by job
        System.out.println("\nPeople grouped by job:");
        Map<String, List<Person>> peopleByJob = people.stream()
                .collect(Collectors.groupingBy(Person::getJob));
        
        peopleByJob.forEach((job, personList) -> {
            System.out.println(job + ":");
            personList.forEach(person -> System.out.println("  " + person));
        });
        
        // Example 7: Complex transformation (average age by job)
        System.out.println("\nAverage age by job:");
        Map<String, Double> averageAgeByJob = people.stream()
                .collect(Collectors.groupingBy(
                        Person::getJob,
                        Collectors.averagingInt(Person::getAge)
                ));
        
        averageAgeByJob.forEach((job, avgAge) -> 
                System.out.println(job + ": " + avgAge));
        
        // Example 8: Flat mapping
        System.out.println("\nAll skills (flatMap example):");
        List<Person> peopleWithSkills = Arrays.asList(
            new Person("John", 30, "Engineer", List.of("Java", "Python", "SQL")),
            new Person("Alice", 25, "Designer", List.of("Photoshop", "Illustrator", "Sketch")),
            new Person("Bob", 35, "Engineer", List.of("Java", "C++", "Rust"))
        );
        
        List<String> allSkills = peopleWithSkills.stream()
                .flatMap(person -> person.getSkills().stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        
        allSkills.forEach(System.out::println);
    }
    
    // Helper class
    static class Person {
        private final String name;
        private final int age;
        private final String job;
        private final List<String> skills;
        
        public Person(String name, int age, String job) {
            this(name, age, job, List.of());
        }
        
        public Person(String name, int age, String job, List<String> skills) {
            this.name = name;
            this.age = age;
            this.job = job;
            this.skills = skills;
        }
        
        public String getName() {
            return name;
        }
        
        public int getAge() {
            return age;
        }
        
        public String getJob() {
            return job;
        }
        
        public List<String> getSkills() {
            return skills;
        }
        
        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + ", job='" + job + "'}";
        }
    }
}
```

### Tutorial 4: Functional Interfaces and Composition

Create `src/main/java/com/example/tutorial/FunctionalComposition.java`:

```java
package com.example.tutorial;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
@Order(4)
public class FunctionalComposition implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("\n=== Tutorial 4: Functional Interfaces and Composition ===\n");
        
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
```

### Tutorial 5: Effective Patterns with Lambdas

Create `src/main/java/com/example/tutorial/EffectiveLambdaPatterns.java`:

```java
package com.example.tutorial;

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

@Component
@Order(5)
public class EffectiveLambdaPatterns implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("\n=== Tutorial 5: Effective Patterns with Lambdas ===\n");
        
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
```

### Tutorial 6: Parallel Streams and Performance

Create `src/main/java/com/example/tutorial/ParallelStreams.java`:

```java
package com.example.tutorial;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Order(6)
public class ParallelStreams implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("\n=== Tutorial 6: Parallel Streams and Performance ===\n");
        
        // Generate a large list of random numbers
        int size = 10_000_000;
        System.out.println("Generating " + size + " random numbers...");
        List<Integer> numbers = IntStream.range(0, size)
                .map(i -> ThreadLocalRandom.current().nextInt(100))
                .boxed()
                .collect(Collectors.toList());
        System.out.println("Generation complete.");
        
        // Example 1: Compare sequential vs parallel sum
        System.out.println("\nComparing sequential vs parallel sum:");
        
        // Sequential sum
        Instant start = Instant.now();
        long sequentialSum = numbers.stream()
                .mapToLong(Integer::longValue)
                .sum();
        Duration sequentialDuration = Duration.between(start, Instant.now());
        
        // Parallel sum
        start = Instant.now();
        long parallelSum = numbers.parallelStream()
                .mapToLong(Integer::longValue)
                .sum();
        Duration parallelDuration = Duration.between(start, Instant.now());
        
        System.out.println("Sequential sum: " + sequentialSum);
        System.out.println("Sequential duration: " + sequentialDuration.toMillis() + " ms");
        System.out.println("Parallel sum: " + parallelSum);
        System.out.println("Parallel duration: " + parallelDuration.toMillis() + " ms");
        System.out.println("Speedup: " + (double) sequentialDuration.toMillis() / parallelDuration.toMillis() + "x");
        
        // Example 2: Complex computation - finding prime numbers
        System.out.println("\nComparing sequential vs parallel prime finding:");
        
        // Function to check if a number is prime
        java.util.function.Predicate<Integer> isPrime = num -> {
            if (num <= 1) {
                return false;
            }
            if (num <= 3) {
                return true;
            }
            if (num % 2 == 0 || num % 3 == 0) {
                return false;
            }
            for (int i = 5; i * i <= num; i += 6) {
                if (num % i == 0 || num % (i + 2) == 0) {
                    return false;
                }
            }
            return true;
        };
        
        // Generate a smaller list for this test
        List<Integer> testNumbers = IntStream.range(1, 100_000)
                .boxed()
                .collect(Collectors.toList());
        
        // Sequential prime counting
        start = Instant.now();
        long sequentialPrimeCount = testNumbers.stream()
                .filter(isPrime)
                .count();
        sequentialDuration = Duration.between(start, Instant.now());
        
        // Parallel prime counting
        start = Instant.now();
        long parallelPrimeCount = testNumbers.parallelStream()
                .filter(isPrime)
                .count();
        parallelDuration = Duration.between(start, Instant.now());
        
        System.out.println("Sequential prime count: " + sequentialPrimeCount);
        System.out.println("Sequential duration: " + sequentialDuration.toMillis() + " ms");
        System.out.println("Parallel prime count: " + parallelPrimeCount);
        System.out.println("Parallel duration: " + parallelDuration.toMillis() + " ms");
        System.out.println("Speedup: " + (double) sequentialDuration.toMillis() / parallelDuration.toMillis() + "x");
        
        // Example 3: Parallel stream with stateful operations (caution)
        System.out.println("\nParallel streams with stateful operations (demonstration of potential issues):");
        
        // Stateful operation with sequential stream
        List<Integer> sequentialResult = new ArrayList<>();
        numbers.stream()
                .limit(1000)
                .filter(n -> n % 2 == 0)
                .forEach(sequentialResult::add);
        
        // Stateful operation with parallel stream
        List<Integer> parallelResult = new ArrayList<>();
        numbers.parallelStream()
                .limit(1000)
                .filter(n -> n % 2 == 0)
                .forEach(parallelResult::add);
        
        System.out.println("Sequential result size: " + sequentialResult.size());
        System.out.println("Parallel result size: " + parallelResult.size());
        System.out.println("Are results in same order? " + sequentialResult.equals(parallelResult));
        
        // Example 4: Safe way to collect results in parallel
        System.out.println("\nSafe parallel collection using collectors:");
        
        start = Instant.now();
        List<Integer> safeSequentialResult = numbers.stream()
                .limit(1000)
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        sequentialDuration = Duration.between(start, Instant.now());
        
        start = Instant.now();
        List<Integer> safeParallelResult = numbers.parallelStream()
                .limit(1000)
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        parallelDuration = Duration.between(start, Instant.now());
        
        System.out.println("Sequential result size: " + safeSequentialResult.size());
        System.out.println("Sequential duration: " + sequentialDuration.toMillis() + " ms");
        System.out.println("Parallel result size: " + safeParallelResult.size());
        System.out.println("Parallel duration: " + parallelDuration.toMillis() + " ms");
        
        // Example 5: When not to use parallel streams
        System.out.println("\nWhen parallel streams may not help - small data sets:");
        
        List<Integer> smallList = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        
        start = Instant.now();
        long smallSequentialSum = smallList.stream().mapToLong(Integer::longValue).sum();
        sequentialDuration = Duration.between(start, Instant.now());
        
        start = Instant.now();
        long smallParallelSum = smallList.parallelStream().mapToLong(Integer::longValue).sum();
        parallelDuration = Duration.between(start, Instant.now());
        
        System.out.println("Small list sequential sum: " + smallSequentialSum);
        System.out.println("Small list sequential duration: " + sequentialDuration.toNanos() + " ns");
        System.out.println("Small list parallel sum: " + smallParallelSum);
        System.out.println("Small list parallel duration: " + parallelDuration.toNanos() + " ns");
        System.out.println("For small datasets, parallel streams may have overhead that negates benefits.");
    }
}
