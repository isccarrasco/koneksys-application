package com.koneksys.service;

import com.koneksys.dao.TelephoneDao;
import com.koneksys.model.Telephone;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class TelephoneServiceImpl implements TelephoneService {

    @Inject
    private TelephoneDao telephoneDao;

    public List<Telephone> findAll(String filter) {
        return telephoneDao.findAll(filter);
    }

    public List<Telephone> findByPersonId(Integer id) {
        return telephoneDao.findByPersonId(id);
    }

    public Telephone findByNumber(String number) {
        return telephoneDao.findByNumber(number);
    }

    public void insert(Telephone telephone) {
        telephoneDao.insert(telephone);
    }

    public void update(Telephone telephone) {
        telephoneDao.update(telephone);
    }

    public void delete(Telephone telephone) {
        telephoneDao.delete(telephone);
    }

}
