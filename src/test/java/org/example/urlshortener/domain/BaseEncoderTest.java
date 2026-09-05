package org.example.urlshortener.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaseEncoderTest {

    private final BaseEncoder baseEncoder = new BaseEncoder(62, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

    @ParameterizedTest
    @MethodSource("invalidInputProvider")
    void testEncode(String input) {
        assertThrows(IllegalArgumentException.class, () -> baseEncoder.encode(input));
    }

    @ParameterizedTest
    @MethodSource("validInputProvider")
    void testEncodeValidInput(String input, String expected) {
        assertEquals(expected, baseEncoder.encode(input));
    }

    private static Stream<Arguments> invalidInputProvider() {
        return Stream.of(
                Arguments.of((String)null),
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of("ABC"),
                Arguments.of("123Ab344")
        );
    }

    private static Stream<Arguments> validInputProvider() {
        return Stream.of(
                Arguments.of("202609051213123111", "eXWZXTFmAf"),
                Arguments.of("202609051213123112", "eXWZXTFmAg"),
                Arguments.of("1234567890", "1ly7vk")
        );
    }
}