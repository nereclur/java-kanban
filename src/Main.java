import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();


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


        manager.getAllTask();
        manager.getAllEpics();
        manager.getAllSubtask();


        task1.setStatus(TaskStatus.DONE);
        Task updatedTask2 = new Task("Задача 2", "Новое описание задачи 2");
        manager.updateTask(updatedTask2, 2);

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.DONE);
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        manager.updateSubtask(subtask3);


        manager.getAllTask();
        manager.getAllEpics();

//        manager.removeTaskById(2);
//        manager.removeEpicById(1);
//
//
//        manager.getAllTask();
//        manager.getAllEpics();
//        manager.getAllSubtask();
    }
}