package idrabenia;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author Ilya Drabenia
 */
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@State(Scope.Thread)
public class HashBenchmark {

    @Param({ "1000" }) // "100", "200", "300", "500",
    public int iterations;

    private Hasher murmur3;
    private Hasher md5;
    private Hasher crc32;
    private Hasher adler32;
    private byte[] buffer = new byte[4096];

    @Setup(Level.Invocation)
    public void setUp() {
        murmur3 = Hashing.murmur3_128().newHasher();
        md5 = Hashing.md5().newHasher();
        crc32 = Hashing.crc32().newHasher();
        adler32 = Hashing.adler32().newHasher();
        Arrays.fill(buffer, (byte) 123);
    }

    @Benchmark
    public void testMd5() {
        for (int i = 0; i < iterations; i += 1) {
            md5.putBytes(buffer);
        }

        md5.hash().toString();
    }

    @Benchmark
    public void testMurMur3() {
        for (int i = 0; i < iterations; i += 1) {
            murmur3.putBytes(buffer);
        }

        murmur3.hash().toString();
    }

    @Benchmark
    public void testCrc32() {
        for (int i = 0; i < iterations; i += 1) {
            crc32.putBytes(buffer);
        }

        crc32.hash().toString();
    }

    @Benchmark
    public void testAdler32() {
        for (int i = 0; i < iterations; i += 1) {
            adler32.putBytes(buffer);
        }

        adler32.hash().toString();
    }

}
