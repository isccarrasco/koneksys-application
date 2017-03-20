package com.koneksys.service;

import com.koneksys.model.Telephone;

import java.util.List;

public interface TelephoneService {

    public List<Telephone> findAll(String filter);

    public List<Telephone> findByPersonId(Integer idPerson);

    public Telephone findByNumber(String number);

    public void insert(Telephone telephone);

    public void update(Telephone telephone);

    public void delete(Telephone telephone);

}
