# Java modularity, split packages, kafka streams

## Problem

If you run `mvn clean test`, this fails: 

```
[INFO] --- maven-compiler-plugin:3.8.0:testCompile (default-testCompile) @ test-fails ---
[INFO] Changes detected - recompiling the module!
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[INFO] Compiling 1 source file to /Users/efeliksik/ebay/test-fails-for-kafka-streams/target/test-classes
[INFO] -------------------------------------------------------------
[ERROR] COMPILATION ERROR :
[INFO] -------------------------------------------------------------
[ERROR] /Users/efeliksik/ebay/test-fails-for-kafka-streams/src/test/java/failing/TestFail.java:[3,32] cannot find symbol
  symbol:   class TopologyTestDriver
  location: package org.apache.kafka.streams
[ERROR] /Users/efeliksik/ebay/test-fails-for-kafka-streams/src/test/java/failing/TestFail.java:[11,55] cannot find symbol
  symbol:   class TopologyTestDriver
  location: class failing.TestFail
```

## Explanation
The package `org.apache.kafka.streams` is split over 2 maven artifacts:

* `org.apache.kafka:kafka-streams:2.0.1`
* `org.apache.kafka:kafka-streams-test-utils:2.0.1`

If you now have a module as follows: 
```
module my.awesome.system.under.test {
    requires kafka.streams;
}
```

You will not be able to write a test using kafka-streams-test-utils and test it with maven. 

The Maven surefire plugin runs the tests with the kafka-streams in the module path, and the kafka-streams-test-utils 
in an *unnamed* module (i.e. in the classpath).

The consequence is that the package `org.apache.kafka.streams` as defined by the `kafka.streams` module
is exposed to the test TestFail -- but this means the part of that packages that is shipped in the
`kafka-streams-test-utils` jar (remember, split package!) is not available to that test.

Note that intellij is cheating and just puts everything on the module path when running the test.
that's why it works there.

## Solution

#### Application developer
This can be worked around in this project by either
* removing the `requires kafka.streams;` line above (lose the ability to use kafka streams in
  my.awesome.system.under.test ), or
* removing the `module-info.java` entirely, so that surefire creates one big unnamed module (lose modularity)

Clearly both options are undesirable. 

#### Kafka maintainers

The kafka maintainers could not do split modules (by renaming the test package, shipping test artifacts for runtime, ...). 

This has been reported as https://issues.apache.org/jira/browse/KAFKA-7668

#### Java ecosystem

If split modules are assumed to be a legitimate thing, it remains the question whether this is up to the maven plugin 
maintainers to fix, or some other part of the java ecosystem. 
