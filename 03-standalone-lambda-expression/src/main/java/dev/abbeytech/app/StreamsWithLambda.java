package dev.abbeytech.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Component
@Order(3)
public class StreamsWithLambda implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("=== Tutorial 3: Stream API with Lambdas ===\n");

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