# Trace ID Generator Benchmark

This project benchmarks different trace ID generation methods:
1. Java's `UUID.randomUUID()`
2. OpenTelemetry's `RandomIdGenerator`

## Overview

The benchmark compares both string and raw byte generation performance for:
- UUID.randomUUID() - standard Java UUID generation
- OpenTelemetry RandomIdGenerator - used in OpenTelemetry for trace ID generation

`RandomIdGenerator.generateTraceId()` is *~17x faster* than `UUID.randomUUID().toString()` when running experiments with a single JMH thread while it is *~150x faster* in experiments run with 10 JMH threads (i.e. typical web application settings). Jump to [results on my machine](#Results-on-my-machine).


## Prerequisites

- Java 17 or later
- Gradle 9.x or later

## Setup

1. Clone this repository
2. Run the benchmark:
   ```bash
   ./gradlew clean jmhReport -PjavaVersion=25 -Pjmh.threads=10
   ```

## Benchmark Details

The benchmark includes four test scenarios:
1. `uuidBasedTraceId` - Measures `UUID.randomUUID().toString()`
2. `openTelemetryTraceId` - Measures `RandomIdGenerator.generateTraceId()`

Configuration:
- Warmup: 5 iterations
- Measurement: 10 iterations
- Forks: 2
- Modes: Average Time
- Time Unit: Nanoseconds
- Threads: Default 1 threads which can be overriden by `-Pjmh.threads=<numberOfThreads>`
- JavaVersion: Default Java 17 which can be overriden by `-PjavaVersion=<JavaVersionNumber>`

## Results

Benchmark results are generated in two formats:
1. JSON format: `build/reports/jmh/results.json`
2. HTML report: `build/reports/jmh/index.html`

### Live JMH Report

The HTML report can also be viewed on `https://jmh.morethan.io`. This is a free online tool for visualizing JMH benchmark results. It reads data directly from a GitHub Gist, allowing for easy sharing and interactive analysis of benchmark data.

The GitHub Actions workflow for this project is configured to automatically update a Gist with the latest benchmark results upon every push to the `main` branch. The workflow runs the benchmarks across various configurations (different JDK versions and thread counts) and uploads each result to the Gist.

You can view the live, interactive report of the latest benchmark run here:

[View Live JMH Report](https://jmh.morethan.io/?gist=ea5b5c1a7b77cc9077bfa50ab2050e2d)

### Reading the Results

The benchmark measures:
- Average Time (ns): Lower is better

Key metrics to look for:
- Score: The primary measurement result
- Error: The statistical error margin
- Units: The measurement unit (ns/op for time, ops/ns for throughput)

## Results on my machine
### My Machine details
Infrastructure:
   - Hardware: Apple M3 Pro 18 GB
   - OS: 26.0.1
Java Settings:
   - Default:
      - Available processors: 11
      - Max memory: 4608MB
   - Overrides:
      - `java.security.egd=''` to force using `DRBG` for `SecureRandom` implementation used by `UUID`.

### Experiments with JMH threads = 1
#### JMH threads: 1 JVM version: JDK 25, OpenJDK 64-Bit Server VM, 25+36-LTS (Zulu25.28+85-CA)

|Benchmark | Mode | Cnt | Score | Error | Units |
|----------|------|-----|-------|-------|-------|
|TraceIdGeneratorBenchmark.openTelemetryTraceId |  avgt |  20  |  14.321 | ±  0.130 |  ns/op |
|TraceIdGeneratorBenchmark.uuidBasedTraceId     |  avgt |  20  | 247.141 | ±  8.476 |  ns/op |

#### JMH threads: 1 and JVM version: JDK 21.0.9, OpenJDK 64-Bit Server VM, 21.0.9+10-LTS (Zulu21.46+19-CA)

|Benchmark | Mode | Cnt | Score | Error | Units |
|----------|------|-----|-------|-------|-------|
|TraceIdGeneratorBenchmark.openTelemetryTraceId |  avgt |  20  |  14.528 | ±  0.474 |  ns/op |
|TraceIdGeneratorBenchmark.uuidBasedTraceId     |  avgt |  20  | 245.351 | ± 14.737 |  ns/op |

#### JMH threads: 1 and JVM version: JDK 17.0.17, OpenJDK 64-Bit Server VM, 17.0.17+10-LTS (Zulu17.62+17-CA)

|Benchmark | Mode | Cnt | Score | Error | Units |
|----------|------|-----|-------|-------|-------|
|TraceIdGeneratorBenchmark.openTelemetryTraceId |  avgt |  20  |  14.414 | ±  0.241 |  ns/op |
|TraceIdGeneratorBenchmark.uuidBasedTraceId     |  avgt |  20  | 862.166 | ± 13.122 |  ns/op |

### Experiments with JMH threads = 10

#### JMH threads: 10 and JVM version: JDK 25, OpenJDK 64-Bit Server VM, 25+36-LTS (Zulu25.28+85-CA)

|Benchmark | Mode | Cnt | Score | Error | Units |
|----------|------|-----|-------|-------|-------|
|TraceIdGeneratorBenchmark.openTelemetryTraceId |  avgt |  20 |   24.967 | ±  1.525 |  ns/op |
|TraceIdGeneratorBenchmark.uuidBasedTraceId     |  avgt |  20 | 3761.317 | ± 65.991 |  ns/op |

#### JMH threads: 10 and JVM version: JDK 21.0.9, OpenJDK 64-Bit Server VM, 21.0.9+10-LTS (Zulu21.46+19-CA)

|Benchmark | Mode | Cnt | Score | Error | Units |
|----------|------|-----|-------|-------|-------|
|TraceIdGeneratorBenchmark.openTelemetryTraceId |  avgt |  20 |   23.517 | ±   1.003 |  ns/op |
|TraceIdGeneratorBenchmark.uuidBasedTraceId     |  avgt |  20 | 4032.508 | ± 176.588 |  ns/op |



#### JMH threads: 10 and JVM version: JDK 17.0.17, OpenJDK 64-Bit Server VM, 17.0.17+10-LTS (Zulu17.62+17-CA)

|Benchmark | Mode | Cnt | Score | Error | Units |
|----------|------|-----|-------|-------|-------|
|TraceIdGeneratorBenchmark.openTelemetryTraceId |  avgt |  20  |    23.236 | ±   0.393 |  ns/op |
|TraceIdGeneratorBenchmark.uuidBasedTraceId     |  avgt |  20  | 12327.410 | ± 886.314 |  ns/op |

## Notes

- The benchmarks use JMH's Blackhole to prevent dead code elimination
- Each benchmark is run in isolated JVM forks to prevent cross-contamination

## License

MIT License