package org.myan;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class IoWriteBenchmark {

    @Param("1000000")
    private int fileSize;

    private File file;
    private FileOutputStream output;
    private int count;
    private FileChannel channel;
    private ByteBuffer buffer;


    @Setup(Level.Trial)
    public void before() throws IOException {
        file = File.createTempFile("IoWrite", ".tmp");
        buffer = ByteBuffer.allocate(1);
        buffer.put((byte) 47);
        buffer.flip();
    }

    @TearDown(Level.Trial)
    public void after() {
        file.delete();
    }

    @Setup(Level.Iteration)
    public void beforeIteration() throws IOException {
        output =  new FileOutputStream(file);
        channel = FileChannel.open(file.toPath(), StandardOpenOption.WRITE);
    }

    @TearDown(Level.Iteration)
    public void afterIteration() throws IOException {
        channel.close();
        output.close();
    }

}
