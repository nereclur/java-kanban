package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;

public class FileBackedTaskManagerTest {
    public static void main(String[] args) {
        File data = new File("data.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(data);

        // Создание задач
        Task task1 = new Task("Task1", "Description1");
        Task task2 = new Task("Task2", "Description2");
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic = new Epic("Epic1", "Description1");
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask1", "Description1", epic.getTaskId());
        manager.createSubtask(subtask);

        System.out.println("Tasks saved to file: ");
        manager.getAllTask().forEach(System.out::println);
        manager.getAllEpics().forEach(System.out::println);
        manager.getAllSubtask().forEach(System.out::println);

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(data);
        System.out.println("Tasks loaded from file: ");
        loadedManager.getAllTask().forEach(System.out::println);
        loadedManager.getAllEpics().forEach(System.out::println);
        loadedManager.getAllSubtask().forEach(System.out::println);
    }
}
