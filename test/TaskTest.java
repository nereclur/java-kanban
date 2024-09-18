package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    void shouldReturnTaskId1() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Купит хулеб", "Пойдит в мазин купит лепешка");
        manager.createTask(task);
        int taskId = task.getTaskId();
        assertEquals(1, taskId);
    }

    @Test
    void shouldReturnTaskId3() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task2 = new Task("Бегит", "бегит кругом дома");
        Task task3 = new Task("Анжуманя", "анжуманя делат болшой руки");
        manager.createTask(task2);
        manager.createTask(task3);
        int task3Id = task3.getTaskId();
        assertEquals(3, task3Id);
    }

    @Test
    void shouldReturnTrueIfTasksEquals() {
        Task task = new Task("Купит хулеб", "Пойдит в мазин купит лепешка");
        Task task1 = new Task("Купит хулеб", "Пойдит в мазин купит лепешка");
        task.setTaskId(1);
        task1.setTaskId(1);
        Assertions.assertTrue(task.equals(task1));
    }

    @Test
    void shouldBeNEWStatus() {
        Task task = new Task("Купит хулеб", "Пойдит в мазин купит лепешка");
        Assertions.assertEquals(TaskStatus.NEW, task.getStatus());
    }
}