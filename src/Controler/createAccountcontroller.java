package Controler;

import DB.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class createAccountcontroller {
    public PasswordField txtNewpassword;
    public PasswordField txtConformPassword;
    public Label lblpasswordNotmatch1;
    public Label lblpasswordNotmatch2;
    public Button btnRegister;
    public TextField txtusername;
    public TextField txtemail;
    public Label lbluserId;
    public AnchorPane root;

    public void initialize(){
        setVisibility(false);
        setDisebaleCommen(true);
    }

    public void btnRegisterOnaction(ActionEvent actionEvent) {
        String text = txtNewpassword.getText();
        String conf = txtConformPassword.getText();

        if(text.equals(conf)){

            setBordercolor("transparent");
            setVisibility(false);

            register();
        }else {
            setBordercolor("red");
            setVisibility(true);

            txtNewpassword.requestFocus();
        }

         Connection connection = dbConnection.getInstance().getConnection();
        System.out.println(connection);







    }
    public void setBordercolor(String color){
        txtNewpassword.setStyle("-fx-border-color:"+color);
        txtConformPassword.setStyle("-fx-border-color:"+color);
    }
    public void  setVisibility(boolean visibal){
        lblpasswordNotmatch1.setVisible(visibal);
        lblpasswordNotmatch2.setVisible(visibal);

    }

    public void btnaddNewUserOnaction(ActionEvent actionEvent) {
        setDisebaleCommen(false);

        txtusername.requestFocus();
        autoGenretId();
    }
    public void setDisebaleCommen(boolean isDiseble){
        txtusername.setDisable(isDiseble);
        txtemail.setDisable(isDiseble);
        txtNewpassword.setDisable(isDiseble);
        txtConformPassword.setDisable(isDiseble);
    }
    public void autoGenretId(){
        Connection connection = dbConnection.getInstance().getConnection();
        try {
            Statement statement= connection.createStatement();
           ResultSet resultSet= statement.executeQuery("select uid from user order by uid desc limit 1");

           boolean isExit = resultSet.next();

           if (isExit){
              String userId= resultSet.getString(1);
               userId= userId.substring(1, userId.length());

               int intId = Integer.parseInt(userId);
               intId++;
               if(intId<10){
                   lbluserId.setText("U00" +intId);
               }else if(intId<100){
                   lbluserId.setText("U0" + intId);
               }else {
                   lbluserId.setText("U" + intId);
               }



           }else {
               lbluserId.setText("U001");
           }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public  void  register(){
       String id= lbluserId.getText();
       String username = txtusername.getText();
       String email = txtemail.getText();
       String password = txtNewpassword.getText();

       if(username.trim().isEmpty()){
           txtusername.requestFocus();
       }else if(email.trim().isEmpty()){
           txtemail.requestFocus();
       }else if(txtNewpassword.getText().trim().isEmpty()){
           txtNewpassword.requestFocus();
       }else if(password.trim().isEmpty()){
           txtConformPassword.requestFocus();
       }else {
           Connection connection = dbConnection.getInstance().getConnection();

           try {
               PreparedStatement preparedStatement = connection.prepareStatement("insert into user values(?,?,?,?)");
               preparedStatement.setObject(1,id);
               preparedStatement.setObject(2,username);
               preparedStatement.setObject(3,email);
               preparedStatement.setObject(4,password);

               preparedStatement.executeUpdate();

               Parent parent = FXMLLoader.load(this.getClass().getResource("../View/Loginpage.fxml"));
               Scene scene = new Scene(parent);

               Stage primaryStage = (Stage) root.getScene().getWindow();

               primaryStage.setScene(scene);
               primaryStage.setTitle("Login");
               primaryStage.centerOnScreen();




           } catch (SQLException e) {
               throw new RuntimeException(e);
           } catch (IOException e) {
               throw new RuntimeException(e);
           }

       }




    }
}
