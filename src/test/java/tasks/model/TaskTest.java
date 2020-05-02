package tasks.model;

import org.junit.jupiter.api.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Disabled
    void nextTimeAfter() {
    }

    @Test
    @Tag("TC01")
    void nextTimeAfterOrEqualF02P01() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/25 12:22:00");
            Task task1 = new Task(1L, "Task1", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Date currentDate = dateFormat.parse("2012/12/27 08:30:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            assertNull(dateResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC02")
    void nextTimeAfterOrEqualF02P02() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/25 12:22:00");
            Task task1 = new Task(1L, "Task1", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Date currentDate = dateFormat.parse("2012/12/25 12:22:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            Date expectedResult = dateFormat.parse("2012/12/25 12:22:00");
            assertNotNull(dateResult);
            assertEquals(dateResult, expectedResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC03")
    void nextTimeAfterOrEqualF02P03Cond9TCond10F() {
        try {
            Date date = dateFormat.parse("2012/12/21 08:00:00");
            Task task1 = new Task(1L, "Task1", date);
            assertFalse(task1.isRepeated());
            Date currentDate = dateFormat.parse("2012/12/02 08:00:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            Date expectedResult = dateFormat.parse("2012/12/21 08:00:00");
            assertNotNull(dateResult);
            assertEquals(dateResult, expectedResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC04")
    void nextTimeAfterOrEqualF02P04Cond9FCond10F() {
        try {
            Date date = dateFormat.parse("2012/12/21 08:00:00");
            Task task1 = new Task(1L, "Task1", date);
            assertFalse(task1.isRepeated());
            Date currentDate = dateFormat.parse("2012/12/25 08:00:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            assertNull(dateResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC05")
    void nextTimeAfterOrEqualF02P05Cond3TCond4F() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/25 12:22:00");
            Task task1 = new Task(1L, "Task1", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Date currentDate = dateFormat.parse("2012/12/19 08:30:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            Date expectedResult = dateFormat.parse("2012/12/21 08:00:00");
            assertNotNull(dateResult);
            assertEquals(dateResult, expectedResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC06")
    void nextTimeAfterOrEqualF02P06Cond3FCond4FCond5TCond6FCond7TCond8F() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/25 12:22:00");
            Task task1 = new Task(1L, "Task1", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Date currentDate = dateFormat.parse("2012/12/21 08:30:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            Date expectedResult = dateFormat.parse("2012/12/21 09:00:00");
            assertNotNull(dateResult);
            assertEquals(dateResult, expectedResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC07")
    void nextTimeAfterOrEqualF02P07Cond3FCond4FCond5TCond6FCond7TFCond8F() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/21 09:55:00");
            Task task1 = new Task(1L, "Task1", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Date currentDate = dateFormat.parse("2012/12/21 09:30:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            assertNull(dateResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC08")
    void nextTimeAfterOrEqualF02P08Cond3FCond4FCond5FCond6F() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/21 08:35:00");
            Task task1 = new Task(1L, "Task1", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Date currentDate = dateFormat.parse("2012/12/27 08:30:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            assertNull(dateResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC09")
    void nextTimeAfterOrEqualF02P05Cond3FCond4T() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/25 12:22:00");
            Task task1 = new Task(1L, "Task1", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Date currentDate = dateFormat.parse("2012/12/21 08:00:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            Date expectedResult = dateFormat.parse("2012/12/21 08:00:00");
            assertNotNull(dateResult);
            assertEquals(dateResult, expectedResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC10")
    void nextTimeAfterOrEqualF02P06Cond3FCond4FCond5TCond6FCond7FCond8T() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/21 09:45:00");
            Task task1 = new Task(1L, "Task1", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Date currentDate = dateFormat.parse("2012/12/21 09:00:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            Date expectedResult = dateFormat.parse("2012/12/21 09:00:00");
            assertNotNull(dateResult);
            assertEquals(dateResult, expectedResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC11")
    void nextTimeAfterOrEqualF02LoopNeverRuns() {
        try {
            Date date1 = dateFormat.parse("2020/04/12 08:00:00");
            Task task1 = new Task(1L, "Loop", date1);
            Date currentDate = dateFormat.parse("2020/04/12 12:00:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            assertNull(dateResult);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC12")
    void nextTimeAfterOrEqualF02LoopRunsOnce() {
        try {
            Date date1 = dateFormat.parse("2020/04/12 12:00:00");
            Date date2 = dateFormat.parse("2020/04/12 13:30:00");
            Task task1 = new Task(1L, "Loop", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Date currentDate = dateFormat.parse("2020/04/12 12:45:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            Date expectedResult = dateFormat.parse("2020/04/12 13:00:00");
            assertNotNull(dateResult);
            assertEquals(dateResult, expectedResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC13")
    void nextTimeAfterOrEqualF02LoopsRunsTwice() {
        try {
            Date date1 = dateFormat.parse("2020/04/12 12:00:00");
            Date date2 = dateFormat.parse("2020/04/12 14:45:00");
            Task task1 = new Task(1L, "Loop", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Date currentDate = dateFormat.parse("2020/04/12 13:45:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            Date expectedResult = dateFormat.parse("2020/04/12 14:00:00");
            assertNotNull(dateResult);
            assertEquals(dateResult, expectedResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC14")
    void nextTimeAfterOrEqualF02MaxLoopIterations() {
        try {
            Date date1 = dateFormat.parse("2020/04/12 12:00:00");
            Date date2 = dateFormat.parse("2020/04/15 12:00:00");
            Task task1 = new Task(1L, "Loop", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Date currentDate = dateFormat.parse("2020/04/15 12:30:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            assertNull(dateResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("TC15")
    void nextTimeAfterOrEqualF02MaxMinusOneLoopIterations() {
        try {
            Date date1 = dateFormat.parse("2020/04/12 12:00:00");
            Date date2 = dateFormat.parse("2020/04/15 12:00:00");
            Task task1 = new Task(1L, "Loop", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            Date currentDate = dateFormat.parse("2020/04/12 10:30:00");
            Date dateResult = task1.nextTimeAfterOrEqual(currentDate);
            Date expectedResult = dateFormat.parse("2020/04/12 12:00:00");
            assertNotNull(dateResult);
            assertEquals(dateResult, expectedResult);
        } catch (ParseException e) {
                e.printStackTrace();
        }
    }

    @Test
    @Tag("TC16Lab04")
    void setTitleNormal() {
        try {
            Date date1 = dateFormat.parse("2012/12/21 08:00:00");
            Date date2 = dateFormat.parse("2012/12/25 12:22:00");
            Task task1 = new Task(1L, "Task1", date1, date2, (int) TimeUnit.HOURS.toSeconds(1));
            assertEquals(task1.getTitle(),"Task1");
            task1.setTitle("Task1V2");
            assertEquals(task1.getTitle(),"Task1V2");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}