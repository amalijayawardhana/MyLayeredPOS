package controller;

import DBConnect.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.CustomerTM;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageCustomerFormController {
    public AnchorPane root;
    public TextField txtCustomerID;
    public TextField txtCustomerName;
    public TextField txtAddress;
    public TableView<CustomerTM> tblCustomers;
    public Button btnSave;
    public Button btnDelete;
    public Button btnBack;
    public Button btnAddNewCustomer;

    public void initialize(){
        tblCustomers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tblCustomers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tblCustomers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        loadCustomers();

        tblCustomers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustomerTM>() {
            @Override
            public void changed(ObservableValue<? extends CustomerTM> observable, CustomerTM oldValue, CustomerTM newValue) {
                CustomerTM selectedItem = tblCustomers.getSelectionModel().getSelectedItem();

                if(selectedItem==null){
                    btnDelete.setDisable(true);
                    txtCustomerID.clear();
                    txtCustomerName.clear();
                    txtAddress.clear();
                    return;
                }
                btnSave.setText("Update");
                btnSave.setDisable(false);
                btnDelete.setDisable(false);
                txtCustomerName.setDisable(false);
                txtAddress.setDisable(false);
                txtCustomerID.setText(selectedItem.getCustomerId());
                txtCustomerName.setText(selectedItem.getCustomerName());
                txtAddress.setText(selectedItem.getAddress());
            }
        });
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        String id = txtCustomerID.getText();
        String name = txtCustomerName.getText();
        String address = txtAddress.getText();
        if(btnSave.getText().equals("Save")) {
            try {
                PreparedStatement pstm = DBConnection.getInstance().geConnection().prepareStatement("INSERT INTO Customer VALUES (?,?,?)");
                pstm.setObject(1, id);
                pstm.setObject(2, name);
                pstm.setObject(3, address);
                int affected = pstm.executeUpdate();

                if (affected > 0) {
                    loadCustomers();
                    tblCustomers.getSelectionModel().clearSelection();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save information", ButtonType.OK).show();
                    txtCustomerName.requestFocus();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                PreparedStatement pstm = DBConnection.getInstance().geConnection().prepareStatement("UPDATE Customer SET CustomerID=?,CustomerName=?,CustomerAddress=? WHERE CustomerID ='"+id+"'");
                pstm.setObject(1, id);
                pstm.setObject(2, name);
                pstm.setObject(3, address);
                int affected = pstm.executeUpdate();
                if(affected>0){
                    new Alert(Alert.AlertType.CONFIRMATION, "Updated Successfully", ButtonType.OK).show();
                }
                loadCustomers();
                tblCustomers.getSelectionModel().clearSelection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        String id = txtCustomerID.getText();
        try {
            PreparedStatement pstm = DBConnection.getInstance().geConnection().prepareStatement("DELETE  FROM Customer WHERE CustomerID=?");
            pstm.setObject(1,id);
            int affected = pstm.executeUpdate();
            if(affected>0){
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted Successfully", ButtonType.OK).show();
            }
            loadCustomers();
            tblCustomers.getSelectionModel().clearSelection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnAddNewCustomer_OnAction(ActionEvent actionEvent) {
        btnSave.setText("Save");
        txtCustomerName.clear();
        txtAddress.clear();
        btnSave.setDisable(false);
        btnDelete.setDisable(true);

        int maxId = 0;
        for (CustomerTM customer : tblCustomers.getItems()) {
            int id = Integer.parseInt(customer.getCustomerId().replace("C", ""));
            if (maxId < id) {
                maxId = id;
            }
        }
        maxId=maxId+1;
        String id = "";
        if(maxId<10){
            id="C00"+maxId;
        }else if(maxId<100){
            id="C0"+maxId;
        }else{
            id="C"+maxId;
        }
        txtCustomerID.setText(id);
    }

    public void loadCustomers(){
        try {
            Statement stm = DBConnection.getInstance().geConnection().createStatement();
            ResultSet rst = stm.executeQuery("Select * from Customer");
            ObservableList<CustomerTM> customers = tblCustomers.getItems();
            customers.clear();
            while(rst.next()){
                String id = rst.getString("CustomerId");
                String name = rst.getString("CustomerName");
                String address = rst.getString("CustomerAddress");
                customers.add(new CustomerTM(id,name,address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public void btnBack_OnAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainForm.fxml"));
            Scene mainScene = new Scene(root);
            Stage mainStage = (Stage)this.root.getScene().getWindow();
            mainStage.setScene(mainScene);
            mainStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
