package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

public class EvenSecondThrowableProcessor implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public EvenSecondThrowableProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }


    @Override
    public Message process(Message message) {

        LocalDateTime now = dateTimeProvider.now();

        if (now.getSecond() % 2 == 0)  {
            throw new IllegalStateException("Exception time, cause number of seconds is even");
        }

        return message.toBuilder().build();
    }
}
