package com.koneksys.dao;

import com.koneksys.model.Telephone;

import java.util.List;
import java.util.Set;

public interface TelephoneDao {

    public List<Telephone> findAll(String filter);

    public List<Telephone> findByPersonId(Integer idPerson);

    public Telephone findByNumber(String number);

    public void insert(Telephone telephone);

    public void insert(Set<Telephone> telephones);

    public void update(Telephone telephone);

    public void delete(Telephone telephone);

}
