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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    private FileBackedTaskManager taskManager;
    private File file;

    @BeforeEach
    void init() throws IOException {
        file = java.io.File.createTempFile("backup", "csv");
        taskManager = Managers.getFileBackedTaskManager(file);
    }

    @Test
    void save_shouldSaveEmptyFile() throws IOException {
        // prepare
        Task task = null;

        // do
        taskManager.addNewTask(task);

        // check
        assertTrue(Files.readString(file.toPath()).isEmpty());
    }

    @Test
    void save_shouldLoadEmptyFile() throws ManagerLoadException {
        // prepare
        Task task = null;

        // do
        taskManager.addNewTask(task);
        FileBackedTaskManager newManager = Managers.getFileBackedTaskManager(file);
        newManager.loadFromFile(file);

        // check
        assertTrue(newManager.idTask.isEmpty());
        assertTrue(newManager.idEpic.isEmpty());
        assertTrue(newManager.idSubtask.isEmpty());
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
        boolean isTaskInFile = false;
        boolean isEpicInFile = false;
        boolean isSubInFile = false;
        for (String s : Files.readAllLines(file.toPath())) {
            if (s.contains(actualTask.getDescription())) {
                isTaskInFile = true;
            } else if (s.contains(actualEpic.getDescription())) {
                isEpicInFile = true;
            } else if (s.contains(actualSub.getDescription())) {
                isSubInFile = true;
            }
        }

        assertTrue(isTaskInFile && isEpicInFile && isSubInFile);
    }

    @Test
    void save_shouldLoadTasks() throws ManagerLoadException {
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

        Task loadTask = manager.getTask(0);
        Epic loadEpic = manager.getEpic(1);
        Subtask loadSub = manager.getSubtask(2);

        assertEquals(actualTask, loadTask);
        assertEquals(actualEpic, loadEpic);
        assertEquals(actualSub, loadSub);
    }
}
