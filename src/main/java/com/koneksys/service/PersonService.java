package com.koneksys.service;

import com.koneksys.model.Person;

import java.util.List;

public interface PersonService {

    public List<Person> findAll(String filter);

    public Person findById(Integer id);

    public List<Person> findByCountry(String country);

    public void insert(Person person);

    public void update(Person person);

    public void delete(Person person);
}
