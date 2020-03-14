package com.example.swtest;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class StringCalculatorTest {

    @Test
    public void testEmptyString() {
        StringCalculator calc = new StringCalculator();
        int result = calc.sum("");
        MatcherAssert.assertThat(result, Matchers.equalTo(0));
    }

}