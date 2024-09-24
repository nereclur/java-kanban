import manager.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = new File("data.csv");
        manager = new FileBackedTaskManager(tempFile);
        manager.clearAllTask();
        manager.clearAllEpic();
        manager.clearAllSubtask();
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void shouldSaveAndLoadEmptyFile() {
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getAllTask().isEmpty(), "Пустой файл");
    }

    @Test
    void shouldSaveMultipleTasks() {
        Task task1 = new Task("пример", "описание 1");
        Task task2 = new Task("пример 2", "описание 2");
        Task task3 = new Task("пример 3", "описание 3");
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        List<Task> tasks = FileBackedTaskManager.loadFromFile(tempFile).getAllTask();
        assertEquals(3, tasks.size(), "Должно быть 3 задания");
    }
    
    @Test
    void shouldLoadFile() {
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> tasks = loadedManager.getAllTask();
        assertEquals(3, tasks.size(), "Должно быть 3 задания");
    }

}