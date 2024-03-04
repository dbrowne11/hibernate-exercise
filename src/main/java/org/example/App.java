package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


import java.util.List;
import java.util.Scanner;

import org.hibernate.query.Query;


public class App 
{
    public static void main( String[] args )
    {
        // Hibernate setup
        Configuration config = new Configuration();
        config.configure("hibernate.cfg.xml");
        // Create a session factory
        SessionFactory sessionFactory = config.buildSessionFactory();
        // Open the session
        Session session = sessionFactory.openSession();
        //Transaction transaction = session.beginTransaction();

        // Setup loop and input scanner
        boolean flag = true;
        Scanner scanner = new Scanner(System.in);
        while (flag) {
            // Open a new transaction (could put after input and not create if exit is selected, but I doubt that yields measurably better performance
            Transaction transaction = session.beginTransaction();
            // Prompt for operation
            System.out.println("""
                    Select database operation from the following options:\s
                    1: Add to database\s
                    2: Update database\s
                    3: Delete from database\s
                    4: Get Employee by ID
                    5: Get all employees
                    6: Exit""");

            int input = scanner.nextInt();
            //Variables common between multiple switch statements
            int empId;
            // Handle operations
            switch (input) {
                // Adding to databse
                case 1:
                    // Prompting and collecting input
                    System.out.println("database add selected");
                    System.out.println("Enter your name...");
                    String addName = scanner.next();
                    System.out.println("Enter your email...");
                    String addEmail = scanner.next();
                    // Create employee object -- this constructor increments primary key - id
                    Employee toAdd = new Employee(addName, addEmail);
                    // add to db
                    session.save(toAdd);        // save is depreciated, using persist I believe is now recommended
                    System.out.println("Inserted: " + toAdd);
                    break;
                // Updating database
                case 2:
                    // Prompy and collect input
                    System.out.println("Database Update Selected");
                    System.out.println("Enter target employee's ID");
                    empId = scanner.nextInt();
                    System.out.println("Enter your name...");
                    String updateName = scanner.next();
                    System.out.println("Enter your email...");
                    String updateEmail = scanner.next();
                    // Create employee object
                    Employee toUpdate = new Employee(empId, updateName, updateEmail);
                    // execute the update
                    try {
                        session.update(toUpdate);   // update depreciated -- merge is now preferred
                        System.out.println("Updated: " + toUpdate);
                    }
                    catch (Exception e) {
                        System.out.println("Update failed with error message: " + e.getMessage());
                    }
                    break;
                //Delete from database
                case 3:
                    // Prompt and read input
                    System.out.println("Database deletion selected");
                    System.out.println("Enter employee Id of employee");
                    empId = scanner.nextInt();
                    // Set up for deletion
                    Employee toDelete = new Employee();
                    toDelete.setId(empId);
                    // Execute deletion -- error occurs on not found
                    try {
                        session.delete(toDelete);
                    } catch (Exception e) {
                        System.out.println("Deletion failed with error message: " + e.getMessage());
                    }

                    System.out.println("Deleted Employee with Id: " + empId);
                    break;

                // Get by ID
                case 4:
                    // Prompt and read input
                    System.out.println("Selected get employee");
                    System.out.println("Enter employee ID");
                    empId = scanner.nextInt();
                    // Execute -- error occurs if id not found
                    try {
                        Employee toGet = session.load(Employee.class, empId);
                        System.out.println("Got employee: " + toGet);
                    }
                    catch (Exception e) {
                        System.out.println("Update failed with error message: " + e.getMessage());
                    }

                    break;
                // Get all Employees
                case 5:
                    System.out.println("Selected get all employees");
                    // Make select * equivalent query
                    Query<Employee> query = session.createQuery("FROM Employee");
                    // Get results then print them
                    List<Employee> employees = query.getResultList();
                    for (Employee employee: employees) {
                        System.out.println(employee.toString());
                    }
                    break;
                case 6:
                    // Exit
                    flag = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Ensure number selection is in range [1-6]");
                    break;
            }
            // Send operation to db
            transaction.commit();
            // Clears objects to ensure no naming clashes by hibernate
            session.clear();
        }
        // close the session
        session.close();
    }
}
