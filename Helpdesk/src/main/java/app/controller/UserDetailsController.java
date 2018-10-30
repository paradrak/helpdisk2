package main.java.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.model.DBController;


public class UserDetailsController {

    @FXML private TextField tfLogin, tfPass;

    private Stage dialogStage;
    private String userLogin = "";
    private String userName = "";

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Устанавливает сцену для этого окна.
     * @param dialogStage
     */
    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;}

    String getUserLogin() {
        return userLogin;}

    String getUserName() {
        return userName;}

    /**
     * Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void onClickLogin() {
        if (isInputValid()) {
            String login, password;
            login = tfLogin.getText();
            password = tfPass.getText();

            DBController dbControl;
            dbControl = new DBController();
            dbControl.connectDB();
            String[] tmp = dbControl.loginUser(login, password);
            dbControl.disconnectDB();

            if (tmp[0].equals("") || tmp[1].equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(dialogStage);
                alert.setTitle("Log in fail");
                alert.setHeaderText("Incorrect login/password!");
                alert.setContentText("Please, type correct login and password");
                alert.showAndWait();
                tfLogin.setText("");
                tfPass.setText("");
            }
            else {
                userLogin = tmp[0];
                userName = tmp[1];
                /**
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(dialogStage);
                alert.setTitle("Log in OK");
                alert.setHeaderText("All good: Welcome, " + userLogin + "!");
                alert.setContentText("log = " + userLogin + " name = " + userName);
                alert.showAndWait();
                */
                dialogStage.close();
            }
        }
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке Cancel.
     */
    @FXML
    private void onClickCancel() {
        dialogStage.close();
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке Delete.
     */
    @FXML
    private void onClickRegister() {
        dialogStage.close();
    }

    /**
     * Проверяет пользовательский ввод в текстовых полях.
     * @return true, если пользовательский ввод корректен
     */
    private boolean isInputValid() {
        String errorMessage = "";
        if (tfLogin.getText() == null || tfLogin.getText().length() == 0) {
            errorMessage += "No valid user name!\n";
        }
        if (tfPass.getText() == null || tfPass.getText().length() == 0) {
            errorMessage += "No valid password!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}

