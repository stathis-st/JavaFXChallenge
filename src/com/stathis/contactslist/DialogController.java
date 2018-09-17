package com.stathis.contactslist;

import com.stathis.contactslist.datamodel.Contact;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DialogController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField notesField;

    public Contact getNewContact() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String notes = notesField.getText().trim();

        Contact newContact = new Contact(firstName, lastName, phoneNumber, notes);
        return newContact;
    }


    public void editContact(Contact selectedContact) {
        firstNameField.setText(selectedContact.getFirstName());
        lastNameField.setText(selectedContact.getLastName());
        phoneNumberField.setText(selectedContact.getPhoneNumber());
        notesField.setText(selectedContact.getNotes());
    }

    public void updateContact(Contact selectedContact) {

        selectedContact.setFirstName(firstNameField.getText().trim());
        selectedContact.setLastName(lastNameField.getText().trim());
        selectedContact.setPhoneNumber(phoneNumberField.getText().trim());
        selectedContact.setNotes(notesField.getText().trim());
    }

    public String getFirstNameField() {
        return firstNameField.getText().trim();
    }

    public String getLastNameField() {
        return lastNameField.getText().trim();
    }

    public String getPhoneNumberField() {
        return phoneNumberField.getText().trim();
    }

    public String getNotesField() {
        return notesField.getText().trim();
    }
}
