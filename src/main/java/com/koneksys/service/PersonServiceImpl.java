package com.koneksys.service;

import com.koneksys.dao.PersonDao;
import com.koneksys.model.Person;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class PersonServiceImpl implements PersonService {

    @Resource
    private SessionContext contexto;

    @Inject
    private PersonDao personDao;

    public List<Person> findAll(String filter) {
        return personDao.findAll(filter);
    }

    public Person findById(Integer id) {
        return personDao.findById(id);
    }

    public List<Person> findByCountry(String country) {
        return personDao.findByCountry(country);
    }

    public void insert(Person person) {
        personDao.insert(person);
    }

    public void update(Person person) {
        personDao.update(person);
    }

    public void delete(Person person) {
        personDao.delete(person);
    }
}
