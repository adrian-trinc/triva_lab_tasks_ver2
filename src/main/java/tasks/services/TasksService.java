package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.model.TasksOperations;
import tasks.repository.TaskIO;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class TasksService {
    private ArrayTaskList tasks;
    private File savedTasksFile;

    public File getSavedTasksFile() {
        return savedTasksFile;
    }

    public TasksService() throws IOException {
        this.tasks = new ArrayTaskList();
        this.savedTasksFile = new File("./src/main/java/tasks/data/tasks.txt");
        if (savedTasksFile.length() != 0) {
            TaskIO.readBR(this.tasks, savedTasksFile);
        }
    }

    public ObservableList<Task> getObservableList() {
        return FXCollections.observableArrayList(tasks.getAll());
    }

    public String getIntervalInHours(Task task) {
        int seconds = task.getRepeatInterval();
        int minutes = seconds / DateService.SECONDS_IN_MINUTE;
        int hours = minutes / DateService.MINUTES_IN_HOUR;
        minutes = minutes % DateService.MINUTES_IN_HOUR;
        return formTimeUnit(hours) + ":" + formTimeUnit(minutes);//hh:MM
    }

    public String formTimeUnit(int timeUnit) {
        StringBuilder sb = new StringBuilder();
        if (timeUnit < 10) sb.append("0");
        if (timeUnit == 0) sb.append("0");
        else {
            sb.append(timeUnit);
        }
        return sb.toString();
    }

    public int parseFromStringToSeconds(String stringTime) {//hh:MM
        String[] units = stringTime.split(":");
        int hours = Integer.parseInt(units[0]);
        int minutes = Integer.parseInt(units[1]);
        return (hours * DateService.MINUTES_IN_HOUR + minutes) * DateService.SECONDS_IN_MINUTE;
    }

    public Iterable<Task> filterTasks(Date start, Date end) {
        TasksOperations tasksOps = new TasksOperations(getObservableList());
        return tasksOps.incoming(start, end);
    }

    public Iterable<Task> filterTasksV2(Date start, Date end) {
        TasksOperations tasksOps = new TasksOperations(getObservableList());
        return tasksOps.incomingV2(start, end);
    }

    public void updateTask(Task task) {
        Optional<Task> optionalTask = tasks.getAll().stream().filter(taskIter -> taskIter.getTaskID().equals(task.getTaskID())).findFirst();
        if (optionalTask.isPresent()) {
            Task oldTask = optionalTask.get();
            oldTask.setTitle(task.getTitle());
            oldTask.setTime(task.getStartTime(), task.getEndTime(), task.getRepeatInterval());
            oldTask.setActive(task.isActive());
        }
    }

    public boolean removeTask(Task task) {
        return tasks.getAll().removeIf(taskIter -> taskIter.getTaskID().equals(task.getTaskID()));
    }
}
