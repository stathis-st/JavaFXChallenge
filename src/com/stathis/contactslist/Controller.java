package com.stathis.contactslist;

import com.stathis.contactslist.datamodel.Contact;
import com.stathis.contactslist.datamodel.ContactData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    private TableView<Contact> contactsTableView;

    @FXML
    private BorderPane mainBorderPane;

    public void initialize() {

        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");

        deleteMenuItem.setOnAction(event -> deleteContactItem());
        editMenuItem.setOnAction(event -> showEditContactDialog());

        listContextMenu.getItems().addAll(editMenuItem, new SeparatorMenuItem(), deleteMenuItem);

        contactsTableView.setContextMenu(listContextMenu);

        contactsTableView.setItems(ContactData.getInstance().getContacts());
        contactsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        contactsTableView.getSelectionModel().selectFirst();
    }

    @FXML
    public void showNewContactDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Contact");
        dialog.setHeaderText("Use this dialog to create a new contact");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contactItemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        DialogController controller = fxmlLoader.getController();

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Contact newContact = controller.getNewContact();
            if (!emptyFieldDetected(newContact.getFirstName(), newContact.getLastName(), newContact.getPhoneNumber(), newContact.getNotes())) {
                ContactData.getInstance().addContact(newContact);
                contactsTableView.getSelectionModel().select(newContact);
            }
        }
    }

    @FXML
    public void showEditContactDialog() {
        Contact selectedContact = contactsTableView.getSelectionModel().getSelectedItem();
        if (selectedContact == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Contact selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the contact you want to edit.");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit Contact");
        dialog.setHeaderText("Use this dialog to edit the selected contact");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contactItemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        DialogController controller = fxmlLoader.getController();
        controller.editContact(selectedContact);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            if (!emptyFieldDetected(controller.getFirstNameField(), controller.getLastNameField(), controller.getPhoneNumberField(), controller.getNotesField())) {
                controller.updateContact(selectedContact);
            }
        }
    }

    private boolean emptyFieldDetected(String firstName, String lastName, String phoneNumber, String notes) {
        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || notes.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty fields detected");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields.");
            alert.showAndWait();
            return true;
        }
        return false;
    }


    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.DELETE)) {
            deleteContactItem();
        }
    }

    @FXML
    public void deleteContactItem() {
        Contact selectedContact = contactsTableView.getSelectionModel().getSelectedItem();
        if (selectedContact == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Contact selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the contact you want to delete.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
        alert.setHeaderText("Delete contact: " + selectedContact.getFirstName() + " " + selectedContact.getLastName());
        alert.setContentText("Are you sure? Press OK to confirm, or cancel to Back out.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ContactData.getInstance().deleteContact(selectedContact);
        }
    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }

}
