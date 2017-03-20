package com.koneksys.dao;

import com.koneksys.model.Telephone;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

@Stateless
public class TelephoneDaoImpl implements TelephoneDao {

    @PersistenceContext(unitName = "applicationPU")
    EntityManager em;

    @Override
    public List<Telephone> findAll(String filter) {
        Query query;
        String queryFilter;

        if (filter.isEmpty()) {
            query = em.createQuery("from Telephone t order by t.idTelephone");
        } else {
            queryFilter = "from Telephone t where lower(number) like ('%" + filter.toLowerCase() + "%') order by p.idTelephone";
            query = em.createQuery(queryFilter);
        }

        return (List<Telephone>) query.getResultList();

    }

    @Override
    public List<Telephone> findByPersonId(Integer idPerson) {
        Query query = em.createQuery("from Telephone t left join fetch t.person p where p.idPerson = :idPerson");
        query.setParameter("idPerson", idPerson);

        return query.getResultList();
    }

    @Override
    public Telephone findByNumber(String number) {
        Query query = em.createQuery("from Telephone t left join fetch t.person p where p.number = :number");
        query.setParameter("number", number);
        return (Telephone) query.getSingleResult();
    }

    @Override
    public void insert(Telephone telephone) {
        em.persist(telephone);
    }

    @Override
    public void insert(Set<Telephone> telephones) {
        for (Telephone telephone : telephones) {
            this.insert(telephone);
        }
    }

    @Override
    public void update(Telephone telephone) {
        em.merge( telephone );

    }

    @Override
    public void delete(Telephone telephone) {
        telephone = em.find(Telephone.class, telephone.getIdTelephone());
        em.remove( telephone );
    }
}
