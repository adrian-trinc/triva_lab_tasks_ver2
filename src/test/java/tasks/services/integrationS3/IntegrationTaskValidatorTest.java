package tasks.services.integrationS3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tasks.model.Task;
import tasks.model.TaskValidator;
import tasks.model.ValidationException;
import tasks.repository.TaskFileRepository;
import tasks.services.TaskServiceV2;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegrationTaskValidatorTest {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final String FILE_PATH = "./src/main/java/tasks/datatest/tasksmockito.txt";
    private TaskValidator taskValidator;
    private TaskFileRepository taskFileRepository;
    private TaskServiceV2 taskServiceV2;

    @BeforeEach
    void setUp() {
        try {
            new PrintWriter(FILE_PATH).close();
            this.taskValidator = new TaskValidator();
            this.taskFileRepository = new TaskFileRepository(this.taskValidator, FILE_PATH);
            this.taskServiceV2 = new TaskServiceV2(this.taskFileRepository);
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
            assertEquals(0, this.taskServiceV2.getAll().size());
            this.taskServiceV2.addTask(task1);
            assertEquals(1, this.taskServiceV2.getAll().size());
            this.taskServiceV2.addTask(task2);
            assertEquals(2, this.taskServiceV2.getAll().size());
            this.taskServiceV2.addTask(task3);
            assertEquals(3, this.taskServiceV2.getAll().size());
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
            assertThrows(ValidationException.class, () -> taskServiceV2.addTask(task1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
