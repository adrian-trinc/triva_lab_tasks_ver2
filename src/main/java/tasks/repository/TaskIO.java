package tasks.repository;


import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import tasks.model.LinkedTaskList;
import tasks.model.Task;
import tasks.model.TaskList;
import tasks.services.TasksService;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("DuplicatedCode")
public class TaskIO {
    private static TaskIO singleInstance = null;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]");

    private static final int SECONDS_IN_DAY = 86400;
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MIN = 60;
    private static final String[] TIME_ENTITY = {" day", " hour", " minute", " second"};
    private static long lastTaskID = 0;
    private static final String IO_EXCEPTION_MESSAGE = "IO exception reading or writing file";

    private static final Logger log = Logger.getLogger(TaskIO.class.getName());

    private TaskIO() {
    }

    public static TaskIO getInstance() {
        if (singleInstance == null) {
            singleInstance = new TaskIO();
        }
        return singleInstance;
    }

    public static void write(TaskList tasks, OutputStream out) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(out)) {
            dataOutputStream.writeInt(tasks.size());
            for (Task t : tasks) {
                dataOutputStream.writeInt(t.getTitle().length());
                dataOutputStream.writeUTF(t.getTitle());
                dataOutputStream.writeBoolean(t.isActive());
                dataOutputStream.writeInt(t.getRepeatInterval());
                if (t.isRepeated()) {
                    dataOutputStream.writeLong(t.getStartTime().getTime());
                    dataOutputStream.writeLong(t.getEndTime().getTime());
                } else {
                    dataOutputStream.writeLong(t.getTime().getTime());
                }
            }
        }
    }

    public static void writeBW(TaskList tasks, File file) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getPath()))) {
            bufferedWriter.write(Integer.toString(tasks.size()));
            bufferedWriter.newLine();
            for (Task t : tasks) {
                bufferedWriter.write(t.getTitle());
                bufferedWriter.write(";");
                bufferedWriter.write(Boolean.toString(t.isActive()));
                bufferedWriter.write(";");
                bufferedWriter.write(Integer.toString(t.getRepeatInterval()));
                bufferedWriter.write(";");
                if (t.isRepeated()) {
                    bufferedWriter.write(Long.toString(t.getStartTime().getTime()));
                    bufferedWriter.write(";");
                    bufferedWriter.write(Long.toString(t.getEndTime().getTime()));
                } else {
                    bufferedWriter.write(Long.toString(t.getTime().getTime()));
                }
                bufferedWriter.newLine();
            }
        }
    }

    public static void read(TaskList tasks, InputStream in) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(in)) {
            int listLength = dataInputStream.readInt();
            for (int i = 0; i < listLength; i++) {
                String title = dataInputStream.readUTF();
                boolean isActive = dataInputStream.readBoolean();
                int interval = dataInputStream.readInt();
                Date startTime = new Date(dataInputStream.readLong());
                Task taskToAdd;
                if (interval > 0) {
                    Date endTime = new Date(dataInputStream.readLong());
                    taskToAdd = new Task(lastTaskID, title, startTime, endTime, interval);
                } else {
                    taskToAdd = new Task(lastTaskID, title, startTime);
                }
                taskToAdd.setActive(isActive);
                lastTaskID++;
                tasks.add(taskToAdd);
            }
        }
    }

    public static Task toTask(String line, String caleCompletaFisier) {
        String[] fields = line.split(";");
        if (fields.length == 5) {
            if (fields[0].isEmpty()) {
                throw new CorruptFileException(caleCompletaFisier);
            }
            String title = fields[0];
            if (fields[1].isEmpty()) {
                throw new CorruptFileException(caleCompletaFisier);
            }
            boolean isActive = Boolean.getBoolean(fields[1]);
            if (fields[2].isEmpty()) {
                throw new CorruptFileException(caleCompletaFisier);
            }
            int interval = Integer.parseInt(fields[2]);
            if (fields[3].isEmpty()) {
                throw new CorruptFileException(caleCompletaFisier);
            }
            Date start = new Date(Long.parseLong(fields[3]));
            Date end = new Date(Long.parseLong(fields[4]));
            Task task = new Task(0L, title, start, end, interval);
            task.setActive(isActive);
            return task;
        } else {
            if (fields.length == 4) {
                if (fields[0].isEmpty()) {
                    throw new CorruptFileException(caleCompletaFisier);
                }
                String title = fields[0];
                if (fields[1].isEmpty()) {
                    throw new CorruptFileException(caleCompletaFisier);
                }
                boolean isActive = Boolean.getBoolean(fields[1]);
                if (fields[2].isEmpty()) {
                    throw new CorruptFileException(caleCompletaFisier);
                }
                if (fields[3].isEmpty()) {
                    throw new CorruptFileException(caleCompletaFisier);
                }
                Date start = new Date(Long.parseLong(fields[3]));
                Task task = new Task(0L, title, start);
                task.setActive(isActive);
                return task;
            } else
                throw new CorruptFileException(caleCompletaFisier);
        }
    }

    public static void readBR(TaskList tasks, File file) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getPath()))) {
            String lineFile = bufferedReader.readLine();
            log.info(lineFile);
            lineFile = bufferedReader.readLine();
            while (lineFile != null) {
                Task taskToAdd = TaskIO.toTask(lineFile, file.getPath());
                tasks.add(taskToAdd);
                lineFile = bufferedReader.readLine();
            }
        }
    }

    public static void writeBinary(TaskList tasks, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            write(tasks, fos);
        } catch (IOException e) {
            log.error(IO_EXCEPTION_MESSAGE, e);
        }
    }

    public static void readBinary(TaskList tasks, File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            read(tasks, fis);
        } catch (IOException e) {
            log.error(IO_EXCEPTION_MESSAGE);
        }
    }

    public static void write(TaskList tasks, Writer out) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(out);
        Task lastTask = tasks.getTask(tasks.size() - 1);
        for (Task t : tasks) {
            bufferedWriter.write(getFormattedTask(t));
            bufferedWriter.write(t.equals(lastTask) ? ';' : '.');
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    public static void read(TaskList tasks, Reader in) throws IOException {
        BufferedReader reader = new BufferedReader(in);
        String line;
        Task t;
        while ((line = reader.readLine()) != null) {
            t = getTaskFromString(line);
            tasks.add(t);
        }
        reader.close();

    }

    public static void writeText(TaskList tasks, File file) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            write(tasks, fileWriter);
        } catch (IOException e) {
            log.error(IO_EXCEPTION_MESSAGE);
        }

    }

    public static void readText(TaskList tasks, File file) throws IOException {
        try (FileReader fileReader = new FileReader(file)) {
            read(tasks, fileReader);
        }
    }

    //// service methods for reading
    private static Task getTaskFromString(String line) {
        boolean isRepeated = line.contains("from");//if contains - means repeated
        boolean isActive = !line.contains("inactive");//if doesnt have inactive - means active
        //Task(String title, Date time)   Task(String title, Date start, Date end, int interval)
        Task result;
        String title = getTitleFromText(line);
        if (isRepeated) {
            Date startTime = getDateFromText(line, true);
            Date endTime = getDateFromText(line, false);
            int interval = getIntervalFromText(line);
            result = new Task(lastTaskID, title, startTime, endTime, interval);
        } else {
            Date startTime = getDateFromText(line, true);
            result = new Task(lastTaskID, title, startTime);
        }
        result.setActive(isActive);
        lastTaskID++;
        return result;
    }

    private static int getIntervalFromText(String line) {
        int days;
        int hours;
        int minutes;
        int seconds;
        //[1 day 2 hours 46 minutes 40 seconds].
        //[46 minutes 40 seconds].
        //[46 minutes].
        int start = line.lastIndexOf('[');
        int end = line.lastIndexOf(']');
        String trimmed = line.substring(start + 1, end);//returns interval without brackets -> 2 hours 46 minutes
        days = trimmed.contains("day") ? 1 : 0;
        hours = trimmed.contains("hour") ? 1 : 0;
        minutes = trimmed.contains("minute") ? 1 : 0;
        seconds = trimmed.contains("second") ? 1 : 0;

        int[] timeEntities = new int[]{days, hours, minutes, seconds};
        int i = 0;
        int j = timeEntities.length - 1;// positions of timeEntities available
        while (i != 1 && j != 1) {
            if (timeEntities[i] == 0) i++;
            if (timeEntities[j] == 0) j--;
        }

        String[] numAndTextValues = trimmed.split(" ");
        for (int k = 0; k < numAndTextValues.length; k += 2) {
            timeEntities[i] = Integer.parseInt(numAndTextValues[k]);
            i++;
        }

        int result = 0;

        if (timeEntities[0] != 0) {
            result += SECONDS_IN_DAY * timeEntities[0];
        }
        if (timeEntities[1] != 0) {
            result += SECONDS_IN_HOUR * timeEntities[1];
        }
        if (timeEntities[2] != 0) {
            result += SECONDS_IN_MIN * timeEntities[2];
        }
        if (timeEntities[3] != 0) {
            result += timeEntities[3];
        }

        return result;
    }

    private static Date getDateFromText(String line, boolean isStartTime) {
        Date date = null;
        String trimmedDate; //date trimmed from whole string
        int start;
        int end;

        if (isStartTime) {
            start = line.indexOf('[');
            end = line.indexOf(']');
        } else {
            int firstRightBracket = line.indexOf(']');
            start = line.indexOf('[', firstRightBracket + 1);
            end = line.indexOf(']', firstRightBracket + 1);
        }
        trimmedDate = line.substring(start, end + 1);
        try {
            date = TaskIO.getInstance().simpleDateFormat.parse(trimmedDate);
        } catch (ParseException e) {
            log.error("date parse exception");
        }
        return date;

    }

    private static String getTitleFromText(String line) {
        int start = 1;
        int end = line.lastIndexOf('\"');
        String result = line.substring(start, end);
        result = result.replace("\"\"", "\"");
        return result;
    }

    ////service methods for writing
    private static String getFormattedTask(Task task) {
        StringBuilder result = new StringBuilder();
        String title = task.getTitle();
        if (title.contains("\"")) title = title.replace("\"", "\"\"");
        result.append("\"").append(title).append("\"");

        if (task.isRepeated()) {
            result.append(" from ");
            result.append(TaskIO.getInstance().simpleDateFormat.format(task.getStartTime()));
            result.append(" to ");
            result.append(TaskIO.getInstance().simpleDateFormat.format(task.getEndTime()));
            result.append(" every ").append("[");
            result.append(getFormattedInterval(task.getRepeatInterval()));
            result.append("]");
        } else {
            result.append(" at ");
            result.append(TaskIO.getInstance().simpleDateFormat.format(task.getStartTime()));
        }
        if (!task.isActive()) result.append(" inactive");
        return result.toString().trim();
    }

    public static String getFormattedInterval(int interval) {
        if (interval <= 0) throw new IllegalArgumentException("Interval <= 0");
        StringBuilder sb = new StringBuilder();

        int days = interval / SECONDS_IN_DAY;
        int hours = (interval - SECONDS_IN_DAY * days) / SECONDS_IN_HOUR;
        int minutes = (interval - (SECONDS_IN_DAY * days + SECONDS_IN_HOUR * hours)) / SECONDS_IN_MIN;
        int seconds = (interval - (SECONDS_IN_DAY * days + SECONDS_IN_HOUR * hours + SECONDS_IN_MIN * minutes));

        int[] time = new int[]{days, hours, minutes, seconds};
        int i = 0;
        int j = time.length - 1;
        while (time[i] == 0 || time[j] == 0) {
            if (time[i] == 0) i++;
            if (time[j] == 0) j--;
        }

        for (int k = i; k <= j; k++) {
            sb.append(time[k]);
            sb.append(time[k] > 1 ? TIME_ENTITY[k] + "s" : TIME_ENTITY[k]);
            sb.append(" ");
        }
        return sb.toString();
    }

    public static void rewriteFile(ObservableList<Task> tasksList) {
        LinkedTaskList taskList = new LinkedTaskList();
        for (Task t : tasksList) {
            taskList.add(t);
        }
        try {
            TaskIO.writeBinary(taskList, new TasksService().getSavedTasksFile());
        } catch (IOException e) {
            log.error(IO_EXCEPTION_MESSAGE);
        }
    }

    public static void rewriteFileBW(ObservableList<Task> tasksList) {
        LinkedTaskList taskList = new LinkedTaskList();
        for (Task t : tasksList) {
            taskList.add(t);
        }
        try {
            TaskIO.writeBW(taskList, new TasksService().getSavedTasksFile());
        } catch (IOException e) {
            log.error(IO_EXCEPTION_MESSAGE);
        }
    }
}
