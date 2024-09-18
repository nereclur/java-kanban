package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    
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
