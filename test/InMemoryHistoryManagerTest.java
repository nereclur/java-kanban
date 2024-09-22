import manager.InMemoryHistoryManager;
import task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void addTaskToHistoryTest() {
        Task task = new Task("Task 1", "Description 1");
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Должна содержать одну задачу");
        assertEquals(task, history.get(0), "Должна ровняться");
    }

    @Test
    void addMultipleTasksToHistoryTest() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        task1.setTaskId(1);
        historyManager.add(task1);
        task2.setTaskId(2);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Должна содержать две задачи");
        assertEquals(task1, history.get(0), "Должна быть task1");
        assertEquals(task2, history.get(1), "Должна быть task2");
    }

    @Test
    void removeTaskFromHistoryTest() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        task1.setTaskId(1);
        task2.setTaskId(2);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Должна содержать одну задачу после удаления");
        assertEquals(task2, history.get(0), "Должна быть task2");
    }

    @Test
    void removeNonExistentTaskFromHistoryTest() {
        Task task1 = new Task("Task 1", "Description 1");

        historyManager.add(task1);
        historyManager.remove(999);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Должна содержать одну задачу после попытки удалить");
        assertEquals(task1, history.get(0), "Должна остаться без изменений");
    }

    @Test
    void addDuplicateTaskToHistoryTest() {
        Task task = new Task("Task 1", "Description 1");

        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Дубликаты не должны добавляться");
    }
}