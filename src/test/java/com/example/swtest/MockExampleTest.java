package com.example.swtest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

@RunWith(JMock.class)
public class MockExampleTest {
    private Mockery mock = new JUnit4Mockery();

    @Test
    public void xxx() {
        Collection c = mock.mock(Collection.class);
        mock.checking(new Expectations() {{
            oneOf(c).isEmpty();
            will(returnValue(false));
        }});
    }
}
