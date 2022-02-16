package ru.otus.crm.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Phone> phones = new ArrayList<>();

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        setClientToPhones(phones);
        this.phones = phones;
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
        phone.setClient(this);
    }

    public void removePhone(Phone phone) {
        phones.remove(phone);
        phone.setClient(null);
    }

    public Client copy() {

        List<Phone> phoneCopies = this.phones == null
                ? Collections.emptyList()
                : this.phones.stream().filter(Objects::nonNull).map(Phone::copy).collect(toList());

        Address addressCopy = this.address == null
                ? null
                : this.address.copy();

        return new Client(this.id, this.name, addressCopy, phoneCopies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Client client = (Client) o;
        return id != null && Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    private void setClientToPhones(List<Phone> phones) {
        if (phones != null) {
            phones.forEach(phone -> phone.setClient(this));
        }
    }
}
