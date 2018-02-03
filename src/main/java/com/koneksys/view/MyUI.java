package com.koneksys.view;

import com.koneksys.model.Person;
import com.koneksys.model.PersonKnown;
import com.koneksys.model.Telephone;
import com.koneksys.service.PersonService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

@Theme("valo")
public class MyUI extends UI {

    @EJB
    private PersonService personService;

    final Grid gridPerson = new Grid();
    final Grid gridTelephone = new Grid();
    final Grid gridKnown = new Grid();

    final BeanItemContainer<Person> bicPerson = new BeanItemContainer<>(Person.class);
    final BeanItemContainer<Telephone> bicTelephone = new BeanItemContainer<>(Telephone.class);
    final BeanItemContainer<Person> bicKnown = new BeanItemContainer<>(Person.class);

    private TextField txtFilter = new TextField();
    private PersonForm formPerson = new PersonForm(this);
    private TelephoneForm formTelephone = new TelephoneForm(this);
    private KnownForm formKnown = new KnownForm(this);

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        String directory = "MyDatabases/Dataset1" ;
        Dataset dataset = TDBFactory.createDataset(directory) ;

        dataset.begin(ReadWrite.READ) ;
        // Get model inside the transaction
        Model model = dataset.getDefaultModel() ;
        dataset.end() ;

        dataset.begin(ReadWrite.WRITE) ;
        model = dataset.getDefaultModel() ;
        dataset.end() ;

        final VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        final Label welcome = new Label();
        welcome.setCaption("Welcome, this is the Person Web Application, the Exercise for Koneksys.");

        txtFilter.setInputPrompt("Filter by name...");
        txtFilter.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                updateList();
            }
        });

        Button btnClearFilter = new Button(FontAwesome.TIMES);
        btnClearFilter.setDescription("Clear the current filter");
        btnClearFilter.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                txtFilter.clear();
            }
        });

        CssLayout filtering = new CssLayout();
        filtering.addComponents(txtFilter, btnClearFilter);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        Button btnAddPerson = new Button("Add new person");
        btnAddPerson.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                gridPerson.deselectAll();
                formPerson.setPerson(new Person(), new Telephone());
                formTelephone.setVisible(false);
            }
        });

        final Button btnAddPhone = new Button("Add new Phone");
        btnAddPhone.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                gridTelephone.deselectAll();
                formTelephone.setTelephone((Person) gridPerson.getSelectedRow(), new Telephone());
            }
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, btnAddPerson, btnAddPhone, new Label("For Update or Delete, click the person record."));
        toolbar.setSpacing(true);

        gridPerson.setCaption("Persons");
        gridPerson.setSelectionMode(Grid.SelectionMode.SINGLE);
        gridPerson.setContainerDataSource(bicPerson);
        gridPerson.setColumns("name", "age", "country");
        gridPerson.setHeightMode(HeightMode.ROW);
        gridPerson.setHeightByRows(5);

        gridTelephone.setCaption("Telephone Numbers");
        gridTelephone.setSelectionMode(Grid.SelectionMode.SINGLE);
        gridTelephone.setContainerDataSource(bicTelephone);
        gridTelephone.setColumns("number");
        gridTelephone.setHeightMode(HeightMode.ROW);
        gridTelephone.setHeightByRows(5);

        gridKnown.setCaption("Friends");
        gridKnown.setSelectionMode(Grid.SelectionMode.SINGLE);
        gridKnown.setContainerDataSource(bicKnown);
        gridKnown.setColumns("name", "country");
        gridKnown.setHeightMode(HeightMode.ROW);
        gridKnown.setHeightByRows(5);

        HorizontalLayout grids = new HorizontalLayout(gridPerson, gridTelephone, gridKnown);
        grids.setSpacing(true);
        grids.setSizeFull();

        HorizontalLayout forms = new HorizontalLayout(formPerson, formTelephone, formKnown);
        forms.setSpacing(true);
        forms.setSizeFull();

        VerticalLayout main = new VerticalLayout(grids, forms);
        main.setSizeFull();
        main.setSpacing(true);

        gridPerson.setSizeFull();
        gridTelephone.setSizeFull();
        gridKnown.setSizeFull();

        main.setExpandRatio(grids, 1);

        layout.addComponents(welcome, toolbar, main);

        // fetch list of Customers from service and assign it to Grid
        updateList();
        if (bicPerson.size() > 0 && gridPerson.getSelectedRow() != null) {
            btnAddPhone.setVisible(true);
        } else {
            btnAddPhone.setVisible(false);
        }

        setContent(layout);

        formPerson.setVisible(false);
        formTelephone.setVisible(false);
        formKnown.setVisible(false);

        gridPerson.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent selectionEvent) {
                if (gridPerson.getSelectedRow() != null) {
                    Person person = (Person) gridPerson.getSelectedRow();

                    bicTelephone.removeAllItems();
                    bicTelephone.addAll(person.getTelephones());

                    bicKnown.removeAllItems();
                    if (person.getKnowns() != null && person.getKnowns().size() > 0) {
                        List<Person> listPerson = new ArrayList<>();
                        for (PersonKnown personKnown : person.getKnowns()) {
                            bicKnown.addBean(personKnown.getKnown());
                        }

                    }

                    Telephone telephone;
                    if (person.getTelephones() != null && person.getTelephones().size() > 0) {
                        gridTelephone.select(person.getTelephones().iterator().next());
                        telephone = (Telephone) gridTelephone.getSelectedRow();
                    } else {
                        telephone = new Telephone();
                    }

                    formPerson.setPerson(person, telephone);
                    formKnown.setPerson(person);

                    btnAddPhone.setVisible(gridPerson.getSelectedRow() != null);

                } else {
                    bicTelephone.removeAllItems();
                    formPerson.setVisible(false);
                    formTelephone.setVisible(false);
                    formKnown.setVisible(false);
                }
            }
        });

        gridTelephone.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent selectionEvent) {

                if (gridTelephone.getSelectedRow() != null) {
                    Person person = (Person) gridPerson.getSelectedRow();
                    Telephone telephone = (Telephone) gridTelephone.getSelectedRow();

                    formTelephone.setTelephone(person, telephone);

                }

            }
        });

    }

    public void updateList() {

        try {
            Context context = new InitialContext();
            personService = (PersonService) context.lookup("java:global/application/PersonServiceImpl!com.koneksys.service.PersonService");

            List<Person> listPerson = personService.findAll(txtFilter.getValue());
            bicPerson.removeAllItems();
            bicTelephone.removeAllItems();
            bicKnown.removeAllItems();

            bicPerson.addAll(listPerson);

            if (formPerson.isVisible()) {
                formPerson.setVisible(false);
            }

            if (formTelephone.isVisible()) {
                formTelephone.setVisible(false);
            }

            if (formKnown.isVisible()) {
                formKnown.setVisible(false);
            }

        } catch (NamingException e) {
            e.printStackTrace();
            Notification.show("Connection issue", "Can't find the JNDI server", Notification.Type.ERROR_MESSAGE);
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

    }

}
