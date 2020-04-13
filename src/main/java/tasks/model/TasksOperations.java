package tasks.model;

import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("DuplicatedCode")
public class TasksOperations {
    private static final Logger log = Logger.getLogger(TasksOperations.class.getName());
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private List<Task> tasks;

    public TasksOperations(ObservableList<Task> tasksList) {
        tasks = new ArrayList<>();
        tasks.addAll(tasksList);
    }

    public Iterable<Task> incoming(Date start, Date end) {
        log.info(dateFormat.format(start));
        log.info(dateFormat.format(end));
        ArrayList<Task> incomingTasks = new ArrayList<>();
        for (Task t : tasks) {
            Date nextTime = t.nextTimeAfter(start);
            if (nextTime != null && (nextTime.before(end) || nextTime.equals(end))) {
                incomingTasks.add(t);
                log.info(t.getTitle());
            }
        }
        return incomingTasks;
    }

    public Iterable<Task> incomingV2(Date start, Date end) {
        log.info("V2");
        log.info(dateFormat.format(start));
        log.info(dateFormat.format(end));
        ArrayList<Task> incomingTasks = new ArrayList<>();
        log.info(tasks.size());
        for (Task t : tasks) {
            Date nextTime = t.nextTimeAfterOrEqual(start);
            log.info(nextTime);
            if (nextTime != null && (nextTime.before(end) || nextTime.equals(end))) {
                incomingTasks.add(t);
                log.info(t.getTitle());
            }
        }
        return incomingTasks;
    }

    public SortedMap<Date, Set<Task>> calendar(Date start, Date end) {
        Iterable<Task> incomingTasks = incoming(start, end);
        TreeMap<Date, Set<Task>> calendar = new TreeMap<>();

        for (Task t : incomingTasks) {
            Date nextTimeAfter = t.nextTimeAfter(start);
            while (nextTimeAfter != null && (nextTimeAfter.before(end) || nextTimeAfter.equals(end))) {
                if (calendar.containsKey(nextTimeAfter)) {
                    calendar.get(nextTimeAfter).add(t);
                } else {
                    HashSet<Task> oneDateTasks = new HashSet<>();
                    oneDateTasks.add(t);
                    calendar.put(nextTimeAfter, oneDateTasks);
                }
                nextTimeAfter = t.nextTimeAfter(nextTimeAfter);
            }
        }
        return calendar;
    }
}
