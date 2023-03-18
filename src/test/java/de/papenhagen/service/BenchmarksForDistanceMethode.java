package de.papenhagen.service;

import de.papenhagen.enities.Root;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static org.openjdk.jmh.annotations.Scope.Thread;

@State(Thread)
@OutputTimeUnit(NANOSECONDS)
@BenchmarkMode(AverageTime)
@Fork(value = 1, jvmArgsAppend = {
        "-XX:+UseSuperWord",
        "-XX:+UnlockDiagnosticVMOptions",
        "-XX:CompileCommand=print,*BenchmarksForDistanceMethode.calculateDistanceMethode"})
@Warmup(iterations = 5)
@Measurement(iterations = 10)
public class BenchmarksForDistanceMethode {

    public static final int SIZE = 1024;

    @State(Thread)
    public static class Context {

        public final double[] results = new double[SIZE];

        public List[] input;

        @Setup
        public void setup() {
            final Random random = new Random();
            final List<Root> roots = new ArrayList<>(SIZE);
            for (int i = 0; i < (SIZE * 2); i++) {
                final double latitude = random.nextInt(90) * (random.nextBoolean() ? -1 : 1);
                final double longitude = random.nextInt(180) * (random.nextBoolean() ? -1 : 1);

                final Root root1 = new Root(latitude, longitude, "Hamburg", 22123);
                roots.add(root1);
            }

            // Creating object of List with reference to
            // ArrayList class Declaring object List<Root> type
            List<List<Root>> tempList = new ArrayList<>(
                    roots.stream()
                            .collect(Collectors.partitioningBy(
                                    s -> roots.indexOf(s) > SIZE))
                            .values());

            // an array containing both tempList
            input = new List[]{tempList.get(0), tempList.get(1)};
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE) //makes looking at assembly easier
    public void calculateDistanceMethode(final Context context) {
        if (context.input[0].size() <= SIZE && context.input[1].size() <= SIZE) {
            for (int i = 0; i < SIZE; i++) {
                final Root root1 = (Root) context.input[0].get(i);
                final Root root2 = (Root) context.input[1].get(i);

                context.results[i] = MeasuringService.calculateDistance(root1, root2);
            }
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE) //makes looking at assembly easier
    public void calculateDistanceOriginalMethode(final Context context) {
        if (context.input[0].size() <= SIZE && context.input[1].size() <= SIZE) {
            for (int i = 0; i < SIZE; i++) {
                final Root root1 = (Root) context.input[0].get(i);
                final Root root2 = (Root) context.input[1].get(i);

                context.results[i] = MeasuringService.calculateDistanceOriginal(root1, root2);
            }
        }
    }

}
