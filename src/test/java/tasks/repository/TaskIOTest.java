package tasks.repository;

import org.junit.jupiter.api.*;
import tasks.model.LinkedTaskList;
import tasks.model.Task;
import tasks.model.TaskList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskIOTest {
    private String FILE_PATH = "./src/main/java/tasks/datatest/tasks.txt";
    private String FILE_PATH_DOES_NOT_EXIST = "./src/main/java/tasks/datatest/doesnotexist.txt";
    private TaskList taskList;
    private File file;

    @BeforeEach
    void setUp() {
        try {
            new PrintWriter(FILE_PATH).close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @RepeatedTest(value = 1, name = "{displayName} {currentRepetition}/{totalRepetitions}")
    @Order(1)
    @DisplayName("writeBWSuccessEquivalenceClass1TaskListWith3TasksAndFileExists")
    @Tag("EquivalenceClass1")
    void writeBWSuccessEquivalenceClass1TaskListWith3TasksAndFileExists() {
        taskList = new LinkedTaskList();
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.HOUR, 1);
        Date currentDatePlusOne = c.getTime();
        long diff = TimeUnit.SECONDS.convert(Math.abs(currentDatePlusOne.getTime() - currentDate.getTime()), TimeUnit.MILLISECONDS);
        Task task1 = new Task(1L, "Task1", currentDate, currentDatePlusOne, (int) diff);
        Task task2 = new Task(2L, "Task2", currentDate, currentDatePlusOne, (int) diff);
        Task task3 = new Task(3L, "Task3", currentDate, currentDatePlusOne, (int) diff);
        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);
        file = new File(FILE_PATH);
        try {
            TaskList taskListBefore = new LinkedTaskList();
            assertEquals(taskListBefore.size(), 0);
            TaskIO.readBR(taskListBefore, file);
            assertEquals(taskListBefore.size(), 0);
            TaskIO.writeBW(taskList, file);
            TaskList taskListAfter = new LinkedTaskList();
            assertEquals(taskListAfter.size(), 0);
            TaskIO.readBR(taskListAfter, file);
            assertEquals(taskListAfter.size(), 3);
            Task task1After = taskListAfter.getTask(0);
            Task task2After = taskListAfter.getTask(1);
            Task task3After = taskListAfter.getTask(2);
            assertEquals(task1After.getTitle(), "Task1");
            assertEquals(task2After.getTitle(), "Task2");
            assertEquals(task3After.getTitle(), "Task3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    @DisplayName("writeBW success EquivalenceClass1 TaskList with 1 task and file exists")
    @Tag("EquivalenceClass1")
    void writeBWSuccessEquivalenceClass1TaskListWith1TaskAndFileExists() {
        taskList = new LinkedTaskList();
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.HOUR, 1);
        Date currentDatePlusOne = c.getTime();
        long diff = TimeUnit.SECONDS.convert(Math.abs(currentDatePlusOne.getTime() - currentDate.getTime()), TimeUnit.MILLISECONDS);
        Task task1 = new Task(1L, "Task1", currentDate, currentDatePlusOne, (int) diff);
        taskList.add(task1);
        file = new File(FILE_PATH);
        try {
            TaskList taskListBefore = new LinkedTaskList();
            assertEquals(taskListBefore.size(), 0);
            TaskIO.readBR(taskListBefore, file);
            assertEquals(taskListBefore.size(), 0);
            TaskIO.writeBW(taskList, file);
            TaskList taskListAfter = new LinkedTaskList();
            assertEquals(taskListAfter.size(), 0);
            TaskIO.readBR(taskListAfter, file);
            assertEquals(taskListAfter.size(), 1);
            Task task1After = taskListAfter.getTask(0);
            assertEquals(task1After.getTitle(), "Task1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    @DisplayName("writeBWFailureEquivalenceClass2TaskListNullAndFileExists")
    @Tag("EquivalenceClass2")
    void writeBWFailureEquivalenceClass2TaskListNullAndFileExists() {
        taskList = null;
        file = new File(FILE_PATH);
        try {
            TaskList taskListBefore = new LinkedTaskList();
            assertEquals(taskListBefore.size(), 0);
            TaskIO.readBR(taskListBefore, file);
            assertEquals(taskListBefore.size(), 0);
            try {
                TaskIO.writeBW(taskList, file);
                assert false;
            } catch (IllegalArgumentException e) {
                assert true;
            }
            TaskList taskListAfter = new LinkedTaskList();
            assertEquals(taskListAfter.size(), 0);
            TaskIO.readBR(taskListAfter, file);
            assertEquals(taskListAfter.size(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(4)
    @DisplayName("writeBW failure EquivalenceClass3 TaskList with 3 tasks and file does not exist")
    @Tag("EquivalenceClass3")
    void writeBWFailureEquivalenceClass3TaskListWith3TasksAndFileDoesNotExist() {
        taskList = new LinkedTaskList();
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.HOUR, 1);
        Date currentDatePlusOne = c.getTime();
        long diff = TimeUnit.SECONDS.convert(Math.abs(currentDatePlusOne.getTime() - currentDate.getTime()), TimeUnit.MILLISECONDS);
        Task task1 = new Task(1L, "Task1", currentDate, currentDatePlusOne, (int) diff);
        Task task2 = new Task(2L, "Task2", currentDate, currentDatePlusOne, (int) diff);
        Task task3 = new Task(3L, "Task3", currentDate, currentDatePlusOne, (int) diff);
        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);
        file = new File(FILE_PATH_DOES_NOT_EXIST);
        try {
            try {
                TaskIO.writeBW(taskList, file);
                assert false;
            } catch (IllegalArgumentException e) {
                assert true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(5)
    @DisplayName("writeBW success BVA TaskList with 0 tasks and file exists")
    @Tag("BVA1")
    void writeBWSuccessBVA1TaskListWith0TasksAndFileExists() {
        taskList = new LinkedTaskList();
        file = new File(FILE_PATH);
        try {
            TaskList taskListBefore = new LinkedTaskList();
            assertEquals(taskListBefore.size(), 0);
            TaskIO.readBR(taskListBefore, file);
            assertEquals(taskListBefore.size(), 0);

            TaskIO.writeBW(taskList, file);

            TaskList taskListAfter = new LinkedTaskList();
            assertEquals(taskListAfter.size(), 0);
            TaskIO.readBR(taskListAfter, file);
            assertEquals(taskListAfter.size(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(6)
    @DisplayName("writeBW success BVA TaskList with 1 task and file exists")
    @Tag("BVA2")
    void writeBWSuccessBVA2TaskListWith1TaskAndFileExists() {
        taskList = new LinkedTaskList();
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.HOUR, 5);
        Date currentDatePlusOne = c.getTime();
        long diff = TimeUnit.SECONDS.convert(Math.abs(currentDatePlusOne.getTime() - currentDate.getTime()), TimeUnit.MILLISECONDS);
        Task task1 = new Task(1L, "Task1", currentDate, currentDatePlusOne, (int) diff);
        taskList.add(task1);
        file = new File(FILE_PATH);
        try {
            TaskList taskListBefore = new LinkedTaskList();
            assertEquals(taskListBefore.size(), 0);
            TaskIO.readBR(taskListBefore, file);
            assertEquals(taskListBefore.size(), 0);

            TaskIO.writeBW(taskList, file);

            TaskList taskListAfter = new LinkedTaskList();
            assertEquals(taskListAfter.size(), 0);
            TaskIO.readBR(taskListAfter, file);
            assertEquals(taskListAfter.size(), 1);

            Task task1After = taskListAfter.getTask(0);
            assertEquals(task1After.getTitle(), "Task1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(7)
    @DisplayName("writeBW failure BVA TaskList with 5 tasks and file does not exist")
    @Tag("BVA3")
    void writeBWSuccessBVA3TaskListWith5TasksAndFileDoesNotExist() {
        taskList = new LinkedTaskList();
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.HOUR, 5);
        Date currentDatePlusOne = c.getTime();
        long diff = TimeUnit.SECONDS.convert(Math.abs(currentDatePlusOne.getTime() - currentDate.getTime()), TimeUnit.MILLISECONDS);
        Task task1 = new Task(1L, "Task1", currentDate, currentDatePlusOne, (int) diff);
        Task task2 = new Task(2L, "Task2", currentDate, currentDatePlusOne, (int) diff);
        Task task3 = new Task(3L, "Task3", currentDate, currentDatePlusOne, (int) diff);
        Task task4 = new Task(4L, "Task4", currentDate, currentDatePlusOne, (int) diff);
        Task task5 = new Task(5L, "Task5", currentDate, currentDatePlusOne, (int) diff);
        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);
        taskList.add(task4);
        taskList.add(task5);
        file = new File(FILE_PATH_DOES_NOT_EXIST);
        try {
            try {
                TaskIO.writeBW(taskList, file);
                assert false;
            } catch (IllegalArgumentException e) {
                assert true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Disabled
    void rewriteFileBW() {
    }
}