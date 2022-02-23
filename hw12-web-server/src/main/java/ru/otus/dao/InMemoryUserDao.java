package ru.otus.dao;

import ru.otus.model.User;

import java.util.*;

import static java.util.Objects.*;

public class InMemoryUserDao implements UserDao {

    private final Map<String, User> users;

    public InMemoryUserDao() {
        users = new HashMap<>();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(users.get(login));
    }

    public InMemoryUserDao withUser(User user) {
        requireNonNull(user, "User must not be null");
        requireNonNull(user.login(), "Login must not be null");

        users.put(user.login(), user);

        return this;
    }
}
