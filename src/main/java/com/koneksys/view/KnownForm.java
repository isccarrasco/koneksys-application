package com.koneksys.view;

import com.koneksys.model.Person;
import com.koneksys.model.PersonKnown;
import com.koneksys.model.PersonKnownPK;
import com.koneksys.service.PersonKnownService;
import com.koneksys.service.PersonService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KnownForm extends FormLayout {

    @EJB
    private PersonService personService;

    @EJB
    private PersonKnownService personKnownService;

    private Person person;
    private MyUI myUI;

    final BeanItemContainer<Person> bicPerson = new BeanItemContainer<>(Person.class);
    private TwinColSelect twinKnowns = new TwinColSelect("Select Targets");
    private Button saveKnown = new Button("Save");

    public KnownForm(MyUI myUI) {

        this.myUI = myUI;

        twinKnowns.setContainerDataSource(bicPerson);
        twinKnowns.setItemCaptionPropertyId("name");
        twinKnowns.setRows(5);
        twinKnowns.setSizeFull();
        twinKnowns.setImmediate(true);

        try {
            Context context = new InitialContext();
            personService = (PersonService) context.lookup("java:global/application/PersonServiceImpl!com.koneksys.service.PersonService");
            personKnownService = (PersonKnownService) context.lookup("java:global/application/PersonKnownServiceImpl!com.koneksys.service.PersonKnownService");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        setSizeFull();
        VerticalLayout components = new VerticalLayout(twinKnowns, saveKnown);
        components.setSpacing(true);

        addComponents(components);

        saveKnown.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveKnown.setClickShortcut(ShortcutAction.KeyCode.ENTER);


        saveKnown.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                save();
            }
        });

    }

    public void setPerson(Person person) {
        this.person = person;

        List<Person> listPerson = personService.findAll("");

        bicPerson.removeAllItems();
        bicPerson.addAll(listPerson);

        twinKnowns.clear();

        for (Person p : listPerson) {
            if (p.equals(person)) {
                bicPerson.removeItem(p);
            } else {
                for (PersonKnown pk : person.getKnowns()) {

                    if (p.equals(pk.getKnown())) {
                        twinKnowns.select(p);
                    }
                }
            }
        }

        setVisible(true);
    }

    private void save() {

        if (person.getIdPerson() != null && person.getIdPerson() > 0) {

            Set<Person> selected = (Set<Person>) twinKnowns.getValue();

            person.getKnowns().clear();

            for (Person known : selected) {
                PersonKnownPK personKnownPK = new PersonKnownPK();
                personKnownPK.setIdPerson(this.person.getIdPerson());
                personKnownPK.setIdKnown(known.getIdPerson());

                PersonKnown personKnown = new PersonKnown();
                personKnown.setPersonKnownPK(personKnownPK);
                personKnown.setPerson(this.person);
                personKnown.setKnown(known);

                person.getKnowns().add(personKnown);
            }

            personService.updateKnown(person);
        }

        myUI.updateList();
        setVisible(false);
    }


}
