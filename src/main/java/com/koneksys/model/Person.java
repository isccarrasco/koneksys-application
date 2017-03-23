package com.koneksys.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "person")
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_person", unique = true, nullable = false)
    private Integer idPerson;

    private String name;

    private int age;

    private String country;

    @OneToMany(mappedBy="person")
    private Set<Telephone> telephones;

    @OneToMany(mappedBy = "person")
    private Set<PersonKnown> knowns;

    public Person() {

    }

    public Person(String name, int age, String country) {
        this.name = name;
        this.age = age;
        this.country = country;
    }

    public Integer getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Integer idPerson) {
        this.idPerson = idPerson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Telephone> getTelephones() {
        return telephones;
    }

    public void setTelephones(Set<Telephone> telephones) {
        this.telephones = telephones;
    }

    public Set<PersonKnown> getKnowns() {
        return knowns;
    }

    public void setKnowns(Set<PersonKnown> knowns) {
        this.knowns = knowns;
    }

    @Override
    public String toString() {
        return "Person [idPerson=" + idPerson + ", name=" + name
                + ", age=" + age + ", country=" + country + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (age != person.age) return false;
        if (!idPerson.equals(person.idPerson)) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        return country != null ? country.equals(person.country) : person.country == null;
    }

    @Override
    public int hashCode() {
        int result = idPerson.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }
}
