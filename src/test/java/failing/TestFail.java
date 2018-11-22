package failing;

import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.Test;

// note this isn't even depending on my.awesome.system.under.test
public class TestFail {

    @Test
    public void someTestMethod() {
        System.out.println("It works, we can access "+TopologyTestDriver.class.getName());
    }
}
