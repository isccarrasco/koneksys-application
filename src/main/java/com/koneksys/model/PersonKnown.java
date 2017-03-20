package com.koneksys.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "person_known")
public class PersonKnown implements Serializable {

    @EmbeddedId
    private PersonKnownPK personKnownPK;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_person", updatable = false, insertable = false)
    private Person person;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_known", updatable = false, insertable = false)
    private Person known;

    public PersonKnown() {
    }

    public PersonKnownPK getPersonKnownPK() {
        return personKnownPK;
    }

    public void setPersonKnownPK(PersonKnownPK personKnownPK) {
        this.personKnownPK = personKnownPK;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getKnown() {
        return known;
    }

    public void setKnown(Person known) {
        this.known = known;
    }

}
