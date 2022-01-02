package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import static org.junit.jupiter.api.Assertions.*;

class SwapField11AndField12ProcessorTest {

    Processor processor;

    @BeforeEach
    void setUp() {
        processor = new SwapField11AndField12Processor();
    }

    @Test
    void process() {

        var f11BeforeProcess = "f11";
        var f12BeforeProcess = "f12";

        var message = new Message.Builder(1L)
                .field11(f11BeforeProcess)
                .field12(f12BeforeProcess)
                .build();

        Message afterProcessMessage = processor.process(message);

        assertEquals(f11BeforeProcess, afterProcessMessage.getField12());
        assertEquals(f12BeforeProcess, afterProcessMessage.getField11());
    }
}