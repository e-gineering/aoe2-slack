package com.eg.aoe2slackbot.view;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static com.eg.aoe2slackbot.view.SlackMatchResult.durationAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SlackMatchResultTests {

    @Test
    void durationAsStringTest73() {
        String expected = "1:13";
        String actual = durationAsString(Duration.ofMinutes(73));
        assertEquals(expected, actual);
    }

    @Test
    void durationAsStringTest0() {
        String expected = "0:00";
        String actual = durationAsString(Duration.ofMinutes(0));
        assertEquals(expected, actual);
    }

    @Test
    void durationAsStringTest7() {
        String expected = "0:07";
        String actual = durationAsString(Duration.ofMinutes(7));
        assertEquals(expected, actual);
    }

    @Test
    void durationAsStringTest37() {
        String expected = "0:37";
        String actual = durationAsString(Duration.ofMinutes(37));
        assertEquals(expected, actual);
    }

    @Test
    void durationAsStringTest483() {
        String expected = "8:03";
        String actual = durationAsString(Duration.ofMinutes(483));
        assertEquals(expected, actual);
    }

    @Test
    void durationAsStringTest9117() {
        String expected = "151:57";
        String actual = durationAsString(Duration.ofMinutes(9117));
        assertEquals(expected, actual);
    }
}
