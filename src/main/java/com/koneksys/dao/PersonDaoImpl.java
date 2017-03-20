package com.koneksys.dao;

import com.koneksys.model.Person;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
public class PersonDaoImpl implements PersonDao {

    @PersistenceContext(unitName = "applicationPU")
    EntityManager em;

    public List<Person> findAll(String filter) {
        Query query;
        String queryFilter;

        if (filter.isEmpty()) {
            query = em.createQuery("from Person p left join fetch p.telephones t order by p.idPerson");
        } else {
            queryFilter = "from Person p " +
                    "left join fetch p.telephones t " +
                    "where lower(name) like ('%" + filter.toLowerCase() + "%') order by p.idPerson";
            query = em.createQuery(queryFilter);
        }

        return (List<Person>) query.getResultList();
    }

    public Person findById(Integer id) {
        return em.find(Person.class, id);
    }

    public List<Person> findByCountry(String country) {
        Query query = em.createQuery("from Person p where p.country = :country");
        query.setParameter("country", country);
        return query.getResultList();
    }

    public void insert(Person person) {
        em.persist(person);
    }

    public void update(Person person) {
        em.merge( person );
    }

    public void delete(Person person) {
        person = em.find(Person.class, person.getIdPerson());
        em.remove( person );
    }

}
