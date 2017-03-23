# koneksys-application
Exercise for Koneksys application

For this exercise i used PostgreSQL database and an embedded web server (Wildfly),
for the view layer, i used Vaadin Framework.

Follow these step to create the database and tables requireds.

1. Create the database
   ```
   DROP DATABASE IF EXISTS db_name;
   CREATE DATABASE db_name;
   ```

2. Create the table Person
   ```
   CREATE TABLE person (
     id_person serial not null primary key unique,
     name character varying not null,
     age integer,
     country character varying
   );
   ```

3. Create the table Telephone
   ```
   CREATE TABLE telephone (
     id_telephone serial not null primary key unique,
     number character varying not null unique,
     id_person integer not null,
     constraint telephone_person_fk foreign key (id_person) references person (id_person)
   );
   ```
   
4. Create the table PersonKnown
   ```
   CREATE TABLE person_known
   (
     id_person integer not null,
     id_known integer not null,
     constraint person_person_fk foreign key (id_person) references person (id_person),
     constraint person_known_fk foreign key (id_known) references person (id_person)
   );
   ```

5. You can create the database usin the script ``database.sql`` saved on
``resource\script`` folder.
   
If you have an Wildfly web server, and you can manually create the datasource 
for connect to the new database, only update the ``persistence.xml`` file on
the ``<jta-data-source>`` and put the Datasource you have create, 
run ``maven package`` and then deploy the war on your server with ``application`` name. 


In other case, follow the next steps to configure the datasource inside a embedded Wildfly server.
   
6. After clone the project, go inside the application folder.

7. Change the value of the database connection parameters inside the ``application.properties`` file
   ```
   datasource.connection=jdbc:postgresql://localhost:5432/db_name
   datasource.user=[your-user-name]
   datasource.password=[your-password]
   ```

8. Compile the project.
   ```
   mvn clean compile package install
   ```

9. Run the Wildfly embedded web server.
   ```
   mvn wildfly:start
   ```

10. In the `pom.xml file, uncomment the line ``233``, for enable wildfly home after start
   ```
   <jbossHome>${project.build.directory}/wildfly-run/wildfly-10.1.0.Final</jbossHome>
   ```

11. Install the PostgreSQL Driver inside the Wildfly instance (if don't have it) 
using the install script defined on pom.xml
   ```
   mvn wildfly:execute-commands -P "install-driver"
   ```
   
   In case you have any error for the driver location, yo can update the driver location inside 
    the ``install-postgres-driver.cli`` changin the line where the location driver like this. 
    (i used the maven local repository iside the ``.cli`` file)
      
    ``--resources=${settings.localRepository}/org/postgresql/postgresql/42.0.0/postgresql-42.0.0.jar``
    
12. Install Datasource inside Wildfly instance.
   ```
   mvn wildfly:execute-commands -P "install-datasource"
   ```
   
13. Now you can deploy the application.
   ```
   mvn wildfly:deploy
   ```

14. Now, go to [http://localhost:8080/application](http://localhost:8080/application)
