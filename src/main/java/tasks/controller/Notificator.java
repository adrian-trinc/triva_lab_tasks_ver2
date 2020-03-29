package tasks.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.controlsfx.control.Notifications;
import tasks.model.Task;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class Notificator extends Thread {

    private static final int MILLISECONDS_IN_SEC = 1000;
    private static final int SECONDS_IN_MIN = 60;

    private static final Logger log = Logger.getLogger(Notificator.class.getName());

    private ObservableList<Task> tasksList;
    private final AtomicBoolean atomicBoolean;

    public Notificator(ObservableList<Task> tasksList) {
        this.tasksList = tasksList;
        this.atomicBoolean = new AtomicBoolean(true);
    }

    private void processTask(Task t, Date currentDate) {
        if (t.isActive()) {
            if (t.isRepeated() && t.getEndTime().after(currentDate)) {
                Date next = t.nextTimeAfter(currentDate);
                long currentMinute = getTimeInMinutes(currentDate);
                long taskMinute = getTimeInMinutes(next);
                if (currentMinute == taskMinute) {
                    showNotification(t);
                }
            } else {
                if (!t.isRepeated() && (getTimeInMinutes(currentDate) == getTimeInMinutes(t.getTime()))) {
                    showNotification(t);
                }
            }
        }
    }

    @Override
    public void run() {
        Date currentDate = new Date();
        while (atomicBoolean.get()) {
            for (Task t : tasksList) {
                processTask(t, currentDate);
            }
            try {
                Thread.sleep((long) MILLISECONDS_IN_SEC * SECONDS_IN_MIN);
            } catch (InterruptedException e) {
                log.error("thread interrupted exception");
                Thread.currentThread().interrupt();
            }
            currentDate = new Date();
        }
    }

    public static void showNotification(Task task) {
        log.info("push notification showing");
        Platform.runLater(() -> Notifications.create().title("Task reminder").text("It's time for " + task.getTitle()).showInformation());
    }

    private static long getTimeInMinutes(Date date) {
        return date.getTime() / MILLISECONDS_IN_SEC / SECONDS_IN_MIN;
    }

    public void setAtomicBooleanValue(boolean value) {
        this.atomicBoolean.set(value);
    }
}
