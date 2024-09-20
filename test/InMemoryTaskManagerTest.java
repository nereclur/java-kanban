package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager manager;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void shouldReturnListSize1() {
        Task task = new Task("Задача 1", "Описание задачи 1");
        manager.createTask(task);
        List<Task> tasks = manager.getAllTask();
        assertEquals(1, tasks.size());
    }

    @Test
    void shouldReturnListSize3() {
        Task task1 = new Task("Задача 1", "Описание 1");
        Task task2 = new Task("Задача 2", "Описание 2");
        Task task3 = new Task("Задача 3", "Описание 3");
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        List<Task> tasks = manager.getAllTask();
        assertEquals(3, tasks.size());
    }

    @Test
    void shouldReturnTrueIfTaskUpdated() {
        Task task = new Task("Задача 1", "Описание 1");
        manager.createTask(task);
        Task updatedTask = new Task("Обновленная задача 1", "Обновленное описание");
        manager.updateTask(updatedTask, task.getTaskId());
        Task retrievedTask = manager.getTaskById(task.getTaskId());
        assertEquals("Обновленная задача 1", retrievedTask.getName());
    }

    @Test
    void shouldReturnCountOfDeletedTask() {
        Task task = new Task("Задача 1", "Описание 1");
        manager.createTask(task);
        manager.removeTaskById(task.getTaskId());
        List<Task> tasks = manager.getAllTask();
        assertEquals(0, tasks.size());
    }

    @Test
    void shouldReturnTrueIfEpicUpdated() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.createEpic(epic);
        manager.updateEpic(epic.getTaskId(), "Обновленный Эпик 1", "Обновленное описание");
        Epic updatedEpic = manager.getEpicById(epic.getTaskId());
        assertEquals("Обновленный Эпик 1", updatedEpic.getName());
    }

    @Test
    void shouldReturnCountsOfDeletedEpics() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.createEpic(epic);
        manager.removeEpicById(epic.getTaskId());
        List<Epic> epics = manager.getAllEpics();
        assertEquals(0, epics.size());
    }

    @Test
    void shouldReturnTrueIfFoundSubtaskById() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", epic.getTaskId());
        manager.createSubtask(subtask);
        Subtask retrievedSubtask = manager.getSubtaskById(subtask.getTaskId());
        assertEquals(subtask, retrievedSubtask);
    }

    @Test
    void shouldListSizeBeZeroIfNotContainSubtasks() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", epic.getTaskId());
        manager.createSubtask(subtask);
        manager.removeSubtaskById(subtask.getTaskId());
        List<Subtask> subtasks = manager.getAllSubtask();
        assertEquals(0, subtasks.size());
    }

    @Test
    void shouldEpicStatusBeUpdatedCorrectly() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.createEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", epic.getTaskId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", epic.getTaskId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);

        subtask2.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask2);

        Epic updatedEpic = manager.getEpicById(epic.getTaskId());
        assertEquals(TaskStatus.DONE, updatedEpic.getStatus());
    }

    @Test
    void shouldContainTasksAfterReceivingThem() {
        Task task = new Task("Test Task", "Описание");
        manager.createTask(task);
        manager.getTaskById(task.getTaskId());
        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task.getTaskId(), history.get(0).getTaskId());
    }

    @Test
    void shouldNotContainOldSubtaskIdsAfterRemoval() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", epic.getTaskId());
        manager.createSubtask(subtask);
        manager.removeSubtaskById(subtask.getTaskId());

        Epic updatedEpic = manager.getEpicById(epic.getTaskId());
        assertTrue(updatedEpic.getSubtasks().isEmpty(),
                "Эпик не должен содержать старые id сабтасок после их удаления");
    }

    @Test
    void shouldUpdateEpicStatusWhenSubtaskRemoved() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", epic.getTaskId());
        manager.createSubtask(subtask);
        manager.removeSubtaskById(subtask.getTaskId());

        Epic updatedEpic = manager.getEpicById(epic.getTaskId());
        assertEquals(TaskStatus.NEW, updatedEpic.getStatus(),
                "Статус эпика должен обновиться после удаление задачи");
    }


    @Test
    void shouldMaintainHistoryIntegrityAfterTaskUpdate() {
        Task task = new Task("Test Task", "Описание");
        manager.createTask(task);
        manager.getTaskById(task.getTaskId());

        Task updatedTask = new Task("Updated Task", "Новое описание");
        manager.updateTask(updatedTask, task.getTaskId());

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(updatedTask.getTaskId(), history.get(0).getTaskId(),
                "История должна содержать таску с обновленным описанием задачи");
    }

    @Test
    public void shouldNotDuplicateTasksInHistory() {
        TaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = new InMemoryHistoryManager();

        Task task = new Task("Test Task", "Описание", 1);
        taskManager.addTask(task);

        taskManager.getTaskById(task.getTaskId());
        taskManager.getTaskById(task.getTaskId()); /

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }


    @Test
    void shouldRemoveTaskFromHistory() {
        Task task = new Task("Test Task", "Описание");
        manager.createTask(task);
        manager.getTaskById(task.getTaskId());
        manager.removeTaskById(task.getTaskId());

        List<Task> history = manager.getHistory();
        assertFalse(history.contains(task), "Удаленные таски не должны быть в истории");
    }

    @Test
    void shouldMaintainUniqueTaskIdsInHistory() {
        Task task1 = new Task("Test Task 1", "Описание 1");
        Task task2 = new Task("Test Task 2", "Описание 2");
        manager.createTask(task1);
        manager.createTask(task2);
        manager.getTaskById(task1.getTaskId());
        manager.getTaskById(task2.getTaskId());

        List<Task> history = manager.getHistory();
        assertEquals(2, history.size(), "История должна содержать обе таски");

        int taskId1 = task1.getTaskId();
        int taskId2 = task2.getTaskId();
        long uniqueTaskIds = history.stream()
                .mapToInt(Task::getTaskId)
                .distinct()
                .count();
        assertEquals(2, uniqueTaskIds, "История должна содержать обе таски с уникальными id");
    }
}
