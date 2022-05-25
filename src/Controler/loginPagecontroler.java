package Controler;

import DB.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginPagecontroler {
    public AnchorPane root;
    public TextField txtUsername;
    public PasswordField txtPassword;

    public  static String loginuserId;
    public static  String loginuserName;

    public void lblCreateNewAccountOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../View/CreateAccount.fxml"));

        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Register");
        primaryStage.centerOnScreen();

    }

    public void btnloginOnaction(ActionEvent actionEvent) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.trim().isEmpty()){
            txtUsername.requestFocus();
        }else if(password.trim().isEmpty()){
            txtPassword.requestFocus();
        }else {
            Connection connection = dbConnection.getInstance().getConnection();

            try {
                PreparedStatement preparedStatement= connection.prepareStatement("select * from user where name =? and password = ?");
                preparedStatement.setObject(1,username);
                preparedStatement.setObject(2,password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){
                    loginuserId=resultSet.getString(1);
                    loginuserName=resultSet.getString(2);

                    Parent parent = FXMLLoader.load(this.getClass().getResource("../View/toDolist.fxml"));

                    Scene scene = new Scene(parent);

                    Stage primaryStage = (Stage) root.getScene().getWindow();
                    primaryStage.setScene(scene);
                    primaryStage.setTitle("toDolist");
                    primaryStage.centerOnScreen();



                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "User Name or Password not matched");
                    alert.showAndWait();

                    txtUsername.clear();
                    txtPassword.clear();
                }






            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }




    }
}
