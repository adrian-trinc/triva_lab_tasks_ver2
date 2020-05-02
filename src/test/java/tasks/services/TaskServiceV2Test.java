package tasks.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tasks.model.LinkedTaskList;
import tasks.model.Task;
import tasks.model.TaskList;
import tasks.model.ValidationException;
import tasks.repository.TaskFileRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceV2Test {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private TaskFileRepository taskFileRepository;
    private TaskServiceV2 taskServiceV2;

    @BeforeEach
    void setUp() {
        this.taskFileRepository = mock(TaskFileRepository.class);
        this.taskServiceV2 = new TaskServiceV2(this.taskFileRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAddTaskResultNormal() {
        TaskList taskList = new LinkedTaskList();
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/25 12:22:00");
            Task task1 = new Task(1L, "Task1", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Task task2 = new Task(2L, "Task2", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Task task3 = new Task(3L, "Task3", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Mockito.when(taskFileRepository.getAll()).thenReturn(taskList);
            Mockito.doAnswer(invocationOnMock -> {
                taskList.add(task1);
                return null;
            }).when(taskFileRepository).addTask(task1);
            Mockito.doAnswer(invocationOnMock -> {
                taskList.add(task2);
                return null;
            }).when(taskFileRepository).addTask(task2);
            Mockito.doAnswer(invocationOnMock -> {
                taskList.add(task3);
                return null;
            }).when(taskFileRepository).addTask(task3);
            taskServiceV2.addTask(task1);
            Mockito.verify(taskFileRepository, times(1)).addTask(task1);
            assertEquals(1, taskServiceV2.getAll().size());
            taskServiceV2.addTask(task2);
            Mockito.verify(taskFileRepository, times(1)).addTask(task2);
            assertEquals(2, taskServiceV2.getAll().size());
            taskServiceV2.addTask(task3);
            Mockito.verify(taskFileRepository, times(1)).addTask(task3);
            assertEquals(3, taskServiceV2.getAll().size());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAddTaskResultValidationException() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/25 12:22:00");
            Task task1 = new Task(1L, "Task1;", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Mockito.doThrow(ValidationException.class).when(taskFileRepository).addTask(task1);
            assertThrows(ValidationException.class, () -> taskServiceV2.addTask(task1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}