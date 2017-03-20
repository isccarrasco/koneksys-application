package com.koneksys.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PersonKnownPK implements Serializable {

    @Column(name = "id_person", nullable = false)
    private Integer idPerson;

    @Column(name = "id_known", nullable = false)
    private Integer idKnown;

    public PersonKnownPK() {
    }

    public Integer getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Integer idPerson) {
        this.idPerson = idPerson;
    }

    public Integer getIdKnown() {
        return idKnown;
    }

    public void setIdKnown(Integer idKnown) {
        this.idKnown = idKnown;
    }

}
