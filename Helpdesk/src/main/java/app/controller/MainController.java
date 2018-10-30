package main.java.app.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import main.java.model.DBController;
import main.java.model.Task;
import java.io.IOException;

public class MainController {

    private Stage mainStage;
    private ObservableList<Task> taskForMeList, taskFromMeList = FXCollections.observableArrayList();
    private ObservableList<String> listTaskStatuses, listUserNames = FXCollections.observableArrayList();
    @FXML private Label lblUserName;
    @FXML private Button btnLoadTasks;
    @FXML private Button btnAddTask;
    @FXML private Button btnUserControl;
    @FXML private TableView TblTasksForMe;
    @FXML private TableView TblTasksFromMe;
    @FXML private TableColumn<Task, String> ColTaskID_forMe, ColTaskTitle_forMe, ColTaskAuthor_forMe, ColTaskStatus_forMe, ColTaskDateTime_forMe;
    @FXML private TableColumn<Task, String> ColTaskID_fromMe, ColTaskTitle_fromMe, ColTaskContractor_fromMe, ColTaskStatus_fromMe, ColTaskDateTime_fromMe;

    private String userLogin = "";
    private String userName = "";

    /**
     * Устанавливает сцену для этого окна.
     */
    public void setDialogStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    /**
     * Инициализирует слушателей таблицам
     */
    @FXML
    private void initialize() {
        TblTasksForMe.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if ((event.getClickCount() > 1) && (TblTasksForMe.getItems().size() > 0) ) {
                    ObservableList<Task> task = TblTasksForMe.getSelectionModel().getSelectedItems();
                    Stage stage = new Stage();
                    try {
                        TaskDetailsController(stage, task.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        TblTasksFromMe.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if ((event.getClickCount() > 1) && (TblTasksForMe.getItems().size() > 0)) {
                    ObservableList<Task> task = TblTasksFromMe.getSelectionModel().getSelectedItems();
                    Stage stage = new Stage();
                    try {
                        TaskDetailsController(stage, task.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @FXML
    public void onClickUserControl() {
        Stage stage = new Stage();
        try {
            if (!userName.equals("")) {
                userName = "";
                userLogin = "";
                btnLoadTasks.disableProperty().set(true);
                btnAddTask.disableProperty().set(true);
                taskForMeList.clear();
                taskFromMeList.clear();
                lblUserName.setText("Guest");
                btnUserControl.setText("Log in");
            }
            else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../../../resources/userDetails.fxml"));  //"../../../resources/userDetails.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                UserDetailsController userDetailsController = loader.getController(); //получаем контроллер для второй формы
                userDetailsController.setDialogStage(stage);
                stage.showAndWait();

                userLogin = userDetailsController.getUserLogin();
                userName = userDetailsController.getUserName();
                if (!userLogin.equals("")){
                    btnLoadTasks.disableProperty().set(false);
                    btnAddTask.disableProperty().set(false);
                    lblUserName.setText(userName);
                    btnUserControl.setText("Log out");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickLoad(){
        try {
            DBController dbControl;
            dbControl = new DBController();
            dbControl.connectDB();
            taskForMeList = dbControl.getTasks(userName, 1);
            taskFromMeList = dbControl.getTasks(userName, 2);
            listTaskStatuses = dbControl.getTaskStatuses();
            listUserNames = dbControl.getUserNames();
            dbControl.disconnectDB();

            setTableData();
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setHeaderText("Fail on load data from database");
            alert.showAndWait();
        }
    }

    @FXML
    public void onClickAdd() {
        Stage stage = new Stage();
        try {
            TaskDetailsController(stage, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTableData() {
        ColTaskID_forMe.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColTaskTitle_forMe.setCellValueFactory(new PropertyValueFactory<>("title"));
        ColTaskAuthor_forMe.setCellValueFactory(new PropertyValueFactory<>("author"));
        ColTaskStatus_forMe.setCellValueFactory(new PropertyValueFactory<>("status"));
        ColTaskDateTime_forMe.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        TblTasksForMe.setItems(taskForMeList);

        ColTaskID_fromMe.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColTaskTitle_fromMe.setCellValueFactory(new PropertyValueFactory<>("title"));
        ColTaskContractor_fromMe.setCellValueFactory(new PropertyValueFactory<>("contractor"));
        ColTaskStatus_fromMe.setCellValueFactory(new PropertyValueFactory<>("status"));
        ColTaskDateTime_fromMe.setCellValueFactory(new PropertyValueFactory<>("datetime"));
        TblTasksFromMe.setItems(taskFromMeList);
    }

    @FXML
    private void TaskDetailsController(Stage stage, Task person) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../../../resources/taskDetails.fxml"));  //"../../../resources/userDetails.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);

        TaskDetailsController taskDetailsController = loader.getController(); //получаем контроллер для второй формы
        taskDetailsController.setComboBoxes(listTaskStatuses, listUserNames); // передаем необходимые параметры
        taskDetailsController.setTask(person); // передаем необходимые параметры
        taskDetailsController.setDialogStage(stage);
        stage.showAndWait();
        if (taskDetailsController.isOkClicked()){
            onClickLoad();
        }
    }

    @FXML
    public void onExit(){
        Platform.exit();
    }
}