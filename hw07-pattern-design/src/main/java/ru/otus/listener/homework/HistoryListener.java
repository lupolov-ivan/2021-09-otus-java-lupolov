package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class HistoryListener implements Listener, HistoryReader {

    private final Set<Message> messages = new HashSet<>();

    @Override
    public void onUpdated(Message msg) {
        messages.add(msg.copy());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return messages.stream()
                .filter(message -> id == message.getId())
                .findFirst();
    }
}
