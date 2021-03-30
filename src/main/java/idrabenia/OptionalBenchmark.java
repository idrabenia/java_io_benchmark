package idrabenia;


import org.openjdk.jmh.annotations.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@State(Scope.Thread)
public class OptionalBenchmark {

    @Param({ "100", "200", "300", "500", "1000", "2000" })
    public int iterations;

    public Double[] array;

    @Setup(Level.Invocation)
    public void setUp() {
        array = new Double[iterations];

        for (int i = 0; i < iterations; i += 1) {
            double val = Math.random();
            array[i] = val > 0.5 ? val : null;
        }
    }

    @Benchmark
    public void testIfThen() {
        double sum = 0;

        for (int i = 0; i < iterations; i += 1) {
            if (array[i] != null) {
                sum += array[i];
            }
        }
    }

    @Benchmark
    public void testOptional() {
        double sum = 0;

        for (int i = 0; i < iterations; i += 1) {
            sum += Optional
                .ofNullable(array[i])
                .orElse(0.0);
        }
    }

}
