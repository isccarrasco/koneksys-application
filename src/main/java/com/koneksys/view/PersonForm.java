package com.koneksys.view;

import com.koneksys.model.Person;
import com.koneksys.model.Telephone;
import com.koneksys.service.PersonService;
import com.koneksys.service.TelephoneService;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
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

public class PersonForm extends FormLayout {

    private TextField txtName = new TextField("Name");
    private TextField txtAge = new TextField("Age");
    private TextField txtCountry = new TextField("Country");

    private TextField txtPhone = new TextField("Phone Nomber");

    private Button savePerson = new Button("Save");
    private Button deletePerson = new Button("Delete");

    @EJB
    private PersonService personService;
    private TelephoneService telephoneService;

    private Person person;
    private Telephone telephone;
    private MyUI myUI;
    private BeanFieldGroup<Person> binderPerson = new BeanFieldGroup<>(Person.class);
    private BeanFieldGroup<Telephone> binderTelephone = new BeanFieldGroup<>(Telephone.class);

    public PersonForm(MyUI myUI) {
        this.myUI = myUI;

        txtName.setNullRepresentation("");
        txtAge.setNullRepresentation("");
        txtCountry.setNullRepresentation("");

        txtPhone.setNullRepresentation("");

        try {
            Context context = new InitialContext();
            personService = (PersonService) context.lookup("java:global/application/PersonServiceImpl!com.koneksys.service.PersonService");
            telephoneService = (TelephoneService) context.lookup("java:global/application/TelephoneServiceImpl!com.koneksys.service.TelephoneService");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        setSizeFull();
        HorizontalLayout buttons = new HorizontalLayout(savePerson, deletePerson);
        buttons.setSpacing(true);

        addComponents(txtName, txtAge, txtCountry, txtPhone, buttons);

        savePerson.setStyleName(ValoTheme.BUTTON_PRIMARY);
        savePerson.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        binderPerson.bind(txtName, "name");
        binderPerson.bind(txtAge, "age");
        binderPerson.bind(txtCountry, "country");

        binderTelephone.bind(txtPhone, "number");

        savePerson.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                save();
            }
        });

        deletePerson.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                delete();
            }
        });

    }

    public void setPerson(Person person, Telephone telephone) {
        this.person = person;
        this.telephone = telephone;

        binderPerson.setItemDataSource(person);
        binderTelephone.setItemDataSource(telephone);

        // Show delete button for only customers already in the database
        deletePerson.setVisible(person.getIdPerson() != null);
        txtPhone.setVisible(person.getIdPerson() == null || person.getIdPerson() == 0);
        setVisible(true);
        txtName.selectAll();
    }

    private void save() {
        try {
            /* Update the value for person instance from binder data. */
            binderPerson.commit();
            binderTelephone.commit();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }

        if (person.getIdPerson() != null && person.getIdPerson() > 0) {

            Set<Telephone> phones = new HashSet<>();

            if (person.getTelephones() != null) {
                phones.addAll(person.getTelephones());
            }

            phones.add(telephone);

            person.setTelephones(phones);

            personService.update(person);
        } else {

            List<Telephone> phones = new ArrayList<>();
            phones.add(telephone);


            Set<Telephone> setTelephone = new HashSet<>(phones);

            person.setTelephones(setTelephone);
            personService.insert(person);
        }

        myUI.updateList();
        setVisible(false);
    }

    private void delete() {
        try {

            personService.delete(person);
            myUI.updateList();
            setVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Can't Delete.","The person is known of other persons", Notification.Type.WARNING_MESSAGE);
        }

    }

    private void deletePhone() {
        telephoneService.delete(telephone);
        myUI.updateList();
        setVisible(false);
    }


}
