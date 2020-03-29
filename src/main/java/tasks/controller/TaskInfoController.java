package tasks.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.log4j.Logger;
import tasks.model.Task;


public class TaskInfoController {

    private static final Logger log = Logger.getLogger(TaskInfoController.class.getName());
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelStart;
    @FXML
    private Label labelEnd;
    @FXML
    private Label labelInterval;
    @FXML
    private Label labelIsActive;

    private Task currentTask;
    private LaunchController rootController;

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public void setRootController(LaunchController rootController) {
        this.rootController = rootController;
    }

    @FXML
    public void initialize() {
        log.info("task info window initializing");
    }

    public void initializeCustom() {
        labelTitle.setText("Title: " + currentTask.getTitle());
        labelStart.setText("Start time: " + currentTask.getFormattedDateStart());
        labelEnd.setText("End time: " + currentTask.getFormattedDateEnd());
        labelInterval.setText("Interval: " + currentTask.getFormattedRepeated());
        labelIsActive.setText("Is active: " + (currentTask.isActive() ? "Yes" : "No"));
    }

    @FXML
    public void closeWindow() {
        rootController.closeInfoStage();
    }
}
