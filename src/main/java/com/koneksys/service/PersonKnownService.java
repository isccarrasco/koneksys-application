package com.koneksys.service;

import com.koneksys.model.PersonKnown;

public interface PersonKnownService {

    public void insert(PersonKnown personKnown) throws Exception;

    public void update(PersonKnown personKnown) throws Exception;

    public void delete(PersonKnown personKnown) throws Exception;

}
