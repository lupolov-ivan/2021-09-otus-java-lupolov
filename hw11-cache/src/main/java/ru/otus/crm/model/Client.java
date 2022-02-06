package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.mapper.metadata.clazz.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    private Long id;

    private String name;
}
