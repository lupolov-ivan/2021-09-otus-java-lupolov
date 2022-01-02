package ru.otus.processor.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EvenSecondThrowableProcessorTest {

    Processor processor;
    DateTimeProvider dateTimeProvider;

    @BeforeEach
    void setUp() {
        dateTimeProvider = mock(DateTimeProvider.class);
        processor = new EvenSecondThrowableProcessor(dateTimeProvider);
    }

    @Test
    void process_withOddSecond_ReturnMessage() {

        var mockDateTime = LocalDateTime.of(2021,1,1,0,0, 1);
        when(dateTimeProvider.now()).thenReturn(mockDateTime);

        var id = 1L;
        var message = new Message.Builder(id).build();

        Message actualMessage = processor.process(message);

        assertEquals(message, actualMessage);

        verify(dateTimeProvider, times(1)).now();
    }

    @Test
    void process_withEvenSecond_ThrowException() {

        var mockDateTime = LocalDateTime.of(2021,1,1,0,0, 2);
        when(dateTimeProvider.now()).thenReturn(mockDateTime);

        var id = 1L;
        var message = new Message.Builder(id).build();

        assertThrows(IllegalStateException.class, () -> processor.process(message));

        verify(dateTimeProvider, times(1)).now();
    }
}