package com.example.swtest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@RunWith(JMock.class)
public class MockExampleTest {
    private Mockery mock = new JUnit4Mockery();

    @Test
    public void testWebHook() {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject("https://enx8m1v7ntjv.x.pipedream.net/sharp", "Hi from sharp", String.class);
        System.out.println(result);
    }

    @Test
    public void testMock() {
        Collection c = mock.mock(Collection.class);
        mock.checking(new Expectations() {{
            oneOf(c).isEmpty();
            will(returnValue(false));
        }});
    }
}
