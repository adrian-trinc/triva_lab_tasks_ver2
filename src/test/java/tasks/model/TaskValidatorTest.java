package tasks.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskValidatorTest {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private TaskValidator taskValidator;

    @BeforeEach
    void setUp() {
        this.taskValidator = new TaskValidator();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testValidateResultNormal() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/25 12:22:00");
            Task task1 = new Task(1L, "Task1", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Task task2 = new Task(2L, "Task2", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Task task3 = new Task(3L, "Task3", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            this.taskValidator.validate(task1);
            this.taskValidator.validate(task2);
            this.taskValidator.validate(task3);
            assert true;
        } catch (ParseException | ValidationException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    void testValidateResultValidationException() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/25 12:22:00");
            Task task1 = new Task(1L, "Task1;", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            assertThrows(ValidationException.class, () -> taskValidator.validate(task1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}