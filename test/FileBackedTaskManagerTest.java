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
        // do
        taskManager.deleteAllTasks();  // убедимся, что все задачи удалены

        // check
        assertTrue(Files.readString(file.toPath()).isEmpty(), "Файл должен быть пустым");
    }

    @Test
    void save_shouldLoadEmptyFile() throws IOException, ManagerLoadException {
        // prepare
        taskManager.deleteAllTasks();  // убедимся, что все задачи удалены

        // do
        FileBackedTaskManager newManager = Managers.getFileBackedTaskManager(file);
        newManager.loadFromFile(file);

        // check
        assertTrue(newManager.getAllTasks().isEmpty(), "Список задач должен быть пустым");
        assertTrue(newManager.getAllEpic().isEmpty(), "Список эпиков должен быть пустым");
        assertTrue(newManager.getAllSubtasks().isEmpty(), "Список подзадач должен быть пустым");
    }

    @Test
    void save_shouldSaveFewTasksInFile() throws IOException {
        // prepare
        Task task = new Task("Task 1", "Task Description");
        Epic epic = new Epic("Epic 1", "Epic Description");

        // do
        Task actualTask = taskManager.addNewTask(task);
        Epic actualEpic = taskManager.addNewTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", epic.getId());
        Subtask actualSub = taskManager.addNewTask(subtask);

        // check
        String fileContent = Files.readString(file.toPath());
        assertTrue(fileContent.contains(actualTask.getDescription()), "Файл должен содержать задачу");
        assertTrue(fileContent.contains(actualEpic.getDescription()), "Файл должен содержать эпик");
        assertTrue(fileContent.contains(actualSub.getDescription()), "Файл должен содержать подзадачу");
    }

    @Test
    void save_shouldLoadTasks() throws IOException, ManagerLoadException {
        // prepare
        Task task = new Task("Task 1", "Task Description");
        Epic epic = new Epic("Epic 1", "Epic Description");

        // do
        Task actualTask = taskManager.addNewTask(task);
        Epic actualEpic = taskManager.addNewTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", epic.getId());
        Subtask actualSub = taskManager.addNewTask(subtask);

        // check
        FileBackedTaskManager manager = taskManager.loadFromFile(file);

        Task loadTask = manager.getTask(actualTask.getId());
        Epic loadEpic = manager.getEpic(actualEpic.getId());
        Subtask loadSub = manager.getSubtask(actualSub.getId());

        assertEquals(actualTask, loadTask, "Задачи должны совпадать");
        assertEquals(actualEpic, loadEpic, "Эпики должны совпадать");
        assertEquals(actualSub, loadSub, "Подзадачи должны совпадать");
    }
}
