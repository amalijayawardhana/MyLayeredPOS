package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFormController {
    public AnchorPane root;

    public void btnManageCustomer_OnAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/ManageCustomerForm.fxml"));
            Scene customerScene = new Scene(root);
            Stage mainStage = (Stage)this.root.getScene().getWindow();
            mainStage.setScene(customerScene);
            mainStage.centerOnScreen();
        } catch (IOException e) {
            System.out.println("There's problem with loading the Manage Customer Form");
            e.printStackTrace();
        }
    }

    public void btnPlaceOrder_OnAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/PlaceOrderForm.fxml"));
            Scene orderScene = new Scene(root);
            Stage mainStage = (Stage)this.root.getScene().getWindow();
            mainStage.setScene(orderScene);
            mainStage.centerOnScreen();
        } catch (IOException e) {
            System.out.println("There's problem with loading the Place Order Form");
            e.printStackTrace();
        }
    }

    public void btnManageItem_OnAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/ManageItemForm.fxml"));
            Scene itemScene = new Scene(root);
            Stage mainStage = (Stage)this.root.getScene().getWindow();
            mainStage.setScene(itemScene);
            mainStage.centerOnScreen();
        } catch (IOException e) {
            System.out.println("There's problem with loading the Manage Item Form");
            e.printStackTrace();
        }
    }

    public void btnSearchOrder_OnAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/SearchOrderForm.fxml"));
            Scene searchScene = new Scene(root);
            Stage mainStage = (Stage) this.root.getScene().getWindow();
            mainStage.setScene(searchScene);
            mainStage.centerOnScreen();
        } catch (IOException e) {
            System.out.println("There's problem with loading the Search Order Form");
            e.printStackTrace();
        }
    }
}
