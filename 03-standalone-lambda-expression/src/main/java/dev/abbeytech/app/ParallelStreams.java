package dev.abbeytech.app;

import lombok.extern.slf4j.Slf4j;
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


@Slf4j
@Component
@Order(6)
public class ParallelStreams implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("=== Tutorial 6: Parallel Streams and Performance ===\n");

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