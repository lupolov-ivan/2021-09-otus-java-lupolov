package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.mapper.metadata.clazz.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager {

    @Id
    private Long no;

    private String label;
    private String param1;
}
