package main.java.app.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.model.DBController;
import main.java.model.Task;


public class TaskDetailsController {

    @FXML private TextField tfTaskTitle;
    @FXML private ComboBox<String> cbTaskStatus, cbTaskAuthor, cbTaskContractor;
    @FXML private Label lblTaskId;

    private Stage dialogStage;
    private Task task;
    private boolean okClicked = false;

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
        this.dialogStage = dialogStage;
    }

    /**
     * Задаёт адресата, информацию о котором будем менять.
     * @param listTaskStatuses
     * @param listUserNames
     */
    void setComboBoxes(ObservableList<String> listTaskStatuses, ObservableList<String> listUserNames) {
        cbTaskStatus.setItems(listTaskStatuses);
        cbTaskAuthor.setItems(listUserNames);
        cbTaskContractor.setItems(listUserNames);
    }

    /**
     * Задаёт адресата, информацию о котором будем менять.
     * @param task
     */
    void setTask(Task task) {
        this.task = task;
        if (task != null) {
            lblTaskId.setText(Integer.toString(task.getId()));
            tfTaskTitle.setText(task.getTitle());
            cbTaskAuthor.setValue(task.getAuthor());
            cbTaskContractor.setValue(task.getContractor());
            cbTaskStatus.setValue(task.getStatus());
        } else {
            lblTaskId.setText("-1");
            tfTaskTitle.setText("");
            cbTaskAuthor.setValue("");
            cbTaskContractor.setValue("");
            cbTaskStatus.setValue("New");
            this.task = new Task();
        }
    }

    /**
     * Returns true, если пользователь кликнул OK, в другом случае false.
     */
    boolean isOkClicked() {
        return okClicked;
    }

    /** Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void onClickApply() {
        if (isInputValid()) {
            task.setId(Integer.parseInt(lblTaskId.getText()));
            task.setTitle(tfTaskTitle.getText());
            task.setAuthor(cbTaskAuthor.getValue());
            task.setContractor(cbTaskContractor.getValue());
            task.setStatus(cbTaskStatus.getValue());
            boolean flag;
            String errorMessage = "";
            try {
                DBController dbControl;
                dbControl = new DBController();
                dbControl.connectDB();
                if (task.getId() > 0) {
                    flag = dbControl.updateTask(task);
                } else {
                    flag = dbControl.createTask(task);
                }
                dbControl.disconnectDB();
            } catch (Exception e) {
                flag = false;
                errorMessage = "Error on SQL or connect to database";
            }
            if (!flag) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Error on UPDATE/CREATE");
                alert.setHeaderText("UPDATE/CREATE fail");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            }
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке Delete.
     */
    @FXML
    private void onClickDelete() {
        boolean flag;
        String errorMessage = "";
        if (task.getId() >= 0) {
            try {
                DBController dbControl;
                dbControl = new DBController();
                dbControl.connectDB();
                flag = dbControl.deleteTask(task.getId());
                dbControl.disconnectDB();
                dialogStage.close();
            } catch (Exception e) {
                flag = false;
                errorMessage = "Error on SQL or connect to database";
            }
        } else {
            flag = false;
            errorMessage = "No task to delete";
        }
        if (!flag) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Error on DELETE");
            alert.setHeaderText("DELETE fail");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        } else {
            okClicked = true;
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
     * Проверяет пользовательский ввод в текстовых полях.
     * @return true, если пользовательский ввод корректен
     */
    private boolean isInputValid() {
        String errorMessage = "";

        /**
        if (lblTaskId.getText() == null || lblTaskId.getText().length() == 0) {
            errorMessage += "No valid user ID!\n";
        } else {
            try {
                Integer.parseInt(lblTaskId.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid user ID (must be an integer)!\n";
            }
        }*/

        if (tfTaskTitle.getText() == null || tfTaskTitle.getText().length() == 0) {
            errorMessage += "No valid task title!\n";
        }
        if (cbTaskAuthor.getValue() == null || cbTaskAuthor.getValue().length() == 0) {
            errorMessage += "No valid author!\n";
        }
        if (cbTaskContractor.getValue() == null || cbTaskContractor.getValue().length() == 0) {
            errorMessage += "No valid contractor!\n";
        }
        if (cbTaskStatus.getValue() == null || cbTaskStatus.getValue().length() == 0) {
            errorMessage += "No valid status!\n";
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

