import manager.InMemoryTaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();


        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        manager.createTask(task1);
        manager.createTask(task2);


        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);


        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getTaskId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getTaskId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", epic2.getTaskId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);


        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtaskById(5);


        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
