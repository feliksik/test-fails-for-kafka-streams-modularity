module my.awesome.system.under.test {

    // org.apache.kafka.streams is a package split over 2 maven artifacts:
    //  Maven: org.apache.kafka:kafka-streams:2.0.1
    //  Maven: org.apache.kafka:kafka-streams-test-utils:2.0.1
    // by doing the following:

    requires kafka.streams;

    // now maven/surefire puts the kafka-streams in the module path, and the kafka-streams-test-utils in an
    // UNNAMED module (i.e. in the classpath).
    //
    // The consequence is that the package org.apache.kafka.streams as defined by the kafka.streams module
    // is exposed to the test TestFail -- but this means the part of that packages that is shipped in the
    // org.apache.kafka.stream jar (remember, split package!) is not available to that test.
    //
    // Note that intellij is cheating and just puts everything on the module path when running the test.
    // that's why it works there.

    // This can be worked around by
    // * removing the 'requires kafka.streams;' line above (lose the ability to use kafka streams in
    //    my.awesome.system.under.test )
    // * removing the module-info.java entirely, so that surefire creates one big unnamed module (lose modularity)

}