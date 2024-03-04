package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Employee {


    private static int globalId = 1;
    @Id
    private int id;
    private String name;
    private String email;

    public Employee() {
    }

    public Employee(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // This constructor increments the globalId so successive calls to this constructor yield unique Ids
    public Employee(String name, String email) {
        this.id = globalId++;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {

        return "Employee { ID = " + id + ", " +
                "Name = " + name + ", Email = " + email + " }";
    }
}
