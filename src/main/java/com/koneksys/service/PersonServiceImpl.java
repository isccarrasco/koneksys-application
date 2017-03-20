package com.koneksys.service;

import com.koneksys.dao.PersonDao;
import com.koneksys.dao.PersonKnownDao;
import com.koneksys.dao.TelephoneDao;
import com.koneksys.model.Person;
import com.koneksys.model.PersonKnown;
import com.koneksys.model.Telephone;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class PersonServiceImpl implements PersonService {

    @Resource
    private SessionContext contexto;

    @Inject
    private PersonDao personDao;

    @Inject
    private TelephoneDao telephoneDao;

    @Inject
    private PersonKnownDao personKnownDao;

    public List<Person> findAll(String filter) {
        return personDao.findAll(filter);
    }

    public Person findById(Integer id) {
        return personDao.findById(id);
    }

    public List<Person> findByCountry(String country) {
        return personDao.findByCountry(country);
    }

    @Override
    public List<Person> findKnowns(Integer idPerson) {
        return personDao.findKnowns(idPerson);
    }

    @Transactional
    public void insert(Person person) {

        personDao.insert(person);

        for (Telephone telephone : person.getTelephones()) {
            telephone.setPerson(person);
            telephoneDao.insert(telephone);
        }

    }

    @Transactional
    public void update(Person person) {

        Person aux = personDao.findById(person.getIdPerson());
        if (aux.getKnowns().size() >= person.getKnowns().size()) {
            personDao.update(person);
            this.deleteRelationships(person);
        } else {
            this.deleteRelationships(person);
            personDao.update(person);
        }

    }

    @Override
    public void updateKnown(Person person) {

        Person p = personDao.findById(person.getIdPerson());

        for (PersonKnown pk : p.getKnowns()) {

            personKnownDao.delete(pk);

        }

        for (PersonKnown pk : person.getKnowns()) {
            personKnownDao.insert(pk);
        }

    }

    public void delete(Person person) throws Exception {

        for (Telephone telephone : person.getTelephones()) {
            telephoneDao.delete(telephone);
        }

        for (PersonKnown know : person.getKnowns()) {
            personKnownDao.delete(know);
        }

        personDao.delete(person);
    }

    private void deleteRelationships(Person person) {

        for (Telephone telephone  : person.getTelephones()) {
            telephone.setPerson(person);
            if (telephone.getIdTelephone() != null) {
                telephoneDao.update(telephone);
            } else {
                telephoneDao.insert(telephone);
            }
        }

        Person aux = personDao.findById(person.getIdPerson());
        for (PersonKnown friend : aux.getKnowns()) {
            personKnownDao.delete(friend);
        }

        for (PersonKnown known : person.getKnowns()) {
            known.setPerson(person);
            personKnownDao.insert(known);
        }
    }
}
