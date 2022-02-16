package ru.otus.model;

import java.util.List;

public record User(long id, String name, String login, String password,
                   List<Roles> roles) {

}
