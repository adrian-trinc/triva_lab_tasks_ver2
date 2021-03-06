package tasks.model;

import org.apache.log4j.Logger;
import tasks.repository.TaskIO;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Task implements Serializable {
    private Long taskID;
    private String title;
    private Date time;
    private Date start;
    private Date end;
    private int interval;
    private boolean active;

    private static final Logger log = Logger.getLogger(Task.class.getName());
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public SimpleDateFormat getDateFormat() {
        return sdf;
    }

    public Task(Long taskID, String title, Date time) {
        this.taskID = taskID;
        if (time.getTime() < 0) {
            log.error("time below bound");
            throw new IllegalArgumentException("Time cannot be negative");
        }
        this.title = title;
        this.time = time;
        this.start = time;
        this.end = time;
    }

    public Task(Long taskID, String title, Date start, Date end, int interval) {
        this.taskID = taskID;
        if (start.getTime() < 0 || end.getTime() < 0) {
            log.error("time below bound");
            throw new IllegalArgumentException("Time cannot be negative");
        }
        if (interval < 1) {
            log.error("interval < than 1");
            throw new IllegalArgumentException("interval should me > 1");
        }
        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.time = start;
    }

    public Long getTaskID() {
        return taskID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
        this.start = time;
        this.end = time;
        this.interval = 0;
    }

    public Date getStartTime() {
        return start;
    }

    public Date getEndTime() {
        return end;
    }

    public int getRepeatInterval() {
        return interval > 0 ? interval : 0;
    }

    public void setTime(Date start, Date end, int interval) {
        this.time = start;
        this.start = start;
        this.end = end;
        this.interval = interval;
    }

    public boolean isRepeated() {
        return this.interval != 0;
    }

    public Date nextTimeAfter(Date current) {
        if (current.after(end) || current.equals(end)) return null;
        if (isRepeated() && isActive()) {
            Date timeBefore = start;
            Date timeAfter = start;
            if (current.before(start)) {
                return start;
            }
            if (current.equals(timeAfter))
                return new Date(timeAfter.getTime() + interval * 1000);
            if (current.after(timeBefore) && current.before(timeAfter))
                return timeBefore;//return timeAfter
        }
        if (!isRepeated() && current.before(time) && isActive()) {
            return time;
        }
        return null;
    }

    public Date nextTimeAfterOrEqual(Date current) { // 1
        if (current.after(end))                      // 2
            return null;                             // 3
        if (current.equals(end))                     // 4
            return end;                              // 5
        if (isRepeated()) {                          // 6
            if (current.before(start) || current.equals(start)) { // 7
                return start;                                     // 8
            }                                                     // 9
            Date timeAfter = new Date(start.getTime() + interval * 1000);       // 10
            while (timeAfter.before(end) || timeAfter.equals(end)) {            // 11
                if (timeAfter.after(current) || timeAfter.equals(current)) {    // 12
                    return timeAfter;                                           // 13
                }                                                               // 14
                timeAfter = new Date(timeAfter.getTime() + interval * 1000);    // 15
            }                                                                   // 16
        } else {                                                                // 17
            if (current.before(time) || current.equals(time)) {                 // 18
                return time;                                                    // 19
            }                                                                   // 20
        }                                                                       // 21
        return null;                                                            // 22
    }                                                                           // 23

    //duplicate methods for TableView which sets column
    // value by single method and doesn't allow passing parameters
    public String getFormattedDateStart() {
        return sdf.format(start);
    }

    public String getFormattedDateEnd() {
        return sdf.format(end);
    }

    public String getFormattedRepeated() {
        if (isRepeated()) {
            String formattedInterval = TaskIO.getFormattedInterval(interval);
            return "Every " + formattedInterval;
        } else {
            return "No";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!time.equals(task.time)) return false;
        if (!start.equals(task.start)) return false;
        if (!end.equals(task.end)) return false;
        if (interval != task.interval) return false;
        if (active != task.active) return false;
        return title.equals(task.title);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + interval;
        result = 31 * result + (active ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", time=" + time +
                ", start=" + start +
                ", end=" + end +
                ", interval=" + interval +
                ", active=" + active +
                '}';
    }

    public Task(Task task) {
        this.taskID = task.getTaskID();
        this.title = task.getTitle();
        this.time = task.getTime();
        this.start = task.getStartTime();
        this.end = task.getEndTime();
        this.interval = task.getRepeatInterval();
        this.active = task.isActive();
    }
}


