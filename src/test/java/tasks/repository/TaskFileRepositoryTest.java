package tasks.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tasks.model.Task;
import tasks.model.TaskValidator;
import tasks.model.ValidationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskFileRepositoryTest {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final String FILE_PATH = "./src/main/java/tasks/datatest/tasksmockito.txt";
    private TaskValidator taskValidator;
    private TaskFileRepository taskFileRepository;

    @BeforeEach
    void setUp() {
        try {
            new PrintWriter(FILE_PATH).close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.taskValidator = mock(TaskValidator.class);
        try {
            this.taskFileRepository = new TaskFileRepository(this.taskValidator, FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAddTaskResultNormal() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/25 12:22:00");
            Task task1 = new Task(1L, "Task1", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Task task2 = new Task(2L, "Task2", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Task task3 = new Task(3L, "Task3", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Mockito.doNothing().when(this.taskValidator).validate(task1);
            Mockito.doNothing().when(this.taskValidator).validate(task2);
            Mockito.doNothing().when(this.taskValidator).validate(task3);
            assertEquals(0, this.taskFileRepository.getAll().size());
            this.taskFileRepository.addTask(task1);
            assertEquals(1, this.taskFileRepository.getAll().size());
            this.taskFileRepository.addTask(task2);
            assertEquals(2, this.taskFileRepository.getAll().size());
            this.taskFileRepository.addTask(task3);
            assertEquals(3, this.taskFileRepository.getAll().size());
            Mockito.verify(this.taskValidator, times(1)).validate(task1);
            Mockito.verify(this.taskValidator, times(1)).validate(task2);
            Mockito.verify(this.taskValidator, times(1)).validate(task3);
        } catch (ParseException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    void testAddTaskResultValidationException() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/25 12:22:00");
            Task task1 = new Task(1L, "Task1;", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Mockito.doThrow(ValidationException.class).when(taskValidator).validate(task1);
            assertThrows(ValidationException.class, () -> taskValidator.validate(task1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Disabled
    void getAll() {
    }
}