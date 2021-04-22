package org.myan;


import com.google.common.primitives.Ints;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class StreamBenchmark {

    @Param({"10000", "100000", "1000000"})
    private int length;

    @Benchmark
    public List<Integer> mapWithStream() {
        int[] array = new int[length];
        Arrays.fill(new int[length], 1);
        return Ints.asList(array).stream()
                .mapToInt(x -> x)
                .map(x -> x++)
                .boxed()
                .collect(Collectors.toList());
    }

    @Benchmark
    public List<Integer> mapWithParallelStream() {
        int[] array = new int[length];
        Arrays.fill(new int[length], 1);
        return Ints.asList(array).parallelStream()
                .mapToInt(x -> x)
                .map(x -> x++)
                .boxed()
                .collect(Collectors.toList());
    }

    @Benchmark
    public List<Integer> mapWithoutStream() {
        List<Integer> result = new ArrayList<>();
        int[] array = new int[length];
        Arrays.fill(array, 1);
        for (int i : array) {
            result.add(++i);
        }
        return result;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(StreamBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(2)
                .measurementIterations(3)
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(options).run();
    }
}
