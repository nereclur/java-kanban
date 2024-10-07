package managers;

import exceptions.ManagerLoadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    private FileBackedTaskManager taskManager;
    private File file;

    @BeforeEach
    void init() throws IOException {
        file = File.createTempFile("backup", "csv");
        taskManager = Managers.getFileBackedTaskManager(file);
    }

    @Test
    void save_shouldSaveEmptyFile() throws IOException {
        taskManager.deleteAllTasks();
        assertTrue(Files.readString(file.toPath()).isEmpty(), "Файл должен быть пустым");
    }

    @Test
    void save_shouldLoadEmptyFile() throws IOException, ManagerLoadException {
        taskManager.deleteAllTasks();
        FileBackedTaskManager newManager = Managers.getFileBackedTaskManager(file);
        newManager.loadFromFile(file);

        assertTrue(newManager.getAllTasks().isEmpty(), "Список задач должен быть пустым");
        assertTrue(newManager.getAllEpic().isEmpty(), "Список эпиков должен быть пустым");
        assertTrue(newManager.getAllSubtasks().isEmpty(), "Список подзадач должен быть пустым");
    }

    @Test
    void save_shouldSaveFewTasksInFile() throws IOException {
        Task task = new Task("Task 1", "Task Description");
        Epic epic = new Epic("Epic 1", "Epic Description");

        Task actualTask = taskManager.addNewTask(task);
        Epic actualEpic = taskManager.addNewTask(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", epic.getId());
        Subtask actualSub = taskManager.addNewTask(subtask);
        String fileContent = Files.readString(file.toPath());

        assertTrue(fileContent.contains(actualTask.getDescription()), "Файл должен содержать задачу");
        assertTrue(fileContent.contains(actualEpic.getDescription()), "Файл должен содержать эпик");
        assertTrue(fileContent.contains(actualSub.getDescription()), "Файл должен содержать подзадачу");
    }

    @Test
    void save_shouldLoadTasks() throws IOException, ManagerLoadException {
        Task task = new Task("Task 1", "Task Description");
        Epic epic = new Epic("Epic 1", "Epic Description");

        Task actualTask = taskManager.addNewTask(task);
        Epic actualEpic = taskManager.addNewTask(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", epic.getId());
        Subtask actualSub = taskManager.addNewTask(subtask);

        FileBackedTaskManager manager = taskManager.loadFromFile(file);

        Task loadTask = manager.getTask(actualTask.getId());
        Epic loadEpic = manager.getEpic(actualEpic.getId());
        Subtask loadSub = manager.getSubtask(actualSub.getId());

        assertEquals(actualTask, loadTask, "Задачи должны совпадать");
        assertEquals(actualEpic, loadEpic, "Эпики должны совпадать");
        assertEquals(actualSub, loadSub, "Подзадачи должны совпадать");
    }

    @Test
    public void shouldNoOverlapBetweenTasks() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setStartTime(LocalDateTime.of(2024, 10, 7, 10, 0));
        task1.setDuration(Duration.ofHours(1));

        Task task2 = new Task("Task 2", "Description 2");
        task2.setStartTime(LocalDateTime.of(2024, 10, 7, 12, 0));
        task2.setDuration(Duration.ofHours(1));

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        assertFalse(taskManager.hasTimeConflicts(task2), "В задачах не должно быть пересечений.");
    }

    @Test
    public void shouldNoOverlapWithDifferentTimes() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setStartTime(LocalDateTime.of(2024, 10, 7, 10, 0));
        task1.setDuration(Duration.ofHours(2));

        Task task2 = new Task("Task 2", "Description 2");
        task2.setStartTime(LocalDateTime.of(2024, 10, 7, 8, 0));
        task2.setDuration(Duration.ofHours(1));

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);

        assertFalse(taskManager.hasTimeConflicts(task2), "В задачах не должно быть пересечений.");
    }
}


