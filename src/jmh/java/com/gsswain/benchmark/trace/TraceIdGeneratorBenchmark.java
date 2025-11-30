package com.gsswain.benchmark.trace;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import io.opentelemetry.sdk.trace.IdGenerator;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(2)
public class TraceIdGeneratorBenchmark {
    
    private IdGenerator otelIdGenerator;

    static {
        printEnvironment();
    }
    
    @Setup
    public void setup() {
        otelIdGenerator = IdGenerator.random();
    }

    @Benchmark
    public void uuidBasedTraceId(Blackhole blackhole) {
        String traceId = UUID.randomUUID().toString();
        blackhole.consume(traceId);
    }

    @Benchmark
    public void openTelemetryTraceId(Blackhole blackhole) {
        String traceId = otelIdGenerator.generateTraceId();
        blackhole.consume(traceId);
    }

    public static void printEnvironment() {
        System.out.println("JVM: " + System.getProperty("java.version"));
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Max memory: " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB");
        printSecureRandomInfo();
    }

    public static void printSecureRandomInfo() {
        SecureRandom secureRandom = new SecureRandom();
        System.out.println("SecureRandom Provider: " + secureRandom.getProvider().getName());
        System.out.println("SecureRandom Algorithm: " + secureRandom.getAlgorithm());
        System.out.println("SecureRandom ThreadSafe: " + secureRandom.getProvider()
            .getProperty("SecureRandom." + secureRandom.getAlgorithm() + " ThreadSafe", "false"));
    }

}