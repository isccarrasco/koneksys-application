package com.koneksys.view;

import com.koneksys.model.Person;
import com.koneksys.model.Telephone;
import com.koneksys.service.PersonService;
import com.koneksys.service.TelephoneService;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;

public class TelephoneForm extends FormLayout {

    private TextField txtPhone = new TextField("Phone Number");

    private BeanItemContainer<Telephone> bicPhone = new BeanItemContainer<Telephone>(Telephone.class);

    private Button savePhone = new Button("Save");
    private Button deletePhone = new Button("Delete");

    @EJB
    private PersonService personService;
    private TelephoneService telephoneService;

    private Person person;
    private Telephone telephone;
    private MyUI myUI;
    private BeanFieldGroup<Person> binderPerson = new BeanFieldGroup<>(Person.class);
    private BeanFieldGroup<Telephone> binderTelephone = new BeanFieldGroup<>(Telephone.class);

    public TelephoneForm(MyUI myUI) {
        this.myUI = myUI;

        txtPhone.setNullRepresentation("");

        try {
            Context context = new InitialContext();
            personService = (PersonService) context.lookup("java:global/application/PersonServiceImpl!com.koneksys.service.PersonService");
            telephoneService = (TelephoneService) context.lookup("java:global/application/TelephoneServiceImpl!com.koneksys.service.TelephoneService");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(savePhone, deletePhone);
        buttons.setSpacing(true);

        addComponents(txtPhone, buttons);

        savePhone.setStyleName(ValoTheme.BUTTON_PRIMARY);
        savePhone.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        binderTelephone.bind(txtPhone, "number");

        savePhone.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                save();
            }
        });

        deletePhone.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                delete();
            }
        });

    }

    public void setTelephone(Person person, Telephone telephone) {
        this.person = person;
        this.telephone = telephone;

        bicPhone.removeAllItems();
        if (person.getTelephones() != null) {
            bicPhone.addAll(person.getTelephones());
        }

        binderPerson.setItemDataSource(person);
        binderTelephone.setItemDataSource(telephone);

        // Show delete button for only customers already in the database
        deletePhone.setVisible(person.getIdPerson() != null);
        setVisible(true);
        txtPhone.selectAll();
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
            List<Telephone> phones = new ArrayList<>();

            if (person.getTelephones() != null) {
                phones.addAll(person.getTelephones());
            }
            phones.add(telephone);
            person.setTelephones(phones);
            personService.update(person);
        } else {
            List<Telephone> phones = new ArrayList<>();
            phones.add(telephone);
            person.setTelephones(phones);
            personService.insert(person);
        }

        myUI.updateList();
        setVisible(false);
    }

    private void delete() {
        personService.delete(person);
        myUI.updateList();
        setVisible(false);
    }

    private void deletePhone() {
        telephoneService.delete(telephone);
        myUI.updateList();
        setVisible(false);
    }


}
