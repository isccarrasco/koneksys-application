package com.koneksys.service;

import com.koneksys.dao.PersonKnownDao;
import com.koneksys.model.PersonKnown;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class PersonKnownServiceImpl implements PersonKnownService {

    @Inject
    private PersonKnownDao personKnownDao;

    @Override
    public void insert(PersonKnown personKnown) throws Exception {
        personKnownDao.insert(personKnown);
    }

    @Override
    public void update(PersonKnown personKnown) throws Exception {
        personKnownDao.update(personKnown);
    }

    @Override
    public void delete(PersonKnown personKnown) throws Exception {
        personKnownDao.delete(personKnown);
    }

}
