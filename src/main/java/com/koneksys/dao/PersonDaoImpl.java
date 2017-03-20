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
            query = em.createQuery("select distinct p from Person p " +
                    "left join fetch p.telephones t " +
                    "left join fetch p.knowns ks " +
                    "left join fetch ks.known k " +
                    "order by p.idPerson");
        } else {
            queryFilter = "select distinct p from Person p " +
                    "left join fetch p.telephones t " +
                    "left join fetch p.knowns ks " +
                    "left join fetch ks.known k " +
                    "where lower(name) like ('%" + filter.toLowerCase() + "%') order by p.idPerson";
            query = em.createQuery(queryFilter);
        }

        return (List<Person>) query.getResultList();
    }

    public Person findById(Integer id) {
        return em.find(Person.class, id);
    }

    public List<Person> findByCountry(String country) {
        Query query = em.createQuery("select p from Person p " +
                "where p.country = :country");
        query.setParameter("country", country);
        return query.getResultList();
    }

    @Override
    public List<Person> findKnowns(Integer idPerson) {
        String filterQuery = "select p from Person p " +
                "left join fetch p.telephones t " +
                "left join fetch p.knowns ks " +
                "left join fetch ks.known k " +
                "where k.idPerson = :idPerson ";
        Query query = em.createQuery(filterQuery);
        query.setParameter("idPerson", idPerson);
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
