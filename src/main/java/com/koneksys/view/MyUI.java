package com.koneksys.view;

import com.koneksys.model.Person;
import com.koneksys.service.PersonService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import java.util.List;

@Theme("valo")
public class MyUI extends UI {

    @EJB
    private PersonService personService;

    final Grid grid = new Grid();
    final BeanItemContainer<Person> beanItemContainer = new BeanItemContainer<>(Person.class);
    private TextField txtFilter = new TextField();
    private PersonForm form = new PersonForm(this);


    @Override
    protected void init(VaadinRequest vaadinRequest) {

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
                grid.deselectAll();
                form.setPerson(new Person());
            }
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, btnAddPerson, new Label("For Update or Delete, click the person record."));
        toolbar.setSpacing(true);

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setContainerDataSource(beanItemContainer);
        grid.setColumns("name", "age", "country");

        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        main.setSpacing(true);
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);

        layout.addComponents(welcome, toolbar, main);

        // fetch list of Customers from service and assign it to Grid
        updateList();

        setContent(layout);

        form.setVisible(false);

        grid.addSelectionListener(new SelectionEvent.SelectionListener() {
            @Override
            public void select(SelectionEvent selectionEvent) {
                if (grid.getSelectedRow() != null) {
                    Person person = (Person) grid.getSelectedRow();
                    form.setPerson(person);
                } else {
                    form.setVisible(false);
                }
            }
        });

    }

    public void updateList() {

        try {
            Context context = new InitialContext();
            personService = (PersonService) context.lookup("java:global/application/PersonServiceImpl!com.koneksys.service.PersonService");
            List<Person> listPerson = personService.findAll(txtFilter.getValue());
            beanItemContainer.removeAllItems();
            beanItemContainer.addAll(listPerson);
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
