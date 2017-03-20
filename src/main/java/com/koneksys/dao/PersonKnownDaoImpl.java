package com.koneksys.dao;

import com.koneksys.model.PersonKnown;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PersonKnownDaoImpl implements PersonKnownDao {

    @PersistenceContext(unitName = "applicationPU")
    EntityManager em;

    public void insert(PersonKnown personKnown) {
        em.persist(personKnown);
    }

    public void update(PersonKnown personKnown) {
        em.merge( personKnown );
    }

    public void delete(PersonKnown personKnown) {
        personKnown = em.find(PersonKnown.class, personKnown.getPersonKnownPK());
        em.remove( personKnown );
    }
}
