package com.koneksys.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "telephone")
public class Telephone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telephone", unique = true, nullable = false)
    private Integer idTelephone;

    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_person")
    private Person person;

    public Telephone() {

    }

    public Integer getIdTelephone() {
        return idTelephone;
    }

    public void setIdTelephone(Integer idTelephone) {
        this.idTelephone = idTelephone;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Telephone{" +
                "number='" + number + '\'' +
                '}';
    }
}
