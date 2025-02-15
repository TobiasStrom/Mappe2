package com.tobiasstrom.s331392mappe2comtobiasstrom.model;
//Kontakt klassen
public class Contact {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private int contactId;

    public Contact() {
    }

    public Contact(String firstName, String lastName, String email, String phoneNumber, int contactId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.contactId = contactId;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getContactId() {
        return contactId;
    }
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    @Override
    public String toString() {
        return  firstName  + " " + lastName;
    }
}
