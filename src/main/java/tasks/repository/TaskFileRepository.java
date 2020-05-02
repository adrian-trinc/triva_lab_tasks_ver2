package tasks.repository;

import org.apache.log4j.Logger;
import tasks.model.LinkedTaskList;
import tasks.model.Task;
import tasks.model.TaskList;
import tasks.model.TaskValidator;

import java.io.*;
import java.util.Date;
import java.util.Objects;

@SuppressWarnings({"DuplicatedCode", "SpellCheckingInspection"})
public class TaskFileRepository {
    private static final Logger log = Logger.getLogger(TaskFileRepository.class.getName());
    private File file;
    private TaskValidator taskValidator;
    private TaskList fileContent;

    public TaskFileRepository(TaskValidator taskValidator, String fileName) throws IOException {
        this.taskValidator = taskValidator;
        this.file = new File(fileName);
        this.fileContent = new LinkedTaskList();
        readBR(this.fileContent, this.file);
    }

    private Task toTask(String line, String caleCompletaFisier) {
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

    private void writeBW(TaskList tasks, File file) throws IOException {
        if (Objects.isNull(tasks)) {
            throw new IllegalArgumentException("The task list cannot be null!");
        }
        if (tasks.size() < 0) {
            throw new IllegalArgumentException("The task list size must be greater than or equal to 0!");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("The file must exist!");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getPath()))) {
            bufferedWriter.write(Integer.toString(tasks.size()));
            bufferedWriter.newLine();
            tasks.getAll().sort((x, y) -> (int) (x.getTaskID() - y.getTaskID()));
            Long prevId = null;
            for (Task t : tasks.getAll()) {
                if (t.getTaskID().equals(prevId)) {
                    throw new IllegalArgumentException();
                }

                bufferedWriter.write(t.getTitle());

                bufferedWriter.write(";");
                bufferedWriter.write(Boolean.toString(t.isActive()));
                bufferedWriter.write(";");

                bufferedWriter.write(Integer.toString(t.getRepeatInterval()));
                bufferedWriter.write(";");

                if (t.isRepeated()) {
                    if (t.getRepeatInterval() > t.getEndTime().getTime() - t.getStartTime().getTime()) {
                        throw new IllegalArgumentException();
                    }
                    bufferedWriter.write(Long.toString(t.getStartTime().getTime()));
                    bufferedWriter.write(";");
                    bufferedWriter.write(Long.toString(t.getEndTime().getTime()));
                } else {
                    bufferedWriter.write(Long.toString(t.getTime().getTime()));
                }
                bufferedWriter.newLine();
                prevId = t.getTaskID();
            }
        }
    }

    private void readBR(TaskList tasks, File file) throws IOException {
        if (!file.exists()) {
            throw new IllegalArgumentException("The file must exist!");
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getPath()))) {
            String lineFile = bufferedReader.readLine();
            log.info(lineFile);
            lineFile = bufferedReader.readLine();
            while (lineFile != null) {
                Task taskToAdd = toTask(lineFile, file.getPath());
                tasks.add(taskToAdd);
                lineFile = bufferedReader.readLine();
            }
        }
    }

    public void addTask(Task task) {
        taskValidator.validate(task);
        this.fileContent.add(task);
        try {
            writeBW(this.fileContent, this.file);
        } catch (IOException e) {
            throw new TaskFileRepositoryException(e.getMessage());
        }
    }

    public TaskList getAll() {
        return fileContent;
    }
}
