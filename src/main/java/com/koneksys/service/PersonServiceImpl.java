package com.koneksys.service;

import com.koneksys.dao.PersonDao;
import com.koneksys.dao.TelephoneDao;
import com.koneksys.model.Person;
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

    public List<Person> findAll(String filter) {
        return personDao.findAll(filter);
    }

    public Person findById(Integer id) {
        return personDao.findById(id);
    }

    public List<Person> findByCountry(String country) {
        return personDao.findByCountry(country);
    }

    @Transactional
    public void insert(Person person) {

        personDao.insert(person);

        for (Telephone telephone : person.getTelephones()) {
            telephone.setPerson(person);
            telephoneDao.insert(telephone);
        }

    }

    public void update(Person person) {

        personDao.update(person);

        for (Telephone telephone  : person.getTelephones()) {
            telephone.setPerson(person);
            if (telephone.getIdTelephone() != null) {
                telephoneDao.update(telephone);
            } else {
                telephoneDao.insert(telephone);
            }
        }

    }

    public void delete(Person person) {

        for (Telephone telephone : person.getTelephones()) {
            telephoneDao.delete(telephone);
        }

        personDao.delete(person);
    }
}
