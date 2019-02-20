package idrabenia;

import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardOpenOption.*;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@State(Scope.Thread)
public class IOBenchmark {
    private MappedByteBuffer mappedByteBuf;
    private FileChannel channelSync;
    private FileChannel usualChannel;

    @Setup
    public void setUp() throws IOException {
        mappedByteBuf = FileChannel
            .open(Paths.get("./test0.txt"), EnumSet.of(CREATE, READ, WRITE))
            .map(FileChannel.MapMode.READ_WRITE, 0, 1_000_000_000);

        channelSync = FileChannel.open(Paths.get("./test1.txt"), EnumSet.of(CREATE, READ, WRITE, SYNC));
        usualChannel = FileChannel.open(Paths.get("./test2.txt"), EnumSet.of(CREATE, READ, WRITE));
    }

    @Benchmark
    public void testMemoryMappedMethod() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);

        if (mappedByteBuf.position() > 10_000_000) {
            mappedByteBuf.position(0);
//            mappedByteBuf.force();
        }

        mappedByteBuf.put(buffer);
    }

    @Benchmark
    public void testSyncWriteMethod() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);

        channelSync.write(buffer);
    }

    @Benchmark
    public void testWriteMethod() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);

        usualChannel.write(buffer);
    }
}
