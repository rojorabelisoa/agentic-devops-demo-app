package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GreetingControllerTest {

    private final GreetingController controller = new GreetingController();

    @Test
    void greetsWorldByDefault() {
        assertThat(controller.greeting("world").message()).isEqualTo("Hello, world!");
    }

    @Test
    void stripsAndFallsBackOnBlankName() {
        assertThat(controller.greeting("   ").message()).isEqualTo("Hello, world!");
        assertThat(controller.greeting("  Ada ").message()).isEqualTo("Hello, Ada!");
    }

    @Test
    void truncatesLongNames() {
        String longName = "x".repeat(GreetingController.MAX_NAME_LENGTH + 10);
        assertThat(controller.greeting(longName).message())
                .isEqualTo("Hello, " + "x".repeat(GreetingController.MAX_NAME_LENGTH) + "!");
    }
}
