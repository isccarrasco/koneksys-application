package com.koneksys.view;

import com.koneksys.model.Person;
import com.koneksys.service.PersonService;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class PersonForm extends FormLayout {

    private TextField txtName = new TextField("Name");
    private TextField txtAge = new TextField("Age");
    private TextField txtCountry = new TextField("Country");

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    @EJB
    private PersonService personService;
    private Person person;
    private MyUI myUI;
    private BeanFieldGroup<Person> binder = new BeanFieldGroup<>(Person.class);

    public PersonForm(MyUI myUI) {
        this.myUI = myUI;

        txtName.setNullRepresentation("");
        txtAge.setNullRepresentation("");
        txtCountry.setNullRepresentation("");

        try {
            Context context = new InitialContext();
            personService = (PersonService) context.lookup("java:global/application/PersonServiceImpl!com.koneksys.service.PersonService");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        buttons.setSpacing(true);
        addComponents(txtName, txtAge, txtCountry, buttons);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        binder.bind(txtName, "name");
        binder.bind(txtAge, "age");
        binder.bind(txtCountry, "country");

        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                save();
            }
        });

        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                delete();
            }
        });
    }

    public void setPerson(Person person) {
        this.person = person;
        binder.setItemDataSource(person);

        // Show delete button for only customers already in the database
        delete.setVisible(person.getIdPerson() != null);
        setVisible(true);
        txtName.selectAll();
    }

    private void save() {
        try {
            /* Update the value for person instance from binder data. */
            binder.commit();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }

        if (person.getIdPerson() != null &&
                person.getIdPerson() > 0) {
            personService.update(person);
        } else {
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


}
