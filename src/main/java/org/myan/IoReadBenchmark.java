package org.myan;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class IoReadBenchmark {

    @Param({"10000"})
    private int fileSize;

    private File file;
    private FileChannel channel;
    private FileInputStream inputStream;
    private ByteBuffer buffer;

    @Setup(Level.Trial)
    public void before() throws IOException {
        file = File.createTempFile("IoRead", ".tmp");
        try (FileOutputStream output = new FileOutputStream(file)) {
            for (int i = 0; i < fileSize; i++) {
                output.write((byte) i);
            }
        }
        buffer = ByteBuffer.allocate(1);
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        file.delete();
    }

    @Setup(Level.Iteration)
    public void beforeIteration() throws IOException {
        channel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
        inputStream = new FileInputStream(file);
    }

    @TearDown(Level.Iteration)
    public void afterIteration() throws IOException {
        channel.close();
        inputStream.close();
    }

    @Benchmark
    public void readWithFileInputStream() throws IOException {
        int result = inputStream.read();
        if (result != -1) {
            inputStream.close();
            // reset the stream
            inputStream = new FileInputStream(file);
        }
    }

    @Benchmark
    public void readWithFileChannel() throws IOException {
        int result = channel.read(buffer);
        buffer.flip();
        if (result != -1) {
            // reset the channel
            channel.position(0);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(IoReadBenchmark.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(10))
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(options).run();
    }
}
