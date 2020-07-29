package controller;

import DBConnect.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.CustomerTM;
import util.ItemTM;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManageItemFormController {
    public AnchorPane root;
    public Button btnBack;
    public Button btnAddNewItem;
    public Button btnSave;
    public Button btnDelete;
    public TextField txtItemCode;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TableView<ItemTM> tblItems;

    public void initialize(){
        tblItems.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblItems.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItems.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblItems.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("quantityOnHand"));
        loadItems();

        tblItems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemTM>() {
            @Override
            public void changed(ObservableValue<? extends ItemTM> observable, ItemTM oldValue, ItemTM newValue) {
                ItemTM selectedItem = tblItems.getSelectionModel().getSelectedItem();

                if(selectedItem==null){
                    btnDelete.setDisable(true);
                    txtDescription.clear();
                    txtUnitPrice.clear();
                    txtQtyOnHand.clear();
                    return;
                }
                btnSave.setText("Update");
                btnDelete.setDisable(false);
                btnSave.setDisable(false);
                txtDescription.setDisable(false);
                txtUnitPrice.setDisable(false);
                txtQtyOnHand.setDisable(false);
                txtItemCode.setText(selectedItem.getItemCode());
                txtDescription.setText(selectedItem.getDescription());
                txtUnitPrice.setText(selectedItem.getUnitPrice());
                txtQtyOnHand.setText(selectedItem.getQuantityOnHand());

            }
        });
    }

    public void btnAddNewItem_OnAction(ActionEvent actionEvent) {
        btnSave.setText("Save");
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        btnDelete.setDisable(true);
        btnSave.setDisable(false);

        int maxId = 0;
        for (ItemTM items : tblItems.getItems()) {
            int id = Integer.parseInt(items.getItemCode().replace("I",""));
            if(maxId<id){
                maxId=id;
            }
        }
        maxId=maxId+1;
        String id = "";
        if(maxId<10){
            id="I00"+maxId;
        }else if(maxId<100){
            id="I0"+maxId;
        }else{
            id="I"+maxId;
        }
        txtItemCode.setText(id);

    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        String itemCode = txtItemCode.getText();
        String description = txtDescription.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        if(btnSave.getText().equals("Save")) {
            try {
                PreparedStatement pstm = DBConnection.getInstance().geConnection().prepareStatement
                        ("INSERT INTO Item VALUES (?,?,?,?)");
                pstm.setObject(1, itemCode);
                pstm.setObject(2, description);
                pstm.setObject(3, unitPrice);
                pstm.setObject(4, qtyOnHand);
                int affected = pstm.executeUpdate();

                if (affected > 0) {
                    loadItems();
                    tblItems.getSelectionModel().clearSelection();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to add the item, Please try again", ButtonType.OK).show();
                    txtDescription.requestFocus();
                    btnSave.setText("Save");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            try {
                PreparedStatement pstm = DBConnection.getInstance().geConnection().prepareStatement
                        ("UPDATE Item SET Description=?,UnitPrice=?,QtyOnHand=? where ItemCode = '"+itemCode+"'");
                pstm.setObject(1,description);
                pstm.setObject(2,unitPrice);
                pstm.setObject(3,qtyOnHand);
                int affected = pstm.executeUpdate();

                if(affected>0){
                    loadItems();
                    tblItems.getSelectionModel().clearSelection();
                }else{
                    new Alert(Alert.AlertType.ERROR,"Failed to Update, Please try again", ButtonType.OK).show();
                    txtDescription.requestFocus();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        String itemCode = txtItemCode.getText();
        try {
            PreparedStatement pstm = DBConnection.getInstance().geConnection().prepareStatement
                    ("DELETE FROM Item where itemCode=?");
            pstm.setObject(1,itemCode);
            int affected = pstm.executeUpdate();

            if(affected>0){
                loadItems();
                tblItems.getSelectionModel().clearSelection();
            }else{
                new Alert(Alert.AlertType.ERROR,"Failed to delete", ButtonType.OK).show();
                txtDescription.requestFocus();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadItems(){
        try {
            Statement stm = DBConnection.getInstance().geConnection().createStatement();
            ResultSet rst = stm.executeQuery("Select * from Item");
            ObservableList<ItemTM> items = tblItems.getItems();
            items.clear();
            while(rst.next()){
                String itemCode = rst.getString("ItemCode");
                String description = rst.getString("Description");
                String unitPrice = rst.getString("UnitPrice");
                String qtyOnHand = rst.getString("QtyOnHand");
                items.add(new ItemTM(itemCode,description,unitPrice,qtyOnHand));
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
