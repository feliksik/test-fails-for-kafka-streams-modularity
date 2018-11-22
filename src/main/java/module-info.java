// removing this file lets you run your test and build software with Kafka Streams (but you lose modularity)
module my.awesome.system.under.test {

    // removing this line lets you test (but assumes your System Under Test no longer depends on Kafka Streams)
    requires kafka.streams;

}