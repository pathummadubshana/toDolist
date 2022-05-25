package Controler;

import DB.dbConnection;
import TM.toDoTm;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class toDolistcontroler {
    public Label lbluser;
    public Label lbluserId;
    public AnchorPane root;
    public Pane subroot;
    public TextField txtDiscription;
    public ListView<toDoTm> lstList;
    public TextField txtSelected;
    public Button btnDelete;
    public Button btnUpdate;

    public void initialize(){
        lbluser.setText("Hi " + loginPagecontroler.loginuserName+ " Welcome to To-do list");
        lbluserId.setText(loginPagecontroler.loginuserId);

        subroot.setVisible(false);
        loadList();

       setDisebleCommen(true);

        lstList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                if (lstList.getSelectionModel().getSelectedItem()==null){
                    return;
                }


                setDisebleCommen(false);
                subroot.setVisible(false);
                txtSelected.setText(lstList.getSelectionModel().getSelectedItem().getDescription());


            }
        });
    }
    public void setDisebleCommen(boolean isVisible){
        txtSelected.setDisable(isVisible);
        btnDelete.setDisable(isVisible);
        btnUpdate.setDisable(isVisible);

        txtSelected.clear();

    }

    public void btnlogoutOnaction(ActionEvent actionEvent) {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION,"Do you want to log out..?", ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)){
            try {
                Parent parent = FXMLLoader.load(this.getClass().getResource("../View/Loginpage.fxml"));
                Scene scene = new Scene(parent);
                Stage primaryStage = (Stage) root.getScene().getWindow();

                primaryStage.setScene(scene);
                primaryStage.setTitle("login");
                primaryStage.centerOnScreen();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

    }

    public void addnewTodoOnaction(ActionEvent actionEvent) {

        lstList.getSelectionModel().clearSelection();

        setDisebleCommen(true);

        subroot.setVisible(true);
        txtDiscription.requestFocus();

    }

    public void btnAddtodolistOnaction(ActionEvent actionEvent) {
        String id=autoGenretId();
        String discription = txtDiscription.getText();
        String userId = lbluserId.getText();

        Connection connection = dbConnection.getInstance().getConnection();

        try {
             PreparedStatement preparedStatement =connection.prepareStatement("insert into todo values(?,?,?)");
             preparedStatement.setObject(1,id);
             preparedStatement.setObject(2,discription);
             preparedStatement.setObject(3,userId);

             preparedStatement.executeUpdate();

            System.out.println("Add");

            txtDiscription.clear();
            subroot.setVisible(false);

            loadList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String autoGenretId(){

        Connection connection = dbConnection.getInstance().getConnection();
        String id ="";
        try {
            Statement statement= connection.createStatement();
            ResultSet resultSet= statement.executeQuery("select id from todo order by id desc limit 1");

            boolean isExit = resultSet.next();

            if (isExit){
                String todoId= resultSet.getString(1);
                todoId = todoId.substring(1, todoId.length());

                int intId = Integer.parseInt(todoId);
                intId++;
                if(intId<10){
                    id="T00" + intId;
                }else if(intId<100){
                    id="T0" + intId;;
                }else {
                    id="T" + intId;;
                }



            }else {
                id="T001";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return  id;

    }
    public  void  loadList(){
        ObservableList<toDoTm> todo =lstList.getItems();
         todo.clear();

         Connection connection = dbConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from todo where user_id=?");
            preparedStatement.setObject(1,lbluserId.getText());

           ResultSet resultSet =preparedStatement.executeQuery();

           while (resultSet.next()){
               String id=resultSet.getString(1);
               String description = resultSet.getString(2);
               String user_id = resultSet.getString(3);

               todo.add(new toDoTm(id,description,user_id));
           }
           lstList.refresh();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void btnUpdateOnaction(ActionEvent actionEvent) {
        String description = txtSelected.getText();
        String id = lstList.getSelectionModel().getSelectedItem().getId();

        Connection connection = dbConnection.getInstance().getConnection();


        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update todo set description = ? where id = ?");

            preparedStatement.setObject(1,description);
            preparedStatement.setObject(2,id );

            preparedStatement.executeUpdate();

            loadList();

            setDisebleCommen(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnDeleteOnaction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete this todo...?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get().equals(ButtonType.YES)){
            String id = lstList.getSelectionModel().getSelectedItem().getId();
            Connection connection = dbConnection.getInstance().getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("delete from todo where id =?");
                preparedStatement.setObject(1,id);

                preparedStatement.executeUpdate();
                loadList();

                setDisebleCommen(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


    }
}
